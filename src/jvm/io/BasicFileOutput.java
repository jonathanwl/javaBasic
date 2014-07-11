package jvm.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicFileOutput {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("D:\\test\\javaBasic\\src\\jvm\\io\\BasicFileOutput.java"));
		//PrintWriter w = new PrintWriter("BasicFileOutput.out");
		String s ;
		while((s=br.readLine())!=null){
			System.out.println(s);
		}
	}

}
