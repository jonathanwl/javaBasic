package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	private int port = 8000;
	private ServerSocket serverSocket;
	
	public EchoServer() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("服务器启动！");
	}
	private Reader getReader(Socket socket) throws IOException{
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	private Writer getWriter(Socket socket) throws IOException{
			return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
	}
	public void service() {
		while(true){
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				PrintWriter pw = (PrintWriter) getWriter(socket);
				BufferedReader br = (BufferedReader) getReader(socket);
				String msg = null;
				while((msg=br.readLine())!=null){
					System.out.println(msg);
					pw.println("回复："+msg);
					if(msg.equals("bye")){
						break;
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws IOException {
		new EchoServer().service();
	}

}
