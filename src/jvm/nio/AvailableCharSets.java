package jvm.nio;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;

public class AvailableCharSets {

	public static void main(String[] args) {
		SortedMap<String,Charset> charSets = Charset.availableCharsets();
		Iterator<String> it = charSets.keySet().iterator();
		while(it.hasNext()){
			String csname = it.next();
//			printnb(csname);
		}
	}

}
