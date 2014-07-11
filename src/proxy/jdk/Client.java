package proxy.jdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class Client {

	/**
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		//指定被代理类
		Subject sub = new RealSubject(); 
		//初始化代理类
		InvocationHandler proxy = new DynamicProxy(sub) ;
		//获得sub的Class
		Class sc= sub.getClass(); 
		
		Class c = Proxy.getProxyClass(sc.getClassLoader(), sc.getInterfaces());
		
		Constructor ct=c.getConstructor(new Class[]{InvocationHandler.class});
		  
		Subject subject =(Subject) ct.newInstance(new Object[]{proxy});
		
		subject.request();
	}

}
