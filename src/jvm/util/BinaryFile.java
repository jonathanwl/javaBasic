package jvm.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 读取二进制文件
 * @author jonathan
 *
 */
public class BinaryFile {
	public static byte[] read(File file) throws IOException{
		byte[] b = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			b = new byte[bis.available()];
			bis.read(b);
			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			bis.close();
		}
		return null;
	}
	public static byte[] read(String sfile) throws IOException{
		return read(new File(sfile));
	}
	public static void main(String[] args) {

	}

}
