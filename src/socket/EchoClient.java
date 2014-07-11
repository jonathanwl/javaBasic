package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {

	private String host = "localhost";
	private int port = 8000;
	private Socket socket;
	public EchoClient() throws UnknownHostException, IOException{
		socket = new Socket(host,port);
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		new EchoClient().talk();

	}
	private Reader getReader(Socket socket) throws IOException{
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	private Writer getWriter(Socket socket) throws IOException{
			return new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
	}
	
	public void talk() {
		try{
			BufferedReader br = (BufferedReader) getReader(socket);
			PrintWriter pw = (PrintWriter) getWriter(socket);
			BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			while((msg=localReader.readLine())!=null){
				pw.println(msg);
				System.out.println(br.readLine());
				if(msg.equals("bye")){
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
