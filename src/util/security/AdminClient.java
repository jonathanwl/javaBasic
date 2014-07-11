package util.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class AdminClient {

	public static void main(String args[] ) {
		Socket socket = null;
		try{
			socket = new Socket("127.0.0.1",8001);
			OutputStream os = socket.getOutputStream();
			os.write("shutdown\r\n".getBytes());
//			try {
//				socket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg;
			while((msg=br.readLine())!=null){
				System.out.println(msg);
			}
		}catch(Exception e){
			System.out.println("22222222222222");
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
