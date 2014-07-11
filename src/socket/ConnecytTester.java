package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnecytTester {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 25;
		
	}
	public void connect(String host,int port){
		SocketAddress remoteAddr = new InetSocketAddress(host,port);
		Socket socket = null;
		socket = new Socket();
		String result = "";
		try {
			long begin = System.currentTimeMillis();
			socket.connect(remoteAddr,1000);
			long end = System.currentTimeMillis();
			result = (end - begin)+"ms";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
