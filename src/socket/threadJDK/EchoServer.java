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
/**
 * 使用线程池
 * @author jonathan
 *
 */
public class EchoServer {
	private int port = 8000;
	private ServerSocket serverSocket;
	private ExecutorService service;
	public EchoServer() throws IOException{
		serverSocket = new ServerSocket(port);
		//初始化线程池v  Runtime.getRuntime().availableProcessors()返回cpu数目
		service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
}
