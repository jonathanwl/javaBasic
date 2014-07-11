package jvm.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GetChannel {

	private static final int SIZE = 1024;
	public static void main(String[] args) throws IOException {
		FileChannel fc = new FileOutputStream("D:\\data.txt").getChannel();
		fc.write(ByteBuffer.wrap("Some text".getBytes()));
		fc.close();
		fc = new RandomAccessFile("D:\\data.txt","rw").getChannel();
		fc.position(fc.size());
		fc.write(ByteBuffer.wrap("som more".getBytes()));
		fc.close();
		fc = new FileInputStream("D:\\data.txt").getChannel();
		ByteBuffer buff = ByteBuffer.allocate(SIZE);
		fc.read(buff);
		buff.flip();
		while(buff.hasRemaining()){
			System.out.print((char)buff.get());
		}
		
	}

}
