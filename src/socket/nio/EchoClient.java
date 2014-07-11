package socket.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class EchoClient {

	private SocketChannel socketChannel = null;
	public EchoClient() throws IOException{
		socketChannel = SocketChannel.open();
		InetAddress ia = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(ia, 8000);
		socketChannel.connect(isa);
		System.out.println("与服务器建立连接成功");
	}
	public void talk(){
		try{
			BufferedReader br = getReader(socketChannel.socket());
			PrintWriter pw= getWriter(socketChannel.socket());
			BufferedReader localreader = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			while((msg=localreader.readLine())!=null){
				pw.println(msg);
				System.out.println(br.readLine());
				if(msg.equals("bye"))break;
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private BufferedReader getReader(Socket socket) throws IOException{
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	private PrintWriter getWriter(Socket socket) throws IOException{
			return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
	}
	public static void main(String[] args) throws IOException {
		new EchoClient().talk();

	}

}
