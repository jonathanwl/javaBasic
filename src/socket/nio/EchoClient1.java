package socket.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class EchoClient1 {
	private SocketChannel socketChannel = null;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	private Selector selector ; 
	private Charset charSet = Charset.forName("GBK");
	public EchoClient1() throws IOException{
		socketChannel = SocketChannel.open();
		InetAddress ia = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(ia, 8000);
		socketChannel.connect(isa);
		socketChannel.configureBlocking(false);
		System.out.println("与服务器建立连接成功");
		selector = Selector.open();
	}
	/**
	 * 接收用户从控制台输入数据把他放入sendBuffer中
	 * @throws IOException 
	 */
	public void receiveFromUser() {
		try{
			BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
			String msg = null;
			while((msg=localReader.readLine())!=null){
				synchronized(sendBuffer){
					sendBuffer.put(encode(msg+"\r\n"));
				}
				if(msg.equals("bye"))break;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void talk() throws IOException{
		
			socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			while(selector.select()>0){
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = readyKeys.iterator();
				while(it.hasNext()){
					SelectionKey key = null;
					try{
						key = it.next();
						it.remove();
						if(key.isReadable()){
							receive(key);
						}
						if(key.isWritable()){
							send(key);
						}
					}catch(IOException e){
						e.printStackTrace();
						if(key!=null){
							key.cancel();
							try {
								key.channel().close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
	}
	public void send(SelectionKey key) throws IOException{
		SocketChannel socketChannel = (SocketChannel)key.channel();
		synchronized(sendBuffer){
			sendBuffer.flip();
			socketChannel.write(sendBuffer);
			sendBuffer.compact();
		}
	}
	public void receive(SelectionKey key) throws IOException{
		SocketChannel socketChannel = (SocketChannel)key.channel();
		socketChannel.read(receiveBuffer);
		receiveBuffer.flip();
		String receiveData = decode(receiveBuffer);
		if(receiveData.indexOf("\n")==-1)return;
		String outputdata = receiveData.substring(0,(receiveData.indexOf("\n")+1));
		System.out.println(outputdata);
		if(outputdata.equals("echo:bye\r\n")){
			key.cancel();
			socketChannel.close();
			System.out.println("关闭与服务器连接");
			selector.close();
			System.exit(0);
		}
		ByteBuffer temp = encode(outputdata);
		receiveBuffer.position(temp.limit());
		receiveBuffer.compact();
	}
	private ByteBuffer encode(String s){
		return charSet.encode(s);
	}
	private String decode(ByteBuffer buffer){
		CharBuffer charBuffer= charSet.decode(buffer);
		return charBuffer.toString();
	}
	public static void main(String[] args) throws IOException {
		final EchoClient1 client = new EchoClient1();
		Thread receiver = new Thread(){
			public void run(){
				client.receiveFromUser();
			}
		};
		receiver.start();
		client.talk();

	}

}
