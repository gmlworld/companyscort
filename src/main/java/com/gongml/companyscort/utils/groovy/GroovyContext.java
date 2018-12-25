package com.gongml.companyscort.utils.groovy;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

import com.gongml.companyscort.utils.groovy.groovyparse.IGroovyParser;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.reflection.GroovyClassValue;

@Component
public class GroovyContext {
	private final Object lock = new Object();
	private final Object runlock = new Object();
	@Autowired
	private GroovyShell2 shell;
	private Hashtable<String, Script> cache = new Hashtable<String, Script>();

	public Object parseExpr(String expr) {
		Script s = getScriptFromCache(expr);
		shell.getClassLoader().clearCache();
		return s.run();
	}

	@SuppressWarnings("unchecked")
	public <T> T execExpr(String expr, String key, Object value, Class<T> clazz) {
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		Binding binding = new Binding(map);
		Script script = getScriptFromCache(expr);
		Object object = runScript(script,binding);
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	public Object execExpr(String expr, Map<?, ?> map) {
		Binding binding = new Binding(map);
		Script script = getScriptFromCache(expr);
		return runScript(script,binding);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T execExpr(String expr, Map<?, ?> map,Class<T> clazz) {
		Binding binding = new Binding(map);
		Script script = getScriptFromCache(expr);
		Object object = runScript(script,binding);
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	public <T> T parseExpr(String expr, Map<?, ?> map, Class<T> clazz) {
		Binding binding = new Binding(map);
		Script script = getScriptFromCache(expr);
		Object object = runScript(script,binding);
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	public Object execExpr(String expr) {
		Script s = getScriptFromCache(expr);
		shell.getClassLoader().clearCache();
		return s.run();
	}

	/** 
	* @Description: 执行grovvy脚本解析html 
	* @Param: [html, script] 
	* @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>> 
	* @Author: Gongml
	* @Date: 2018-12-25 
	*/ 
	public List<Map<String, String>> executeGroovy(String html,String script) throws Exception {
		// 执行解析
		List<Map<String, String>> tableDataList = new LinkedList<>();
		if (StringUtils.isEmpty(script)) {
			return tableDataList;
		}
		GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
		Class<?> clazz = groovyClassLoader.parseClass(script);
		Object object;
		try {
			object = clazz.newInstance();
			if (object instanceof IGroovyParser) {
				IGroovyParser<String,List> paser = (IGroovyParser<String,List>) object;
				tableDataList = paser.parse(html);
			}
			clearAllClassInfo(clazz);
		} catch (IllegalAccessException e) {
			throw new Exception("执行grovvy脚本解析异常", e);
		}finally {
			if(groovyClassLoader!=null) {
				groovyClassLoader.clearCache();
				groovyClassLoader.close();
			}
		}
		return tableDataList;
	}

	//-Dgroovy.use.classvalue=true
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void clearAllClassInfo(Class<?> type) throws Exception {
        Field globalClassValue = ClassInfo.class.getDeclaredField("globalClassValue");
        globalClassValue.setAccessible(true);
        GroovyClassValue classValueBean = (GroovyClassValue) globalClassValue.get(null);
        classValueBean.remove(type);
    }

	
	private Script getScriptFromCache(String expr) {
		if (cache.contains(expr)) {
			return cache.get(expr);
		}
		synchronized (lock) {
			if (cache.keySet().contains(expr)) {
				return cache.get(expr);
			}
			Script script = shell.parse(expr);
			cache.put(expr, script);
			return script;
		}
	}

	public Object getBinding(String expr, String key) {
		Script script = getScriptFromCache(expr);
		return script.getBinding().getVariable(key);
	}
	
	/** 
	* @Description: 多个线程获取到同一个script，在绑定数据到获取结果的过程需要同步 
	* @Param: [script, binding] 
	* @return: java.lang.Object 
	* @Author: Gongml
	* @Date: 2018-12-25 
	*/ 
	public Object runScript(Script script,Binding binding){
		synchronized (runlock) {
			script.setBinding(binding);
			shell.getClassLoader().clearCache();
			return script.run();
		}
	}
}
