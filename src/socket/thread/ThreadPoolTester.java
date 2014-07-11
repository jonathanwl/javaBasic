package socket.thread;

public class ThreadPoolTester {

	public static void main(String[] args) {
		ThreadPool threadPool = new ThreadPool(3);
		for(int i=0;i<5;i++){
			threadPool.execute(createTask(i));
		}
		threadPool.close();
	}
	private static Runnable createTask(final int taskID){
		return new Runnable(){
			@Override
			public void run() {
				System.out.println("Task" +taskID+":start");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Task" +taskID+":end");
			}
			
		};
	}
}
