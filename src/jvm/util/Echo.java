package jvm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Echo {

	public static void main(String[] args) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String s;
//		while((s=br.readLine()) != null && s.length() > 0){
//			System.out.println(s);
//			System.out.println("111111");
//		}
		File f = new File("d:////a.tp");
		f.renameTo(new File("d:////a.zip"))
;	}

}
