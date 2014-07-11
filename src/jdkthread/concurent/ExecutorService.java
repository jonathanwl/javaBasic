package jdkthread.concurent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ExecutorService extends Executor{
	/**
	 * 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。 
	 */
	void shutDown();
	/**
	 * 试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
	 * @return
	 */
	List<Runnable> shutDownNow();
	/**
	 * 如果此执行程序已关闭，则返回 true。 
	 * @return
	 */
	boolean isShutDown();
	/**
	 * 如果关闭后所有任务都已完成，则返回 true。
	 * 注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
	 * @return
	 */
	boolean isTerminated();
	/**
	 * 请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，
	 * 都将导致阻塞，直到所有任务完成执行。 
	 * 参数：
	 *	 timeout - 最长等待时间
	 *	 unit - timeout 参数的时间单位 
	 * 返回：
	 *   如果此执行程序终止，则返回 true；如果终止前超时期满，则返回 false 
	 * 抛出： 
	 *	InterruptedException - 如果等待时发生中断
	 * @return
	 */
	boolean awaitTerminated(long timeout,TimeUnit unit) throws InterruptedException;
}
