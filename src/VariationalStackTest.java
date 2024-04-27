
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.uwm.cs351.BufferedStack;
import edu.uwm.cs351.BufferedStack.Spy;
import edu.uwm.cs351.ChoiceOfStacks;
import edu.uwm.cs351.ChoiceOfStacks.Spy1;
import edu.uwm.cs351.FeatureExpr;
import edu.uwm.cs351.SimpleConditional;
import edu.uwm.cs351.SimpleFeatureExpr;
import edu.uwm.cs351.StackOfChoices;
import edu.uwm.cs351.StackOfChoices.Spy2;
import edu.uwm.cs351.VariationalStack;

public class VariationalStackTest {

	@Nested
	class BufferedStackTests {

		private VariationalStack<String> baseStack;
		private BufferedStack<String> bufferedStack;
		private Spy<String> spy;
		FeatureExpr ctxTrue = new SimpleFeatureExpr(true);
		FeatureExpr ctxFalse = new SimpleFeatureExpr(false);

		@BeforeEach
		void setUp() {
			baseStack = new StackOfChoices<>();
			bufferedStack = new BufferedStack<>(baseStack);
			spy = new BufferedStack.Spy<String>();
			ctxTrue = new SimpleFeatureExpr(true);
			ctxFalse = new SimpleFeatureExpr(false);
		}

		@Test
		public void TestA00() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Hello"));
			assertEquals("Hello", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA01() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Hello"));
			bufferedStack.push(ctxFalse, new SimpleConditional<>("World"));
			assertEquals("World", bufferedStack.pop(ctxFalse).getValue(ctxFalse));
			assertEquals("Hello", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA02() {
			assertThrows(IllegalStateException.class, () -> bufferedStack.pop(ctxTrue));
		}

		@Test
		public void TestA03() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Hello"));
			bufferedStack.push(ctxTrue, new SimpleConditional<>("World"));
			assertEquals(2, spy.getBuffer(bufferedStack).size());
			assertEquals("World", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
			assertEquals("Hello", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA04() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Hello"));
			bufferedStack.push(ctxTrue, new SimpleConditional<>("World"));
			bufferedStack.push(ctxFalse, new SimpleConditional<>("Different"));
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Back"));
			spy.flushBuffer(bufferedStack);
			assertEquals("Back", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA05() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Temporary"));
			spy.flushBuffer(bufferedStack);
			bufferedStack.pop(ctxTrue);
			assertThrows(IllegalStateException.class, () -> bufferedStack.pop(ctxTrue));
		}

		@Test
		public void TestA06() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("One"));
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Two"));
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Three"));
			assertEquals(3, spy.getBuffer(bufferedStack).size());
		}

		@Test
		public void TestA07() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("First"));
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Second"));
			assertEquals("Second", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
			assertEquals("First", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA08() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("First"));
			bufferedStack.push(ctxFalse, new SimpleConditional<>("Second"));
			assertEquals(1, spy.getBuffer(bufferedStack).size());
		}

		@Test
		public void TestA09() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("True Item"));
			bufferedStack.push(ctxFalse, new SimpleConditional<>("False Item"));
			assertEquals("False Item", bufferedStack.pop(ctxFalse).getValue(ctxFalse));
			assertEquals("True Item", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA10() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Item"));
			spy.flushBuffer(bufferedStack);
			bufferedStack.push(ctxTrue, new SimpleConditional<>("NewItem"));
			assertEquals("NewItem", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}

		@Test
		public void TestA11() {
			bufferedStack.push(ctxTrue, new SimpleConditional<>("Item"));
			bufferedStack.pop(ctxTrue);
			bufferedStack.push(ctxTrue, new SimpleConditional<>("NewItem"));
			assertEquals("NewItem", bufferedStack.pop(ctxTrue).getValue(ctxTrue));
		}
	}

	@Nested
	class ChoiceOfStacksTests {
		private VariationalStack<String> stack;
		private FeatureExpr trueExpr;
		private FeatureExpr falseExpr;
		private Spy1<String> spy;

		@BeforeEach
		void setUp() {
			stack = new ChoiceOfStacks<>();
			spy = new Spy1<>();
			trueExpr = new SimpleFeatureExpr(true);
			falseExpr = new SimpleFeatureExpr(false);
		}

		@Test
		public void TestB00() {
			stack.push(trueExpr, new SimpleConditional<>("Hello"));
			assertEquals("Hello", stack.pop(trueExpr).getValue(trueExpr));
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, trueExpr));
		}

		@Test
		public void TestB01() {
			assertThrows(IllegalStateException.class, () -> stack.pop(trueExpr));
		}

