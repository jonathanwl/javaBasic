package socket.threadJDK;

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
	private int port = 8000;
	private ServerSocket serverSocket;
	private ExecutorService service;
	private ServerSocket serverSocketShutDown ;//定义关闭通信服务
	private int portShutDown = 8001;
	private int threadnum = 4;
	
	private Thread shutDownThread = new Thread(){
		public void start(){
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
					service.shutdown(); //请求关闭线程池
					//判断线程池内任务是否完成，未完成则设置等待时间60m
					try {
						while(!service.isTerminated()){
							service.awaitTermination(30, TimeUnit.SECONDS);
						}
					} catch (InterruptedException e) {
						System.out.println("111111111");
						e.printStackTrace();
					}finally{
						//关闭通信
						System.out.println("111111111");
						serverSocket.close();
						long endTime = System.currentTimeMillis();
						socketShutDown.getOutputStream().write(("服务器已经关闭,关闭事件为"+(endTime-beginTime)+"毫秒").getBytes());
						socketShutDown.close();
						serverSocketShutDown.close();
						
					}
				}
			} catch (IOException e) {
				System.out.println("111111111");
				e.printStackTrace();
			}
		}
	};
	
	public EchoServerShutDown() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serverSocketShutDown = new ServerSocket(portShutDown);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//初始化线程池v  Runtime.getRuntime().availableProcessors()返回cpu数目
		service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*threadnum);
		shutDownThread.start(); //启动关闭监听线程
		System.out.println("服务器启动！");
	}
	
	public void service(){
		while(true){
			Socket socket = null;
			try {
				socket = serverSocket.accept(); 
				//往线程池中增加工作任务线程
				service.execute(new Handler(socket));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Handler implements Runnable{
		private Socket socket;
		public Handler(Socket socket){
			this.socket = socket;
		}
		private BufferedReader getReader(Socket socket) throws IOException{
			return new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		private PrintWriter getWriter(Socket socket) throws IOException{
				return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
		}
		@Override
		public void run() {
			System.out.println("新的连接接入 "+socket.getInetAddress()+":"+socket.getPort());
			try {
				BufferedReader br = getReader(socket);
				PrintWriter pw = getWriter(socket);
				String msg = "";
				while((msg=br.readLine()) != null){
					System.out.println(msg);
					pw.print("sssssssssssss");
					if(msg.equals("bye")){
						break;
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static void main(String[] args) throws IOException {
		EchoServerShutDown a = new EchoServerShutDown();
		a.service();
	}
}
