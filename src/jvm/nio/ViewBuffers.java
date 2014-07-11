package jvm.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class ViewBuffers {

	
	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[]{
				0,0,0,0,0,0,0,0,'a'
		});
		bb.rewind();
		System.out.println("Byte buffer");
		while(bb.hasRemaining()){
			System.out.print(bb.position()+"->"+bb.get()+",");
		}
		
		CharBuffer cb = ((ByteBuffer)bb.rewind()).asCharBuffer();
		System.out.println("\n char buffer");
		while(cb.hasRemaining()){
			System.out.print(cb.position()+"->"+cb.get()+",");
		}
	}

}
