package jvm.io.file;

import java.util.Collection;
/**
 * 把集合转换成字符串
 * @author jonathan
 *
 */
public class PPrint {
	public static String pformat(Collection<?> c){
		if(c.size() == 0) return "[]";
		StringBuilder result = new StringBuilder("[");
		for (Object elem : c){
			if(c.size() != 1){
				result.append("\n ");
			}
			result.append(elem);
		}
		if(c.size() != 1){
			result.append("\n ");
		}
		result.append("] ");
		return result.toString();
	}
	public static void pprint(Collection<?> c){
		
	}

}
