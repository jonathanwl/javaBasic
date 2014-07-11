package jvm.io.file;

import java.io.File;
import java.io.IOException;
/**
 * 查找特定扩展名文件（参数ext）的文件
 * 其中使用了策略模式，把抽象策略内嵌如上下文角色
 * 由客户端来实现具体的策略
 * @author jonathan
 */
public class ProcessFiles {
	public interface Strategy{
		void process(File file);
	}
	private Strategy strategy;
	private String ext;
	public ProcessFiles(Strategy strategy,String ext){
		this.strategy = strategy;
		this.ext = ext;
	}
	public void start(String[] args){
		try{
			if(args.length == 0){
				processDirectoryTree(new File("."));
			}else{
				for(String arg : args){
					File fileArg = new File(arg);
					if(fileArg.isDirectory()){
						processDirectoryTree(fileArg);
					}
					if(!arg.endsWith("."+ext)){
						arg+= "."+ext;
					}
					strategy.process(new File(arg).getCanonicalFile());
				}
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	public void processDirectoryTree(File root) throws IOException{
		for(File file: Directory.walk(root.getAbsolutePath(),
				".*\\."+ext)){
			strategy.process(file.getCanonicalFile());
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ProcessFiles(new ProcessFiles.Strategy(){
			public void process(File file){
				System.out.println(file);
			}
		},"java").start(args);
		
	}

}
