package edu.uwm.cs351;

import java.util.Stack;

public class StackOfChoices<T> implements VariationalStack<T> {
	public Stack<Conditional<T>> stack;

	public StackOfChoices() {
		this.stack = new Stack<>();
	}

//    @Override
//    public void push(FeatureExpr ctx, Conditional<T> value) {
//        // Wrap the value with a new Conditional that respects the context
//        stack.push(new Conditional<T>() {
//            @Override
//            public T getValue(FeatureExpr context) {
//                // Check if the context for the push operation matches the pop context
//                if (context.evaluate()) {
//                    return value.getValue(context);
//                }
//                return null;
//            }
//        });
//    }

	private boolean wellFormed() {
		return stack != null && stack.stream().allMatch(cond -> cond != null);
	}

	@Override
	public void push(FeatureExpr ctx, Conditional<T> value) {
		if (ctx.evaluate()) {
			stack.push(value);
		}
	}

	@Override
	public Conditional<T> pop(FeatureExpr ctx) {
		if (stack.isEmpty()) {
			throw new IllegalStateException("Stack is empty");
		}
		Conditional<T> conditional = stack.pop();
		return new Conditional<T>() {
			@Override
			public T getValue(FeatureExpr context) {
				if (ctx.evaluate() && context.evaluate()) {
					return conditional.getValue(context);
				}
				return null;
			}
		};
	}

	public static class Spy2<T> {
		private StackOfChoices<T> stackOfChoices;

		public Spy2(StackOfChoices<T> stackOfChoices) {
			this.stackOfChoices = stackOfChoices;
		}

		/**
		 * Get the current size of the internal stack.
		 * 
		 * @return the number of items in the stack.
		 */
		public int getStackSize() {
			return stackOfChoices.stack.size();
		}

		/**
		 * Peek at the top element of the stack without removing it.
		 * 
		 * @return the top Conditional<T> if the stack is not empty, null otherwise.
		 */
		public Conditional<T> peekStack() {
			return stackOfChoices.stack.isEmpty() ? null : stackOfChoices.stack.peek();
		}

		/**
		 * Checks if the internal stack is empty.
		 * 
		 * @return true if the stack is empty, false otherwise.
		 */
		public boolean isStackEmpty() {
			return stackOfChoices.stack.isEmpty();
		}

		/**
		 * Clears the internal stack of all elements.
		 */
		public void clearStack() {
			stackOfChoices.stack.clear();
		}

		/**
		 * Check the invariant on the given dynamic array robot.
		 * 
		 * @param r ranking to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(StackOfChoices<?> r) {
			return r.wellFormed();
		}
	}

}
