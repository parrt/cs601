package cs601.collections;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMapSimple {
	@Test
	public void testAssignmentExample1(){
		DoubleKeyMap<String,Integer,Double> m =
			new DoubleKeyHashMap<String, Integer, Double>();
		m.put("hi",32,99.2);
		double d = m.get("hi",32);
		assertEquals(d,99.2,0.001);
	}
}
