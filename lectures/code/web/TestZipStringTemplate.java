import org.stringtemplate.v4.ST;

public class TestZipStringTemplate {
	public static void main(String[] args) {
		int[] nums = {1,5,9,10};
		String[] names = {"parrt", "tombu", "kg9s", "dmose"};
		ST array = new ST(
			"<names,nums:{name,num | <name>:<num>}>"
		);
		array.add("names", names);
		array.add("nums", nums);
		System.out.println(array.render(30));
	}
}