		@Test
		public void TestB02() {
			stack.push(trueExpr, new SimpleConditional<>("Hello"));
			stack.push(trueExpr, new SimpleConditional<>("World"));
			assertEquals("World", stack.pop(trueExpr).getValue(trueExpr));
			assertEquals("Hello", stack.pop(trueExpr).getValue(trueExpr));
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, trueExpr));
		}

		@Test
		public void TestB03() {
			stack.push(trueExpr, new SimpleConditional<>("Hello"));
			assertThrows(IllegalStateException.class, () -> stack.pop(falseExpr));
		}

		@Test
		public void TestB04() {
			stack.push(trueExpr, new SimpleConditional<>("True"));
			stack.push(falseExpr, new SimpleConditional<>("False"));
			assertEquals("False", stack.pop(falseExpr).getValue(falseExpr));
			assertEquals("True", stack.pop(trueExpr).getValue(trueExpr));
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, trueExpr));
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, falseExpr));
		}

		@Test
		public void TestB05() {
			stack.push(trueExpr, new SimpleConditional<>("One"));
			stack.push(trueExpr, new SimpleConditional<>("Two"));
			stack.pop(trueExpr);
			stack.pop(trueExpr);
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, trueExpr));
			assertThrows(IllegalStateException.class, () -> stack.pop(trueExpr));
		}

		@Test
		public void TestB06() {
			stack.push(trueExpr, new SimpleConditional<>("First"));
			stack.push(falseExpr, new SimpleConditional<>("Second"));
			assertEquals("Second", stack.pop(falseExpr).getValue(falseExpr));
			assertEquals("First", stack.pop(trueExpr).getValue(trueExpr));
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, trueExpr));
			assertTrue(spy.isStackEmpty((ChoiceOfStacks<String>) stack, falseExpr));
		}

		@Test
		public void TestB07() {
			stack.push(trueExpr, new SimpleConditional<>("First"));
			stack.push(falseExpr, new SimpleConditional<>("Second"));
			stack.push(trueExpr, new SimpleConditional<>("Third"));
			assertEquals("Third", stack.pop(trueExpr).getValue(trueExpr));
			assertEquals("Second", stack.pop(falseExpr).getValue(falseExpr));
			assertFalse(spy.isStackEmpty((ChoiceOfStacks<String>) stack, trueExpr));
		}

		@Test
		public void TestB08() {
			stack.push(trueExpr, new SimpleConditional<>("Hello"));
			stack.pop(trueExpr);
			assertThrows(IllegalStateException.class, () -> stack.pop(trueExpr));
		}
	}

	@Nested
	public class StackOfChoicesTest {
		private StackOfChoices<String> stack;
		private Spy2<String> spy;

		@BeforeEach
		public void setUp() {
			stack = new StackOfChoices<>();
			spy = new Spy2<>(stack);
		}

		@Test
		public void TestC00() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Hello"));
			assertEquals("Stack size should be 1 after push", 1, spy.getStackSize());
			stack.pop(new SimpleFeatureExpr(true));
			assertTrue("Stack should be empty after pop", spy.isStackEmpty());
		}

		@Test
		public void TestC01() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("World"));
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Hello"));
			assertEquals("Stack size should be 2 before clearing", 2, spy.getStackSize());
			spy.clearStack();
			assertTrue("Stack should be empty after clear", spy.isStackEmpty());
		}

		@Test
		public void TestC02() {
			stack.push(new SimpleFeatureExpr(false), new SimpleConditional<>("Ignored"));
			assertTrue("Stack should remain empty", spy.isStackEmpty());
		}

		@Test
		public void TestC03() {
			assertThrows(IllegalStateException.class, () -> stack.pop(new SimpleFeatureExpr(true)));
		}

		@Test
		public void TestC04() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Hello"));
			assertNull("Should not pop with non-matching context",
					stack.pop(new SimpleFeatureExpr(false)).getValue(new SimpleFeatureExpr(false)));
		}

		@Test
		public void TestC05() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("ClearMe"));
			spy.clearStack();
			assertTrue("Stack should be empty after clear", spy.isStackEmpty());
		}

		@Test
		public void TestC06() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Item"));
			stack.pop(new SimpleFeatureExpr(true));
			assertEquals("Stack size should be 0 after pop", 0, spy.getStackSize());
		}

		@Test
		public void TestC07() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("One"));
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Two"));
			stack.pop(new SimpleFeatureExpr(true));
			assertEquals("Should return 'One'", "One",
					stack.pop(new SimpleFeatureExpr(true)).getValue(new SimpleFeatureExpr(true)));
		}

		@Test
		public void TestC08() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("First"));
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Second"));
			assertEquals("Should pop 'Second'", "Second",
					stack.pop(new SimpleFeatureExpr(true)).getValue(new SimpleFeatureExpr(true)));
			assertEquals("Should pop 'First'", "First",
					stack.pop(new SimpleFeatureExpr(true)).getValue(new SimpleFeatureExpr(true)));
		}

	}

}
