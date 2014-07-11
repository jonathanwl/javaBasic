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
public class EchoServer2 {
	private int port = 8000;
	private ServerSocketChannel serverSocketChannel = null;
	private Selector selector = null;
	private Charset charset = Charset.forName("GBK");
	
	public EchoServer2() throws IOException{ 
		//创建一个Selector对象
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		//设置关闭服务器之后保证再起动绑定相同端口
		serverSocketChannel.socket().setReuseAddress(true);
		//把服务器进程与一个本地端口绑定
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动！");
	}
	private Object gate = new Object();
	//由于service()方法中的selector.select()方法是阻塞的
	// socketChannel.registe(。。。)也是阻塞的都是对selector操作，可造成死锁
	//selector.wakeup();唤醒主线程selector.select()方法的阻塞，synchronized(gate)这个是用于
	//主线程获得accept方法对selector的操作同步锁
	public void accept(){
		for(;;){
			try{
				SocketChannel socketChannel = serverSocketChannel.accept();
				System.out.println("接收到客户端连接，来自："
						+socketChannel.socket().getInetAddress()+":"
						+socketChannel.socket().getPort());
				socketChannel.configureBlocking(false);
				
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				synchronized(gate){
					selector.wakeup();
					socketChannel.register(selector,
							SelectionKey.OP_READ | SelectionKey.OP_WRITE,buffer);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void service() throws IOException{
		for(;;){
			//空的同步代码块，让主线程等待Accept线程执行完同步代码块
			synchronized(gate){}//问题：如果执行完这段又执行了accpt阻塞方法，不还是死锁吗，应该把select()方法拿进来
			int n = selector.select();
			if(n==0){continue;}
			Set readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = readyKeys.iterator();
			while(it.hasNext()){
				SelectionKey key = null;
				try{
					key = it.next();
					it.remove();
					if(key.isAcceptable()){
						ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
						
					}
					if(key.isReadable()){
						receive(key);
					}
					if(key.isWritable()){
						send(key);
					}
				}catch(IOException e){
					
					if(key!=null){
						key.cancel();
						key.channel().close();
					}
					e.printStackTrace();
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
		final EchoServer2 server = new EchoServer2();
		Thread accept = new Thread(){
			public void run(){
				server.accept();
			}
		};
		accept.start();
		server.service();
	}

}
