import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCalculator {
	Calculator calc;

	@Before  // before each test
	public void setUp() { calc = new Calculator(); }

	@After
	public void tearDown() { calc = null; }

	@Test public void testAdd() {
		assertEquals("testing add(3,4)", 7, calc.add(3,4));
	}
}
