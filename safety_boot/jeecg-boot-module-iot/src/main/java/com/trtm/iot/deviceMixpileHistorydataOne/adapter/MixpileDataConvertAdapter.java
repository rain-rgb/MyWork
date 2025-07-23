//package com.trtm.iot.deviceMixpileHistorydataOne.adapter;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.jeecg.modules.jmreport.desreport.render.handler.convert.ApiDataConvertAdapter;
//import org.springframework.stereotype.Component;
//
//@Component("customParser")
//public class MixpileDataConvertAdapter  implements ApiDataConvertAdapter {
//
//    /**
//     * 返回list数据集，转换成积木报表需要格式{}，没有嵌套
//     * 注意：需要json格式，不用data包裹起来了
//     * @param jsonObject 接口数据原始对象
//     * @return
//     */
//    @Override
//    public String getData(JSONObject jsonObject) {
//        if(jsonObject.containsKey("pagelist")){
//            JSONArray pageList = jsonObject.getJSONArray("pagelist");
//            JSONArray array = new JSONArray();
//            for (int i = 0; i < pageList.size(); i++) {
//                JSONObject object = new JSONObject();
//                String shebeino = pageList.getJSONObject(i).getString("shebeino");
//                String pileNo = pageList.getJSONObject(i).getString("pileNo");
//                String piledesigndep = pageList.getJSONObject(i).getString("pileDesigndep");
//                String pilerealdep = pageList.getJSONObject(i).getString("pileRealdep");
//                String piledownbeton = pageList.getJSONObject(i).getString("pileDownbeton");
//                String pileuobeton = pageList.getJSONObject(i).getString("pileUobeton");
//                String pilex = pageList.getJSONObject(i).getString("pileX");
//                String datatime = pageList.getJSONObject(i).getString("datatime");
//                String pileTime = pageList.getJSONObject(i).getString("pileTime");
//
////                object.put("name",name);
////                object.put("id",id);
////                object.put("zhicheng",zhicheng);
////                object.put("banji",banji);
////                object.put("xueke",xueke);
////                array.add(object);
//
//            }
//            return array.toJSONString();
//        }else{
//            return "";
//        }
//    }
//
//    /**
//     * 返回links（没有图表属性可以删掉）
//     * @param jsonObject 接口数据原始对象
//     * @return
//     */
//    @Override
//    public String getLinks(JSONObject jsonObject) {
//        return jsonObject.containsKey("links") ? jsonObject.get("links").toString() : "";
//    }
//
//    /**
//     * 返回总页数（没有分页可以删掉）
//     * @param jsonObject 接口数据原始对象
//     * @return
//     */
//    @Override
//    public String getTotal(JSONObject jsonObject) {
//        return jsonObject.containsKey("pageNumber") ? jsonObject.get("pageNumber").toString() : "0";
//    }
//
//    /**
//     * 返回总条数（没有分页可以删掉）
//     * @param jsonObject 接口数据原始对象
//     * @return
//     */
//    @Override
//    public String getCount(JSONObject jsonObject) {
//        return jsonObject.containsKey("count") ? jsonObject.get("count").toString() : "0";
//    }
//}