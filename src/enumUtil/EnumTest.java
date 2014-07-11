package enumUtil;
import java.util.*;
import java.text.*;
public enum EnumTest {
	  DATE_TIME {
		  //实现抽象方法
	    String getInfo() {
	      return
	        DateFormat.getDateInstance().format(new Date());
	    }
	  },
	  CLASSPATH {
	    String getInfo() {
	      return System.getenv("CLASSPATH");
	    }
	  },
	  VERSION {
	    String getInfo() {
	      return System.getProperty("java.version");
	    }
	  };
	  //定义一个抽象方法
	  abstract String getInfo();
	  public static void main(String[] args) {
		System.out.println(EnumTest.valueOf("VERSION"));
	    for(EnumTest csm : values())
	      System.out.println(csm.getInfo());
	  }
}
