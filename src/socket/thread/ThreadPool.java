package socket.thread;

import java.util.LinkedList;

public class ThreadPool extends ThreadGroup{

	private boolean isClosed = false; //线程池是否关闭
	private LinkedList<Runnable> workQueue; //表示工作队列
	private static int threadPoolID; //表示线程池id
	private int threadID; //表示工作线程id
	
	public ThreadPool(int poolSize) {
		super("ThreadPool-"+(threadPoolID++));
		setDaemon(true);
		workQueue = new LinkedList<Runnable>(); //创件工作队列
		for(int i=0;i<poolSize;i++){
			new WorkThread().start();
		}
	}

	/**向工作队列中加入一个新任务，由工作线程去执行任务*/
	public synchronized void execute(Runnable task){
		if(isClosed){
			throw new IllegalStateException();
		}
		if(task != null){
			workQueue.add(task);
			notify();
		}
	}
	
	protected synchronized Runnable getTask() throws InterruptedException{
		while(workQueue.size() == 0){
			if(isClosed){
				return null;
			}
			wait();
		}
		return workQueue.removeFirst();
	}
	
	public synchronized void close(){
		if(!isClosed){
			isClosed = true;
			workQueue.clear();
			interrupt();
		}
	}
	
	 /** 等待工作线程把所有任务执行完毕*/  
    public void join() {  
        synchronized (this) {  
            isClosed = true;  
            notifyAll();            //唤醒所有还在getTask()方法中等待任务的工作线程  
        }  
        Thread[] threads = new Thread[activeCount()]; //activeCount() 返回该线程组中活动线程的估计值。  
        int count = enumerate(threads); //enumerate()方法继承自ThreadGroup类，根据活动线程的估计值获得线程组中当前所有活动的工作线程  
        for(int i =0; i < count; i++) { //等待所有工作线程结束  
            try {  
                threads[i].join();  //等待工作线程结束  
            }catch(InterruptedException ex) {  
                ex.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 内部类,工作线程,负责从工作队列中取出任务,并执行 
     * @author sunnylocus 
     */  
    private class WorkThread extends Thread {  
        public WorkThread() {  
            //父类构造方法,将线程加入到当前ThreadPool线程组中  
            super(ThreadPool.this,"workThread-"+(threadID++));  
        }  
        public void run() {  
            while(! isInterrupted()) {  //isInterrupted()方法继承自Thread类，判断线程是否被中断  
                Runnable task = null;  
                try {  
                    task = getTask();     //取出任务  
                }catch(InterruptedException ex) {  
                    ex.printStackTrace();  
                }  
                //如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程  
                if(task == null) return;  
                  
                try {  
                    task.run();  //运行任务  
                }catch(Throwable t) {  
                    t.printStackTrace();  
                }  
            }//  end while  
        }//  end run  
    }// end workThread  
}  
