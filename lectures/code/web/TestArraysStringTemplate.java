import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

public class TestArraysStringTemplate {
	public static void main(String[] args) {
		int[] nums =  {3,9,20,2,1,4,6,32,5,6,77,885};
//		ST array = new ST("int <name>[] = { <data; wrap, anchor, separator=\", \"> };");
		ST array = new ST(
			"int <name>[] = {<data:{d |  [<d>]}> }"
		);
		array.add("name", "a");
		array.add("data", nums);
		System.out.println(array.render());
	}
}

