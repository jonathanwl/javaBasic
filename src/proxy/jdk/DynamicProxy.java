package proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler{
	private Object o;
	
	/**
	 * 传入被代理的对象
	 * @param o
	 */
	public DynamicProxy(Object o){
		this.o = o;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("procy before....");
		method.invoke(o, args);
		System.out.println("procy after....");
		return null;
	}

}
