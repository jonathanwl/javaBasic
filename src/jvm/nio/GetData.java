package jvm.nio;

import java.nio.ByteBuffer;

public class GetData {
	public static final int SIZE = 1024;
	public static void main(String[] args) {
		ByteBuffer buff = ByteBuffer.allocate(SIZE);
		int i = 0 ;
		System.out.println(buff.limit());
		while(i++ < buff.limit()){
			if(buff.get()!=0){
				System.out.println("!0");
			}
		}
		System.out.println("i="+i);
		buff.rewind();
		buff.asCharBuffer().put("howdy");
		char c;
		while((c=buff.getChar())!=0){
			System.out.print(c+"=== ");
		}
		buff.rewind();
		
		buff.asIntBuffer().put(9999999);
		System.out.println(buff.getInt());
		buff.rewind();
	}

}
