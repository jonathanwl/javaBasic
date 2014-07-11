package socket.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

class Target{
	InetSocketAddress address;
	SocketChannel channel;
	Exception failure;
	long connectStart;
	long connectFinish = 0;
	boolean shown = false; //该任务是否已经打印
	Target(String host){
		try{
			address = new InetSocketAddress(InetAddress.getByName(host),80);
		}catch(IOException e){
			failure = e;
		}
	}
	void show(){		
		String result;
		if(connectFinish!=0){
			result = Long.toString(connectFinish-connectStart);
		}else if(failure!=null){
			result = failure.toString();
		}else{
			result = "time out";
		}
		System.out.println(address+":"+result);
		shown = true;
	}
}

public class PingClient {
	private Selector selector;
	//存放用户新提交任务
	private LinkedList targets = new LinkedList();
	//存放已经完成的需要打印的任务
	private LinkedList finishedTargets = new LinkedList();
	
	public PingClient() throws IOException{
		selector = Selector.open();
		Connector connector = new Connector();
		Printer printer = new Printer();
		connector.start();  //启动连接线程
		printer.start();	//启动打印线程
		receiveTarget();	//主线程接收用户从控制台输入主机名，然后提交target
	}
	//向target队列加入一个任务，主线程会调用该方法
	public void addTarget(Target target){
		SocketChannel socketChannel = null;
		try{
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(target.address);
			
			target.channel = socketChannel;
			target.connectStart = System.currentTimeMillis();
			synchronized(targets){
				targets.add(target);
			}
			selector.wakeup();
		}catch(Exception e){
			if(socketChannel!=null){
				try {
					socketChannel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			target.failure=e;
			addFinishedTarget(target);
		}
	}
	//向finishedtargets队列加入一个任务，主线程和Connector线程会调用该方法
	public void addFinishedTarget(Target target){
		synchronized(finishedTargets){
			finishedTargets.notify();
			finishedTargets.add(target);
		}
	}
	boolean shutDown = false;//用于控制Connector线程
	public class Printer extends Thread{
		public Printer(){
			setDaemon(true);
		}
		public void run(){
			printFinishedTargets();
		}
	}
	//打印finishedTargets队列中的任务，Printor会调用该方法
	public void printFinishedTargets(){
		try{
			for(;;){
				Target target= null;
				synchronized(finishedTargets){
					while(finishedTargets.size()==0){
						finishedTargets.wait();
					}
					target = (Target)finishedTargets.removeFirst();
				}
				target.show();
			}
		}catch(InterruptedException e){
			return;
		}
	}
	//接收客户输入的任务，向targets队列中加入任务，主线程会调用该方法
	public void receiveTarget(){
		try{
			BufferedReader localReader = new BufferedReader(
					new InputStreamReader(System.in));
			String msg = null;
			while((msg=localReader.readLine())!=null){
				if(!msg.equals("bye")){
					Target target = new Target(msg);
					addTarget(target);
				}else{
					shutDown=true;
					selector.wakeup();
					break;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//处理连接就绪事件，Connector线程调用
	public void processSelectionKeys(){
		for(Iterator<SelectionKey> it=selector.selectedKeys().iterator();it.hasNext();){
			SelectionKey selectionKey = it.next();
			it.remove();
			
			Target target = (Target)selectionKey.attachment();
			SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
			
			try {
				if(socketChannel.finishConnect()){
					selectionKey.cancel();
					target.connectFinish = System.currentTimeMillis();
					socketChannel.close();
					addFinishedTarget(target);
				}
			} catch (IOException e) {
				try {
					socketChannel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				target.failure = e;
				addFinishedTarget(target);
				e.printStackTrace();
			}
		}
	}
	//取出targets队列中的任务，向Selector注册连接就绪事件，Connector会调用此方法
	public void registerTargets(){
		synchronized(targets){
			while(targets.size()>0){
				Target target = (Target)targets .removeFirst();
				try {
					target.channel.register(selector, SelectionKey.OP_CONNECT,target);
				} catch (ClosedChannelException e) {
					try {
						target.channel.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					target.failure = e;
					addFinishedTarget(target);
					e.printStackTrace();
				}
			}
			
		}
	}
	public class Connector extends Thread{
		public void run(){
			while(!shutDown){
				try{
					registerTargets();
					if(selector.select()>0){
						processSelectionKeys();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws IOException {
		new PingClient();

	}

}
