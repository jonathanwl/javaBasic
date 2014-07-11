package jvm.nio;

import java.io.*;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
public class MappedIO {
	
	private static int numOfInts = 4000000;
	private static int numOfUBuffIns = 200000;
	private abstract static class Tester{
		private String name;
		public Tester(String name){
			this.name=name;
		}
		public void runtest(){
			System.out.println(name+":");
			long start = System.nanoTime();
			try {
				test();
				double duration = System.nanoTime()-start;
				System.out.format("%.2f\n", duration/1.0e9);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		public abstract void test() throws IOException;
	}
	private static Tester[] tests = {
		new Tester("Stream writer"){
			@Override
			public void test() throws IOException {
				DataOutputStream dos =new DataOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(new File("d:\\temp.tmp"))));
				try{
					for(int i=0;i<numOfInts;i++){
							dos.writeInt(i);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						dos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		},
		new Tester("Mapped writer"){
			@Override
			public void test() throws IOException{
				FileChannel fc = new RandomAccessFile("d:\\temp.tmp","rw").getChannel();
				try{
					IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
					for(int i = 0;i<numOfInts ;i++){
						ib.put(i);
					}
				}catch(IOException e){
					e.printStackTrace();
				}finally{ fc.close();}
			}
		},
		new Tester("Stram read"){
			@Override
			public void test() throws IOException {
				DataInputStream dis = new DataInputStream(new BufferedInputStream(
						new FileInputStream("D:\\temp.tmp")));
				for(int i = 0;i<numOfInts ;i++){
					dis.readInt();
				}
				dis.close();
			}
			
		},
		new Tester("Mapped read"){
			@Override
			public void test() throws IOException {
				FileChannel fc = new FileInputStream(new File("d:\\temp.tmp")).getChannel();
				try{
					IntBuffer ib = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).asIntBuffer();
					while(ib.hasRemaining()){
						ib.get();
					}
				}catch(IOException e){
					e.printStackTrace();
				}finally{ fc.close();}
			}
			
		},
		
		new Tester("Stream read/writer"){
			@Override
			public void test() throws IOException {
				RandomAccessFile raf = new RandomAccessFile(new File("D:\\tmp.tmp"),"rw");
				raf.writeInt(1);
				for(int i=0;i<numOfUBuffIns;i++){
					raf.seek(raf.length()-4);
					raf.writeInt(raf.readInt());
				}
				raf.close();
			}
		},
		new Tester("Mapped read/write"){
			@Override
			public void test() throws IOException {
				FileChannel fc =  new RandomAccessFile("d:\\temp.tmp","rw").getChannel();
				try{
					IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
					ib.put(0);
					for(int i=1;i<numOfUBuffIns;i++){
						ib.put(ib.get(i-1));
					}
				}catch(IOException e){
					e.printStackTrace();
				}finally{ fc.close();}
			}
		}
	};
	
	public static void main(String[] args) {
		for(Tester test : tests){
			test.runtest();
		}
	}
}
