import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import edu.uwm.cs351.BufferedStack;
import edu.uwm.cs351.ChoiceOfStacks;
import edu.uwm.cs351.SimpleConditional;
import edu.uwm.cs351.SimpleFeatureExpr;
import edu.uwm.cs351.StackOfChoices;

public class TestInvariant {

	@Nested
	class BufferedStackTests {
		private BufferedStack<String> stack;
		private BufferedStack.Spy<String> spy;

		@BeforeEach
		void setUp() {
			stack = new BufferedStack<>(new StackOfChoices<>());
			spy = new BufferedStack.Spy<>();
		}

		@Test
		void testWellFormedInitially() {
			assertTrue(spy.wellFormed(stack), "BufferedStack should be well-formed initially.");
		}

		@Test
		void testWellFormedAfterOperations() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Hello"));
			assertTrue(spy.wellFormed(stack), "BufferedStack should be well-formed after operations.");
		}

		@Test
		void testWellFormedWithEmptyBuffer() {
			stack.flushBuffer();
			assertTrue(spy.wellFormed(stack), "BufferedStack should be well-formed with an empty buffer.");
		}

		@Test
		void testWellFormedAfterFlush() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Flush Me"));
			stack.flushBuffer();
			assertTrue(spy.wellFormed(stack), "BufferedStack should remain well-formed after flush.");
		}

		@Test
		void testInvariantAfterMultipleContexts() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("First"));
			stack.push(new SimpleFeatureExpr(false), new SimpleConditional<>("Second"));
			assertTrue(spy.wellFormed(stack), "BufferedStack should handle multiple contexts correctly.");
		}

		@Test
		void testInvariantWithNullBuffer() {
			try {
				java.lang.reflect.Field bufferField = BufferedStack.class.getDeclaredField("buffer");
				bufferField.setAccessible(true);
				bufferField.set(stack, null);
			} catch (ReflectiveOperationException e) {
				fail("Reflection operation failed: " + e.getMessage());
			}
			assertFalse(spy.wellFormed(stack), "BufferedStack should not be well-formed with a null buffer.");
		}

		@Test
		void testInvariantWithCorruptedConditional() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Valid"));
			stack.buffer.add(null);
			assertFalse(spy.wellFormed(stack),
					"BufferedStack should not be well-formed with corrupted conditionals in the buffer.");
		}
	}

	@Nested
	class ChoiceOfStacksTests {
		private ChoiceOfStacks<String> stack;
		private ChoiceOfStacks.Spy1<String> spy;

		@BeforeEach
		void setUp() {
			stack = new ChoiceOfStacks<>();
			spy = new ChoiceOfStacks.Spy1<>();
		}

		@Test
		void testWellFormedInitially() {
			assertTrue(spy.wellFormed(stack), "ChoiceOfStacks should be well-formed initially.");
		}

		@Test
		void testWellFormedAfterOperations() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("World"));
			assertTrue(spy.wellFormed(stack), "ChoiceOfStacks should be well-formed after push.");
		}

		@Test
		void testWellFormedWithMultipleContexts() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("True Data"));
			stack.push(new SimpleFeatureExpr(false), new SimpleConditional<>("False Data"));
			assertTrue(spy.wellFormed(stack), "ChoiceOfStacks should be well-formed with multiple contexts.");
		}

		@Test
		void testHandlingEmptyPop() {
			assertThrows(IllegalStateException.class, () -> stack.pop(new SimpleFeatureExpr(true)),
					"Attempting to pop from an empty stack should raise an exception.");
		}

		@Test
		void testInvariantWithInvalidConditionalMapping() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Valid"));
			stack.stacks.put(new SimpleFeatureExpr(true), null);
			assertFalse(spy.wellFormed(stack),
					"ChoiceOfStacks should not be well-formed with null mappings in stacks.");
		}

	}

	@Nested
	class StackOfChoicesTests {
		private StackOfChoices<String> stack;
		private StackOfChoices.Spy2<String> spy;

		@BeforeEach
		void setUp() {
			stack = new StackOfChoices<>();
			spy = new StackOfChoices.Spy2<>(stack);
		}

		@Test
		void testWellFormedInitially() {
			assertTrue(spy.wellFormed(stack), "StackOfChoices should be well-formed initially.");
		}

		@Test
		void testWellFormedAfterOperations() {
			stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("Sample"));
			assertTrue(spy.wellFormed(stack), "StackOfChoices should be well-formed after operations.");
		}

		@Test
		void testWellFormedUnderHighLoad() {
			for (int i = 0; i < 1000; i++) {
				stack.push(new SimpleFeatureExpr(true), new SimpleConditional<>("High Load " + i));
			}
			assertTrue(spy.wellFormed(stack), "StackOfChoices should remain well-formed under high load.");
		}

		@Test
		void testWellFormedAfterRandomizedContextPushes() {
			for (int i = 0; i < 500; i++) {
				stack.push(new SimpleFeatureExpr(i % 2 == 0), new SimpleConditional<>("Data " + i));
			}
			assertTrue(spy.wellFormed(stack), "StackOfChoices should handle randomized context pushes correctly.");
		}

		@Test
		void testInvariantViolationWithNullElements() {
			stack.push(new SimpleFeatureExpr(true), null);
			assertFalse(spy.wellFormed(stack), "StackOfChoices should not be well-formed with null elements.");
		}

		@Test
		void testInvariantWithCorruptedStack() {
			stack.push(new SimpleFeatureExpr(true), null);
			assertFalse(spy.wellFormed(stack), "StackOfChoices should not be well-formed with corrupted elements.");
		}

		@Test
		void testInvariantWithEmptyConditional() {
			stack.stack.push(null);
			assertFalse(spy.wellFormed(stack),
					"StackOfChoices should not be well-formed if it contains null conditionals.");
		}

	}
}