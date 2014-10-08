import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

public class TestArraysStringTemplate {
	public static void main(String[] args) {
		int[] nums =
			new int[] {3,9,20,2,1,4,6,32,5,6,77,888,2,1,6,32,5,6,77,
					   4,9,20,2,1,4,63,9,20,2,1,4,6,32,5,6,77,6,32,5,6,77,
					   3,9,20,2,1,4,6,32,5,6,77,888,1,6,32,5};
		ST array = new ST("int <name>[] = { <data; wrap, anchor, separator=\", \"> };");
		array.add("name", "a");
		array.add("data", nums);
		System.out.println(array.render(30));
	}
}

