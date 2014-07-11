package jvm;

class Candy{
	static { System.out.println("loading candy...");} 
}
class Gum{
	static { System.out.println("loading Gum...");} 
}
class Cookie{
	static { System.out.println("loading Cookie...");} 
}
public class Sweetshop {

	public static void main(String[] args) {
		System.out.println("begin load candy...");
		new Candy();
		System.out.println("after load candy..."); 
		try {
			Class.forName("jvm.Gum");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("after Class.forName(\"gum\")");
		new Cookie();
		System.out.println("after new Cookie");
	}

}
