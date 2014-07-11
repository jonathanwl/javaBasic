package util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Map;

public class SecurityTest {

	public static void main(String [] args){
//		for(Provider p : Security.getProviders()){
//			System.out.println("提供者="+p);
//			for(Map.Entry<Object, Object> entry : p.entrySet()){
//				System.out.println("键值="+entry.getKey());
//			}
//		}
		byte[] a = new String("111111").getBytes();
		try {
			System.out.println(encodeMD4(a));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	/**
	 * md4加密
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encodeMD4(byte[] data) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD4");
		md.update(data);
		return md.digest();
	}
}
