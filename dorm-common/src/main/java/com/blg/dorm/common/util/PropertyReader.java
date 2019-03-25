package com.blg.dorm.common.util;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 用于读取properties中的数据。<br/>
 * 注意：<br/>
 * 1、使用前必须要在spring配置文件中进行配置，配置方式如下：<pre>
    <bean id="propertyReader" class="com.sys.common.PropertyReader">
        <property name="locations">
            <list>
                <value>classpath:props/*.properties</value>
            </list>
        </property>
    </bean>
    </pre>
 * 2、该数据在启动后不会在重新加载。<br/>
 * 3、可以加载多个配置文件中的内容，但是key不能出现重复，否则会发生覆盖。<br/>
 * 
 */
public class PropertyReader extends PropertyPlaceholderConfigurer implements Serializable {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -4597092453265262225L;
	
	private static final Logger log = LoggerFactory.getLogger(PropertyReader.class);
	
	protected static Properties ctxProps = null;

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		Iterator<Object> it = props.keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			String value = props.getProperty(key);
			log.debug("load context prop: k={};v={}", key, value);
		}
		log.debug("初始化PropertiesReader，开始加载properties配置文件中的数据！");
		ctxProps = props;
	}
	
	/**
	 * 判断PropertyReader是否进行初始化，如果没有，则抛出IllegalStateException
	 */
	private static void assertInit(){
		if(null == ctxProps){
			throw new IllegalStateException("当前PropertyReader中未进行properties文件注入，请在spring文件中进行配置！");
		}
	}
	
	/**
	 * 根据key获取配置文件中的值，在取值之前会对数据集合进行判断，如果数据集合为null，会抛出异常
	 * @param key properties配置文件中的属性名称
	 * @return java.lang.String 配置文件中属性名称相对应的属性值
	 */
	public static String getContextProperty(String key) {
		assertInit();
		return ctxProps.getProperty(key);
	}
	
	public static HashMap<String,String> getAllPropertys(){
		HashMap<String,String> hash = new HashMap<String,String>() ;
		for(Object key:ctxProps.keySet()){
			hash.put(key.toString(), ctxProps.getProperty(key.toString())) ;
		}
		return hash ;
	}
}
