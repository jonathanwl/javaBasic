package jvm.io;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载远程文件，断点续传，多线程
 * @author jonathan
 * 
 */
public class GetRemoteFile1 implements Runnable{

	public GetRemoteFile1(){
		
	}
	@Override
	public void run() {
		URL url = null;
		HttpURLConnection urlc = null;
		DataOutputStream dos = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		GetRemoteFile1 gco = new GetRemoteFile1();
		long fileSize = 0;
		long start = System.currentTimeMillis();
		int len = 0;
		byte[] bt = new byte[1024];
		// byte[] buffer=new byte[50*1024];
		RandomAccessFile raFile = null;
		long TotalSize = 0; // 要下载的文件总大小
		try {
			url = new URL("http://downloads.jasig.org/cas/cas-server-4.0.0-release.zip");
			String localFile = getName(url); // 文件保存的地方及文件名，具体情况可以改
			String localFile_bak ="d:////a.tp "; // 未下载完文件加.tp扩展名，以便于区别
			urlc = (HttpURLConnection) url.openConnection();
//			TotalSize = Long.parseLong(urlc.getHeaderField(" Content-Length "));
//			System.out.println(" 下载文件大小为: " + TotalSize);
			urlc.disconnect(); // 先断开，下面再连接，否则下面会报已经连接的错误
			urlc = (HttpURLConnection) url.openConnection();
			// 确定文件是否存在
			if (gco.isExist(localFile_bak)){ // 采用断点续传，这里的依据是看下载文件是否在本地有.tp有扩展名同名文件
			
				System.out.println(" 文件续传中… ");
				fileSize = gco.fileSize(localFile_bak); // 取得文件在小，以便确定随机写入的位置
				System.out.println(" fileSize: " + fileSize);
				// 设置User-Agent
				// urlc.setRequestProperty("User-Agent","NetFox");
				// 设置断点续传的开始位置
				urlc.setRequestProperty("RANGE", "bytes=" + fileSize
						+ " - ");
				// urlc.setRequestProperty("RANGE", "bytes="+fileSize); //
				// 这样写不行，不能少了这个"-".
				// 设置接受信息
				urlc.setRequestProperty("Accept"," image/gif,image/x-xbitmap,application/msword,*/* ");
				raFile = new RandomAccessFile(localFile_bak, "rw"); // 随机方位读取
				raFile.seek(fileSize); // 定位指针到fileSize位置
				bis = new BufferedInputStream(urlc.getInputStream());
				while ((len = bis.read(bt)) > 0) { // 循环获取文件
					raFile.write(bt, 0, len);
					// buffer=buffer+bt;
					// System.
				}
				System.out.println(" 文件续传接收完毕！ ");
			} else { // 采用原始下载

				fos = new FileOutputStream(localFile_bak); // 没有下载完毕就将文件的扩展名命名.bak
				dos = new DataOutputStream(fos);
				bis = new BufferedInputStream(urlc.getInputStream());
				System.out.println(" 正在接收文件… ");
				int test = 0;
				while ((len = bis.read(bt)) > 0) { // 循环获取文件

					dos.write(bt, 0, len);
					test++;
					if (test == 50) // 这里是测试，你可以删除这里，就可以正常下载了
						break;
				}
				// System.out.println("文件正常接收完毕！");
			}
			System.out.println(" 共用时： " + (System.currentTimeMillis() - start)/ 1000);
			System.out.println(" localFile_bak: " + gco.fileSize(localFile_bak));
			if (gco.fileSize(localFile_bak) == TotalSize) {// 下载完毕后，将文件重命名
				gco.rename(localFile_bak, localFile);
			}
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (bis != null)
					bis.close();
				if (dos != null)
					dos.close();
				if (fos != null)
					fos.close();
				if (raFile != null)
					raFile.close();
			} catch (IOException f) {
				f.printStackTrace();
			}
		}
	}
	/**
	 * 判断文件是否存在
	 * 
	 * @param filepath
	 *            文件路径
	 * @return
	 */
	private boolean isExist(String filepath) {
		return new File(filepath).exists();
	}

	/**
	 * 获得文件大小
	 * 
	 * @param filepath
	 *            文件路径
	 * @return
	 */
	private long fileSize(String filepath) {
		return new File(filepath).length();
	}

	/**
	 * 重命名文件
	 * 
	 * @param filepath
	 * @param name
	 * @return
	 */
	private boolean rename(String filepath, String name) {
		File f = new File(filepath);
		return f.renameTo(new File(name));
	}

	 private String getName(URL url){
		 return url.getFile();
	 }
	public static void main(String[] args) {
		

	
	}
}
