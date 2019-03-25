package com.blg.dorm.common.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;

import com.blg.dorm.common.CommonLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 公共切面日志
 */
public class CommonAopLog {
	private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
    		.enableComplexMapKeySerialization().create() ;

	/**
	 * 切面日志记录方法
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object logCutMethod(ProceedingJoinPoint pjp) throws Throwable{
		long start = System.currentTimeMillis();

//			String className = pjp.getTarget().getClass().getName() ;
		String simpleClassName = pjp.getTarget().getClass().getSimpleName() ;
		String methodName = pjp.getSignature().getName() ;
		
		StringBuffer buf = new StringBuffer("AOPLOG|");
		buf.append(simpleClassName)
		.append(".").append(methodName)
		.append("|args|").append(this.getArgsTypeAndValue(pjp.getArgs())) ;

		try {
			Object result = pjp.proceed();
			String json = "";
			if(result !=null){
				json = gson.toJson(result) ;
			}
			if(json.length()>300){
				json = json.substring(0,300) ;
			}
			buf.append("|result|").append(json)
			.append("|").append(System.currentTimeMillis()-start) ;
			CommonLog.info(buf.toString()) ;
			
			return result;
		} catch (Exception e) {
			String content = buf.toString() ;
			buf.append("|result|Exception:").append(e.getMessage())
			.append("|").append(System.currentTimeMillis()-start) ;
			CommonLog.error(buf.toString()) ;
//			DingDingUtil.sendTextMessage(content + CommonLog.getException(e)) ;
			throw e;
		}
	}

	/**
	 * service层切面日志
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object serviceCutMethod(ProceedingJoinPoint pjp) throws Throwable{
		long start = System.currentTimeMillis();

//			String className = pjp.getTarget().getClass().getName() ;
		String simpleClassName = pjp.getTarget().getClass().getSimpleName() ;
		String methodName = pjp.getSignature().getName() ;

		StringBuffer buf = new StringBuffer("AOPLOG|");
		buf.append(simpleClassName)
				.append(".").append(methodName)
				.append("|args|").append(this.getArgsTypeAndValue(pjp.getArgs())) ;
		try {
			Object result = pjp.proceed();
			String json = "";
			if(result !=null){
				json = gson.toJson(result) ;
			}
			if(json.length()>300){
				json = json.substring(0,300) ;
			}
			buf.append("|result|").append(json)
					.append("|").append(System.currentTimeMillis()-start) ;
			CommonLog.info(buf.toString()) ;
			return result;
		} catch (Exception e) {
			String content = buf.toString() ;
			buf.append("|result|Exception:").append(e.getMessage())
					.append("|").append(System.currentTimeMillis()-start) ;
			CommonLog.error(buf.toString()) ;
//			DingDingUtil.sendTextMessage(content + CommonLog.getException(e)) ;
			throw e;
		}
	}

	/**
	 * dao层切面日志
	 * 记录dao层的日志记录及错误信息
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object daoCutMethod(ProceedingJoinPoint pjp) throws Throwable{
		long start = System.currentTimeMillis();
		String simpleClassName = pjp.getTarget().getClass().getSimpleName() ;
		String methodName = pjp.getSignature().getName() ;
		StringBuffer buf = new StringBuffer("AOPLOG|");
		buf.append(simpleClassName).append(".").append(methodName)
				.append("|args|").append(this.getArgsTypeAndValue(pjp.getArgs())) ;
		try {
			Object result = pjp.proceed();
			String json = "";
			if(result !=null){
				json = gson.toJson(result) ;
			}
			if(json.length()>300){
				json = json.substring(0,300) ;
			}
			buf.append("|result|").append(json)
					.append("|").append(System.currentTimeMillis()-start) ;
			CommonLog.info(buf.toString()) ;
			return result;
		} catch (Exception e) {
			String content = buf.toString() ;
			buf.append("|result|Exception:").append(e.getMessage())
					.append("|").append(System.currentTimeMillis()-start) ;
			CommonLog.error(buf.toString()) ;
			DingDingUtil.sendTextMessage(content + CommonLog.getException(e)) ;
			throw e;
		}
	}
	/**
	 * 处理参数
	 * 拼接参数类型和值，获取请求IP地址
	 * @param args
	 * @return
	 */
	private StringBuffer getArgsTypeAndValue(Object[] args){
		StringBuffer buf = new StringBuffer() ;
		if(args==null || args.length==0){
			return buf ;
		}
		String ip = "" ;
		for (Object obj:args) {
			if(obj==null){
				continue ;
			}
			try {
				if(!(obj instanceof HttpServletRequest || obj instanceof HttpServletResponse || obj instanceof File)){
					String json = gson.toJson(obj).replaceAll("\\|", "#") ;
					if(json!=null && json.length()>500){
						json = json.substring(0,500) ;
					}
					buf
					.append("[").append(obj.getClass().getSimpleName()).append("]")
					.append(json).append(";") ;
				}
				if(obj instanceof HttpServletRequest){
					HttpServletRequest request = (HttpServletRequest) obj ;
					ip = HttpClientUtil.getIpAddress(request) ;
				}
			} catch (Exception e) {
				CommonLog.error(e);
			}
		}
		buf.append("|") ;
		if(ip!=null){
			buf.append(ip) ;
		}
		return buf ;
	}

}
