package socket.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 阻塞的
 * @author jonathan
 *
 */
public class EchoServer {
	private int port = 8000;
	private ServerSocketChannel serverSocketChannel = null;
	private ExecutorService executorService ; //线程池
	private static final int POOL_MULTIPLE = 4; //线程池中工作线程的数目（单CPU）
	public EchoServer() throws IOException{ 
		//创建线程池
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()  
				.availableProcessors()*this.POOL_MULTIPLE);
		serverSocketChannel = ServerSocketChannel.open();
		//设置关闭服务器之后保证再起动绑定相同端口
		serverSocketChannel.socket().setReuseAddress(true);
		//把服务器进程与一个本地端口绑定
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动！");
	}
	public void service(){
		while(true){
			SocketChannel socketChannel = null ;
			try {
				System.out.println("acept");
				socketChannel = serverSocketChannel.accept();
				System.out.println("accept end");
				executorService.execute(new Handler(socketChannel));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 处理客户连接线程
	 * @author jonathan
	 *
	 */
	class Handler implements Runnable{
		private SocketChannel socketChannel;
		public Handler(SocketChannel socketChannel){
			this.socketChannel = socketChannel;
		}
		@Override
		public void run() {
			handle(socketChannel);
		}
		public void handle(SocketChannel socketChannel){
			Socket socket = socketChannel.socket();
			System.out.println("接收到客户连接，来自："+socket.getInetAddress()+":"+
					socket.getPort());
			try {
				
				BufferedReader br = getReader(socket);
				PrintWriter pw = getWriter(socket);
				String msg = null;
				while((msg=br.readLine()) != null){
					System.out.println(msg);
					pw.println(echo(msg));
					if(msg.equals("bye")){
						break;
					}
				}
				System.out.println("end");
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					socketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		private String echo(String msg){
			return "echo:"+msg;
		}
		private BufferedReader getReader(Socket socket) throws IOException{
			return new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		
		private PrintWriter getWriter(Socket socket) throws IOException{
			return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			 new EchoServer().service();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
