import org.stringtemplate.v4.ST;

public class TestZipStringTemplate {
	public static void main(String[] args) {
		int[] nums = {1,5,9,10};
		int[] nums2 = {-1,-3,-5,-7};
		String[] names = {"parrt", "tombu", "kg9s", "dmose"};
		ST array = new ST(
			"<names,nums:{name,num | name:<name>, id:<num>}; separator=\"\n\">"
		);
		array = new ST("<table>\n" +
			"$nums,nums2,names:{n1,n2,n | <tr><td>$n$</td><td>$n1$,$n2$</td></tr>}; separator=\"\n\"$\n" +
			"</table>\n",
			'$','$');
		array.add("names", names);
		array.add("nums", nums);
		array.add("nums2", nums2);
		System.out.println(array.render(30));
	}
}
