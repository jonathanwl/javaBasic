package util.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 使用jdk线程池，并且客户端可以关闭通信服务
 * @author jonathan
 *
 */
public class EchoServerShutDown {
	private ServerSocket serverSocketShutDown ;//定义关闭通信服务
	private int portShutDown = 8001;
	
	private Thread shutDownThread = new Thread(){
		@Override
		public void start(){
			System.out.println("1111111111111");
			this.setDaemon(true);//设置此线程为守护线程
			super.start();
		}
		@Override
		public void run(){
			try {
				Socket socketShutDown = null;
				socketShutDown = serverSocketShutDown.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(socketShutDown.getInputStream()));
				String msg = null;
				while((msg=br.readLine())!=null){
					long beginTime = System.currentTimeMillis();
					socketShutDown.getOutputStream().write("服务器正在关闭\r\n".getBytes());
						//关闭通信
						System.out.println("111111111");
						long endTime = System.currentTimeMillis();
						socketShutDown.getOutputStream().write(("服务器已经关闭,关闭事件为"+(endTime-beginTime)+"毫秒").getBytes());
						socketShutDown.close();
						serverSocketShutDown.close();
						
					}
				}catch(Exception e){
					
				}
			}
	};
	
	public EchoServerShutDown() {
		try {
			serverSocketShutDown = new ServerSocket(portShutDown);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		shutDownThread.start(); //启动关闭监听线程
		System.out.println("服务器启动！");
	}
	
	public static void main(String[] args) throws IOException {
		new Thread(){
			@Override
			public void run(){
				while(true){
					
				}
			}
		}.start();
		
		EchoServerShutDown a = new EchoServerShutDown();
	}
}
