package jvm.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class LargeMappedFiles {
	static int length = 0x8ffffff;
	public static void main(String[] args) throws FileNotFoundException, IOException {
		MappedByteBuffer out = new RandomAccessFile("D://test.dat","rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
		
		
	}

}
