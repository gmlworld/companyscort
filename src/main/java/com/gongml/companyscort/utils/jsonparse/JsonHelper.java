package com.gongml.companyscort.utils.jsonparse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gongml.companyscort.utils.jsonparse.bean.TreeNode;
import com.jayway.jsonpath.JsonPath;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
* @Description:json解析器
* @Author: Gongml
* @Date: 2018-12-27
*/
public class JsonHelper {

	public static String trimJsonStr(String jsonStr) {
		int oTag = jsonStr.indexOf("{");
		int aTag = jsonStr.indexOf("[");
		if (oTag >= 0 && (aTag < 0 || oTag < aTag)) {
			try {
				jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1).trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (aTag < 0){
				return jsonStr;
			}
			try {
				jsonStr = jsonStr.substring(jsonStr.indexOf("["), jsonStr.lastIndexOf("]") + 1).trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonStr;
	}

	// json解析成树状结构,树节点信息为json-path
	public static List<TreeNode> analyze(String jsonStr) {
		int oTag = jsonStr.indexOf("{");
		int aTag = jsonStr.indexOf("[");
		List<TreeNode> list = new ArrayList<>();
		TreeNode head = new TreeNode();
		head.setName("$");
		head.setParam1("$");
		if (oTag >= 0 && (aTag < 0 || oTag < aTag)) {
			try {
				if (!jsonStr.startsWith("{")) {
					jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1).trim();
				}
				JSONObject obj = JSONObject.parseObject(jsonStr);
				head.addChildAll(analyze(obj, "$"));
				list.add(head);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			if (aTag < 0){
				return null;
			}
			try {
				if (!jsonStr.startsWith("[")) {
					jsonStr = jsonStr.substring(jsonStr.indexOf("["), jsonStr.lastIndexOf("]") + 1).trim();
				}
				JSONArray obj = JSONArray.parseArray(jsonStr);
				head.addChildAll(analyze(obj, "$"));
				list.add(head);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	private static List<TreeNode> analyze(Object objJson, String path) {
		List<TreeNode> list = new ArrayList<>();
		// 对JSONArray数组进行循环遍历
		if (objJson instanceof JSONArray) {
			analyzeJsonArray(list, objJson, path);
			// 如果为json对象
		} else if (objJson instanceof JSONObject) {
			// 将Object对象转换为JSONObject对象
			analyzeJsonObject(list, objJson, path);
		} else {
			TreeNode node = new TreeNode();
			node.setName(objJson.toString());
			list.add(node);
		}
		return list;
	}

	private static void analyzeJsonArray(List<TreeNode> list, Object objJson, String path) {
		JSONArray objArray = (JSONArray) objJson;
		// 对JSONArray数组进行循环遍历
		for (int i = 0; i < objArray.size(); i++) {
			Object obj = objArray.get(i);
			TreeNode treeNode = new TreeNode();
			if (obj instanceof JSONObject) {
				treeNode.setName("[" + i + "]");
				treeNode.setParam1(path + "[" + i + "]");
				treeNode.addChildAll(analyze(obj, path + "[" + i + "]"));
			} else if (obj instanceof JSONArray) {
				treeNode.setParam1(path + "[" + i + "]");
				treeNode.setName("[" + i + "]");
				treeNode.addChildAll(analyze(obj, path + "[" + i + "]"));
			} else {
				treeNode.setParam1(path + ".[" + i + "]");
				treeNode.setName("[" + i + "]:" + obj);
			}
			list.add(treeNode);
		}
	}

	private static void analyzeJsonObject(List<TreeNode> list, Object objJson, String path) {
		JSONObject jsonObject = (JSONObject) objJson;
		// 迭代多有的Key值
		Iterator<Entry<String, Object>> it = jsonObject.entrySet().iterator();
		// 遍历每个Key值
		while (it.hasNext()) {
			// 将key值转换为字符串
			String key = it.next().getKey();
			// 根据key获取对象
			Object object = jsonObject.get(key);
			// 如果得到的是数组
			TreeNode oNode = new TreeNode();
			oNode.setName("." + key);
			oNode.setParam1(path + "." + key);
			if (object instanceof JSONArray) {
				// 将Object对象转换为JSONObject对象
				JSONArray objArray = (JSONArray) object;
				// 调用回调方法
				oNode.addChildAll(analyze(objArray, path + "." + key));
			} else if (object instanceof JSONObject) {
				// 调用回调方法
				oNode.addChildAll(analyze((JSONObject) object, path + "." + key));
			} else {
				oNode.setName("." + key + ":" + object);
			}
			list.add(oNode);
		}
	}

	public static List<Map<String, Object>> listKeyMaps(String jsonStr, String[] keys) {
		List<Map<String, List<?>>> list = new ArrayList<Map<String, List<?>>>();
		int size = 0;
		for (String key : keys) {
			Map<String, List<?>> map = new HashMap<String, List<?>>();
			String jsonPath = "$.listInfo.content[*]." + key;
			List<Object> mList = JsonPath.read(jsonStr, jsonPath);
			map.put(key, mList);
			size = mList.size();
			list.add(map);
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < list.size(); j++) {
				map.put(keys[j], list.get(j).get(keys[j]).get(i));
			}
			result.add(map);
		}
		return result;
	}

}
