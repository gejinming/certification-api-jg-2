package com.gnet.utils;

import com.gnet.model.DbModel;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by xuq on 2017/6/16.
 */
public class ConvertUtils {

    /**
     * <p>Convert the value to an object of the specified class (if
     * possible).</p>
     *
     * @param value Value to be converted (may be null)
     * @param targetType Class of the value to be converted to (must not be null)
     * @return The converted value
     *
     * @throws ConversionException if thrown by an underlying Converter
     */
    public static <T> T convert(final Object value, final Class<T> targetType) {
        return  value == null ? null : (T) org.apache.commons.beanutils.ConvertUtils.convert(value, targetType);
    }

    /**
     * @description:
     * @param modelList
     * @param key
     * @return
     */
    public static List<Long> modelListToIdList(List<? extends DbModel> modelList, String key) {
        List<Long> idList = Lists.newArrayList();
        for (DbModel dbModel : modelList) {
            idList.add(dbModel.getLong(StringUtils.isBlank(key) ? "id" : key));
        }
        return idList;
    }

}
