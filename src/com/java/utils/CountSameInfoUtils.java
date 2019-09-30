package com.java.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/***
 * 统计相同key值相同的数量
 * @author sjwy-0001
 *
 */
public class CountSameInfoUtils {
	
	
	public static void main(String[] args) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(16);
		Map<String, Object> map1 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map2 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map3 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map4 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map5 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map6 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map7 = new LinkedHashMap<String, Object>(16);
		Map<String, Object> map8 = new LinkedHashMap<String, Object>(16);
		map1.put("id", "1");
		map1.put("name", "File");
		list.add(map1);
		map2.put("id", "2");
		map2.put("name", "Edit");
		list.add(map2);
		map3.put("id", "3");
		map3.put("name", "Source");
		list.add(map3);
		map4.put("id", "1");
		map4.put("name", "File");
		list.add(map4);
		map5.put("id", "3");
		map5.put("name", "Source");
		list.add(map5);
		map6.put("id", "1");
		map6.put("name", "File");
		list.add(map6);
		map7.put("id", "4");
		map7.put("name", "Refactor");
		list.add(map7);
		map8.put("id", "2");
		map8.put("name", "Edit");
		list.add(map8);
		countSameInfo(list);
	}
	
	public static Map<String, Integer> countSameInfo(List<Map<String, Object>> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		for (Map<String, Object> m : list) {
			String id = (String) m.get("id");
			String name = (String) m.get("name");
			List<String> ls = map.get(id);
			if (ls == null) {
				ls = new ArrayList<String>();
			}
			ls.add(name);
			map.put(id, ls);
			returnMap.put(id, ls.size());
		}
		return returnMap;
	}

}
