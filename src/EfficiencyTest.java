import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class EfficiencyTest {

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
		void TestD00() {
			int count = 100000;
			for (int i = 0; i < count; i++) {
				bufferedStack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			assertEquals(count, spy.getBuffer(bufferedStack).size());
		}

		@Test
		void TestD01() {
			int count = 100000;
			for (int i = 0; i < count; i++) {
				bufferedStack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			for (int i = 0; i < count; i++) {
				bufferedStack.pop(ctxTrue);
			}
			assertTrue(spy.getBuffer(bufferedStack).isEmpty());
		}

		@Test
		void TestD02() {
			int switches = 5000;
			for (int i = 0; i < switches; i++) {
				bufferedStack.push(ctxTrue, new SimpleConditional<>("True " + i));
				bufferedStack.flushBuffer();
				bufferedStack.push(ctxFalse, new SimpleConditional<>("False " + i));
				bufferedStack.flushBuffer();
			}
			assertTrue(spy.getBuffer(bufferedStack).isEmpty(), "Buffer should be empty after manual flushes");
		}

		@Test
		void TestD03() {
			int elements = 100000;
			for (int i = 0; i < elements; i++) {
				bufferedStack.push(ctxTrue, new SimpleConditional<>("Element " + i));
				spy.flushBuffer(bufferedStack);
			}

			assertTrue(spy.getBuffer(bufferedStack).isEmpty());
		}

		@Test
		void TestD04() {
			int operations = 10000;
			for (int i = 0; i < operations; i++) {
				assertThrows(IllegalStateException.class, () -> bufferedStack.pop(ctxTrue));
			}
		}

		@Test
		void TestD05() {
			int numOperations = 10000;
			for (int i = 0; i < numOperations; i++) {
				FeatureExpr randomCtx = new SimpleFeatureExpr((i % 2) == 0);
				bufferedStack.push(randomCtx, new SimpleConditional<>("Data " + i));
				bufferedStack.pop(randomCtx);
			}
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
		void TestE00() {
			int numElements = 100000;
			for (int i = 0; i < numElements; i++) {
				stack.push(trueExpr, new SimpleConditional<>("Data " + i));
			}
		}

		@Test
		void TestE01() {
			int numElements = 100000;
			for (int i = 0; i < numElements; i++) {
				stack.push(trueExpr, new SimpleConditional<>("Data " + i));
			}
			for (int i = 0; i < numElements; i++) {
				stack.pop(trueExpr);
			}
		}

		@Test
		void TestE02() {
			int numElements = 50000;
			for (int i = 0; i < numElements; i++) {
				stack.push(trueExpr, new SimpleConditional<>("True Data " + i));
				stack.push(falseExpr, new SimpleConditional<>("False Data " + i));
			}
			for (int i = 0; i < numElements; i++) {
				stack.pop(trueExpr);
				stack.pop(falseExpr);
			}
		}

		@Test
		void TestE03() {
			int numSwitches = 10000;
			for (int i = 0; i < numSwitches; i++) {
				FeatureExpr context = new SimpleFeatureExpr(i % 2 == 0);
				stack.push(context, new SimpleConditional<>("Value " + i));
				stack.pop(context);
			}
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
		void TestF00() {
			FeatureExpr ctxTrue = new SimpleFeatureExpr(true);
			int numElements = 100000;
			for (int i = 0; i < numElements; i++) {
				stack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			for (int i = 0; i < numElements; i++) {
				stack.pop(ctxTrue);
			}
			assertTrue(spy.isStackEmpty(), "Stack should be empty after all operations");
		}

		@Test
		void TestF01() {
			FeatureExpr ctxTrue = new SimpleFeatureExpr(true);
			int numElements = 50000;
			for (int i = 0; i < numElements; i++) {
				stack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			assertEquals(numElements, spy.getStackSize(), "Stack size should match the number of pushed elements");
		}

		@Test
		void TestF02() {
			FeatureExpr ctxTrue = new SimpleFeatureExpr(true);
			int numElements = 50000;
			for (int i = 0; i < numElements; i++) {
				stack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			for (int i = 0; i < numElements; i++) {
				stack.pop(ctxTrue);
			}
			assertTrue(spy.isStackEmpty(), "Stack should be empty after popping all elements");
		}

		@Test
		void TestF03() {
			FeatureExpr ctxTrue = new SimpleFeatureExpr(true);
			int capacity = 50000;
			for (int i = 0; i < capacity; i++) {
				stack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			for (int i = 0; i < capacity - 100; i++) {
				stack.pop(ctxTrue);
			}
			int remainingOps = 10000;
			for (int i = 0; i < remainingOps; i++) {
				stack.push(ctxTrue, new SimpleConditional<>("Additional " + i));
				stack.pop(ctxTrue);
			}
			assertEquals(100, spy.getStackSize(), "Stack should have elements equal to remaining initial pushes");
		}

		@Test
		void TestF04() {
			FeatureExpr ctxTrue = new SimpleFeatureExpr(true);
			int numOperations = 1000000;
			for (int i = 0; i < numOperations; i++) {
				stack.push(ctxTrue, new SimpleConditional<>("Element " + i));
			}
			for (int i = 0; i < numOperations; i++) {
				stack.pop(ctxTrue);
			}
			assertTrue(spy.isStackEmpty(), "Stack should be empty after extensive use");
		}

	}
}