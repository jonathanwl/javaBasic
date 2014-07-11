package jvm.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class BufferToText {

	private static final int SIZE = 1024;
	
	public static void main(String[] args) throws IOException {
		FileChannel fc = new FileOutputStream("d://data2.txt").getChannel();
		fc.write(ByteBuffer.wrap("some text".getBytes()));
		fc.close();
		
		fc = new FileInputStream("d://data2.txt").getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(SIZE);
		fc.read(buffer);
		buffer.flip();
		System.out.println("11="+ buffer.asCharBuffer());
		
		buffer.rewind();
		String encoding = System.getProperty("file.encoding");
		System.out.println("decoded using "+ encoding+":"+Charset.forName(encoding).decode(buffer));
		fc = new FileOutputStream("d://data2.txt").getChannel();
		fc.write(ByteBuffer.wrap("some text".getBytes("UTF-16BE")));
		fc.close();
		
		fc = new FileInputStream("d://data2.txt").getChannel();
		buffer.clear();
		fc.read(buffer);
		buffer.flip();
		System.out.println(buffer.asCharBuffer());
		
		fc = new FileOutputStream("d://data2.txt").getChannel();
		buffer = ByteBuffer.allocate(24);
		buffer.asCharBuffer().put("some text");
		fc.write(buffer);
		fc.close();
		
		fc = new FileInputStream("d://data2.txt").getChannel();
		buffer.clear();
		fc.read(buffer);
		buffer.flip();
		System.out.println(buffer.asCharBuffer());
	}

}
