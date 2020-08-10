package com.gnet.plugin.validate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gnet.model.DbModel;

@SuppressWarnings("unchecked")
public abstract class Verifiable<M extends DbModel> extends DbModel<M> {
  /**
   * 缓存扫描到的验证器
   */
  private static Map<String, Verifier> vfs = new HashMap<String, Verifier>();

  /**
   * 把曾经验证过的类的相关验证参数缓存起来
   */
  private static Map<String, SoftReference<Map<String, FieldInfo>>> fieldCache =
      new HashMap<String, SoftReference<Map<String, FieldInfo>>>();

  private static Logger logger = Logger.getLogger(Verifiable.class.getName());

  private Set<VerificationResult> results = new HashSet<VerificationResult>();
  private Boolean globalResult = null;

  static {
  }

  /**
   * 扫描验证器
   */
  private static void init() {
    InputStream input = null;
    Properties prop = new Properties();
    try {
      input = Verifiable.class.getClassLoader().getResourceAsStream("validate.properties");
      prop.load(input);
    } catch (IOException e) {
      logger.warning("无法找到验证器配置文件:validate.properties");
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    String pkgStr = prop.getProperty("packages");

    if (pkgStr == null || "".equals(pkgStr))
      return;

    List<String> names = new LinkedList<String>();

    for (String pkg : pkgStr.split(";")) {
      names.addAll(ClassScaner.getClassnameFromPackage(pkg.trim(), true));
    }

    String suffix = Verifier.class.getSimpleName();
    String type;
    Object o;
    for (String n : names) {
      if (n.endsWith(suffix)) {
        try {
          /* 加载验证器 */
          o = Class.forName(n).newInstance();
          if (o instanceof Verifier) {
            type = n.substring(n.lastIndexOf(".") + 1).replace(suffix, "").toLowerCase();
            vfs.put(type, (Verifier) o);
            logger.info("检测到校验器 [classname:" + n + "; type:" + type + "]");
          }
        } catch (Exception e) {
          e.printStackTrace();
          throw new ValidateException("无法初始化验证器:" + n);
        }
      }
    }
  }

  /**
   * 验证bean字段 不指定字段将验证所有可验证的字段
   * 
   * @param fields 指定需要验证的字段，不指定字段将验证所有可验证的字段
   * @return 整体验证是否通过，当有一个字段验证不同，那么就认为整体验证不通过
   */
  public final boolean validate(String... fields) {
    /* 每次验证前，都会清空上次的验证结果 */
    results.clear();
    globalResult = true;

    List<FieldInfo> fs = getFields(fields);
    Valid vInfo;
    Field field = null;
    Object val = null;
    Verifier verifier;
    VerificationResult result;
    try {
      for (FieldInfo info : fs) {
        vInfo = info.validInfo;
        result = null;
        field = info.field;
        // val = field.get(this);
        val = get(field.getName());

        /* 正则验证 */
        Matcher matcher;
        for (String ptn : vInfo.pattern()) {
          if ("".equals(ptn))
            continue;

          matcher = Pattern.compile(ptn).matcher(val.toString());
          result = matcher.matches() ? null : new VerificationResult(field.getName(), "", vInfo.tipMsg(), val, false);
          if (result != null) {
            globalResult = result.result && globalResult;
            results.add(result);
          }
        }

        /* 验证类验证 */
        for (String type : vInfo.rule()) {
          if ("".equals(type))
            continue;

          verifier = vfs.get(type.toLowerCase());

          if (verifier == null)
            throw new ValidateException("找不到 \"" + type + "\" 对应的验证器");

          result = verifier.verify(this, field.getType(), val, field.getName(), vInfo.params());

          if (result != null) {
            globalResult = result.result && globalResult;
            results.add(result);
          }
        }
      }
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (ValidateException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new ValidateException("无法获取" + field + " 字段值");
    }

    return globalResult;
  }

  /**
   * 
   * @param fs
   * @return
   */
  private List<FieldInfo> getFields(String[] fs) {
    Class<?> clazz = this.getClass();
    String clasName = clazz.getName();

    Map<String, FieldInfo> cacheF;
    if (fieldCache.get(clazz.getName()) == null) {
      cacheF = new HashMap<String, FieldInfo>();

      String fieldN;
      Valid anno;
      while (clazz != null && clazz != Object.class) {
        for (Field f : clazz.getDeclaredFields()) {
          fieldN = f.getName();
          anno = f.getAnnotation(Valid.class);

          // 不考虑父类中被子类重写的属性
          if (anno != null && cacheF.get(fieldN) == null) {
            f.setAccessible(true);
            cacheF.put(fieldN, new FieldInfo(f, anno));
          }
        }
        clazz = clazz.getSuperclass();
      }

      fieldCache.put(clasName, new SoftReference<Map<String, FieldInfo>>(cacheF));
    } else {
      cacheF = fieldCache.get(clasName).get();
    }

    List<FieldInfo> infos = new LinkedList<FieldInfo>();
    if (fs.length != 0) {
      FieldInfo info;
      for (String fn : fs) {
        info = cacheF.get(fn);
        if (info == null) {
          throw new ValidateException("there is no field named \"" + fn + "\" need to validate");
        } else {
          infos.add(info);
        }
      }
    } else {
      infos.addAll(cacheF.values());
    }

    return infos;
  }

  /**
   * 获取全局验证结果，失败的验证和成功的验证
   * @return
   */
  public Set<VerificationResult> getValidateResult() {
    return results;
  }
}


class FieldInfo {
  Field field;
  Valid validInfo;

  FieldInfo(Field f, Valid an) {
    field = f;
    validInfo = an;
  }
}
