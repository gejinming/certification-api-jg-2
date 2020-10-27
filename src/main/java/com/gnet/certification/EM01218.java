package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcSelfreport;
import com.gnet.model.admin.CcSelfreportTitleword;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自评报告文档查询文档标题
 * 
 * @author GJM
 * @Date 2020年09月10日
 */
@Service("EM01218")
public class EM01218 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		//标题列表
		ArrayList<Object> selfItems = new ArrayList<>();
		//一级标题
		List<CcSelfreportTitleword> reportTitle = CcSelfreportTitleword.dao.findReportTitle(1, 0l);
		for (CcSelfreportTitleword oneTitle: reportTitle){
			HashMap<Object, Object> oneTitleMap = new HashMap<>();
			Long oneTitleId = oneTitle.getLong("id");
			String oneTitleName = oneTitle.getStr("title_no")+oneTitle.getStr("title_name");
            oneTitleMap.put("id",oneTitleId);
            oneTitleMap.put("title",oneTitleName);
			oneTitleMap.put("icon","fa fa-lg fa-fw fa-list");
			//判断是否存在二级标题
            List<CcSelfreportTitleword> twoTitleReport = CcSelfreportTitleword.dao.findReportTitle(2, oneTitleId);
            //不存在就为底层
            if (twoTitleReport.size()==0){
                oneTitleMap.put("route",oneTitleId);
            }
            selfItems.add(oneTitleMap);
            //二级标题
            ArrayList<Object> twoTitleList = new ArrayList<>();
            //这个特殊处理
            if (oneTitle.getStr("title_name").equals("背景信息")){
                HashMap<Object, Object> map = new HashMap<>();
                map.put("id",oneTitleId);
                map.put("title",oneTitleName);
                map.put("route",oneTitleId);
                twoTitleList.add(map);
            }
			for (CcSelfreportTitleword twoTitle: twoTitleReport){
                HashMap<Object, Object> twoTitleMap = new HashMap<>();
                Long twoTitleId = twoTitle.getLong("id");
                String twoTitleName = twoTitle.getStr("title_no")+twoTitle.getStr("title_name");
                twoTitleMap.put("id",twoTitleId);
                twoTitleMap.put("title",twoTitleName);
                //判断是否存在三级标题
                List<CcSelfreportTitleword> threeTitleReport = CcSelfreportTitleword.dao.findReportTitle(3, twoTitleId);
                //不存在就为底层
                if (threeTitleReport.size()==0){
                    twoTitleMap.put("route",twoTitleId);
                }
                twoTitleList.add(twoTitleMap);
                oneTitleMap.put("items",twoTitleList);
                ArrayList<Object> threeTitleList = new ArrayList<>();
                for (CcSelfreportTitleword threeTitle : threeTitleReport){
                    HashMap<Object, Object> threeTitleMap = new HashMap<>();
                    Long threeTitleId = threeTitle.getLong("id");
                    String threeTitleName = threeTitle.getStr("title_no")+threeTitle.getStr("title_name");
                    threeTitleMap.put("id",threeTitleId);
                    threeTitleMap.put("route",threeTitleId);
                    threeTitleMap.put("title",threeTitleName);
                    threeTitleList.add(threeTitleMap);
                    twoTitleMap.put("items",threeTitleList);
                }
			}
		}

        result.put("selfItems",selfItems);
		return renderSUC(result, response, header);
	}


}
