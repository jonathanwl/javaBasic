package jvm;

import java.lang.reflect.Field;

import sun.misc.Unsafe;


public class DirectMemoryOOM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Field unsafeField = Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
	}

}
