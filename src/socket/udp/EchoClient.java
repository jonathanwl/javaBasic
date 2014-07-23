package socket.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
	private String remoteHost = "localhost";
	private int remotePort = 8000;
	private DatagramSocket socket;
	public EchoClient() throws SocketException{
		socket = new DatagramSocket();
	}
	public void talk() throws IOException{
		InetAddress remoteIP = InetAddress.getByName(remoteHost);
		BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
		String msg = null;
		while((msg=localReader.readLine())!=null){
			byte[] outputData = msg.getBytes();
			DatagramPacket outputPacket = new DatagramPacket(outputData,outputData.length,
					remoteIP,remotePort);
			socket.send(outputPacket);
			DatagramPacket inputPacket = new DatagramPacket(new byte[512],512);
			socket.receive(inputPacket);
			syso
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
