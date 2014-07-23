package socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class EchoServer {
	private int port = 80;
	private DatagramSocket socket = null;
	
	public EchoServer() throws SocketException{
		socket = new DatagramSocket(port);
		System.out.println("服务器启动！");
	}
	private String echo(String msg){
		return "echo:"+msg;
	}
	public	void service(){
		while(true)
			try {
				DatagramPacket packet = new DatagramPacket(new byte[512],512);
				socket.receive(packet);
				String msg = new String(packet.getData(),0,packet.getLength());
				System.out.println(packet.getAddress()+":"+packet.getPort()+">"+msg);
				packet.setData(echo(msg).getBytes());
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public static void main(String[] args) throws SocketException{
		new EchoServer().service();
	}
}
