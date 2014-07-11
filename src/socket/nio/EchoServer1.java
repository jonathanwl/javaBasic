package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
/**
 * 非阻塞的
 * @author jonathan
 *
 */
public class EchoServer1 {
	private int port = 8000;
	private ServerSocketChannel serverSocketChannel = null;
	private Selector selector = null;
	private Charset charset = Charset.forName("GBK");
	
	public EchoServer1() throws IOException{ 
		//创建一个Selector对象
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);//设置为非阻塞
		//设置关闭服务器之后保证再起动绑定相同端口
		serverSocketChannel.socket().setReuseAddress(true);
		//把服务器进程与一个本地端口绑定
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动！");
	}
	public void service() throws IOException{
		//
		serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
		while(selector.select()>0){
			Set readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = readyKeys.iterator();
			
			while(it.hasNext()){
				SelectionKey key = null;
				try{
					key = it.next();
					it.remove();
					
					if(key.isAcceptable()){
						ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
						SocketChannel socketChannel = ssc.accept();
						System.out.println("接收到客户端连接，来自："
							+socketChannel.socket().getInetAddress()+":"
							+socketChannel.socket().getPort());
						socketChannel.configureBlocking(false);
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						socketChannel.register(selector,
							SelectionKey.OP_READ | SelectionKey.OP_WRITE,buffer);
					}
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
						key.channel().close();
					}
				}
			}
		}
	}
	
	public void send(SelectionKey key) throws IOException{
		ByteBuffer buffer = (ByteBuffer)key.attachment();
		if(buffer!=null){
			SocketChannel socketChannel = (SocketChannel)key.channel();
			buffer.flip();
			String data = decode(buffer);
			if(data.indexOf("\r\n")==-1){
				return;
			}
			String outputData = data.substring(0,data.indexOf("\n")+1);
			System.out.println(outputData);
			ByteBuffer outputBuffer = encode("echo:"+outputData);
			while(outputBuffer.hasRemaining()){
				socketChannel.write(outputBuffer);
			}
			//清除buffer的已发出数据
			ByteBuffer temp = encode(outputData);
			buffer.position(temp.limit());
			buffer.compact();
			
			if(outputData.equals("bye\r\n")){
				key.cancel();
				socketChannel.close();
				System.out.println("关闭与客户的连接");
			}
		}
	}
	public void receive(SelectionKey key) throws IOException{
		ByteBuffer buffer = (ByteBuffer)key.attachment();
		SocketChannel socketChannel = (SocketChannel)key.channel();
		ByteBuffer readBuffer = ByteBuffer.allocate(32);
		socketChannel.read(readBuffer);
		readBuffer.flip();
		buffer.limit(buffer.capacity());
		buffer.put(readBuffer);
	}
	private ByteBuffer encode(String s){
		return charset.encode(s);
	}
	private String decode(ByteBuffer buffer){
		CharBuffer charBuffer= charset.decode(buffer);
		return charBuffer.toString();
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EchoServer1 server = new EchoServer1();
		server.service();
	}

}
