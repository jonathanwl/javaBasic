package jvm.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.*;

public class WhoAmI {
	public static void main(String[] args) throws Exception {
	//	BufferedReader sb = new BufferedReader(new StringReader(new S));
		BufferedReader buf = new BufferedReader (new InputStreamReader(System.in));  
		String s = buf.readLine();
		InetAddress a = InetAddress.getByName(s);
		System.out.println(a);
	}
}