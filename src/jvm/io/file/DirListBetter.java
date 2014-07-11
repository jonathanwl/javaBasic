package jvm.io.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;
/**
 * 列出路径的所有文件名
 * @author jonathan
 *
 */
public class DirListBetter {
	//使用匿名过滤器跟好的实现
	@SuppressWarnings("unused")
	private FilenameFilter filter(final String args){
		return new FilenameFilter(){
			private Pattern pattern = Pattern.compile(args);
			@Override
			public boolean accept(File dir,String name){
				return pattern.matcher(name).matches();	
			}
		};
	}
	
	public static void main(String[] args) {
		File path = new File(".");
		String [] list;
		if(args.length == 0){
			list = path.list();
		}else{
			list = path.list(new DirFilter(args[0]));
		}
		Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
		for(String dirItem : list){
			System.out.println(dirItem);
		}
	}

}
