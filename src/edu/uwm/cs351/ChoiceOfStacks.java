package edu.uwm.cs351;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ChoiceOfStacks<T> implements VariationalStack<T> {

	public Map<FeatureExpr, Stack<T>> stacks = new HashMap<>();

	private boolean wellFormed() {
		if (stacks == null)
			return false;
		return stacks.values().stream().allMatch(stack -> stack != null);
	}

	@Override
	public void push(FeatureExpr ctx, Conditional<T> value) {
		Stack<T> stack = stacks.computeIfAbsent(ctx, k -> new Stack<>());
		stack.push(value.getValue(ctx));
	}

	@Override
	public Conditional<T> pop(FeatureExpr ctx) {
		Stack<T> stack = stacks.get(ctx);
		if (stack == null || stack.isEmpty()) {
			throw new IllegalStateException("Stack is empty or context not found");
		}
		T value = stack.pop();
		return new Conditional<T>() {
			@Override
			public T getValue(FeatureExpr ctx) {
				return value;
			}
		};
	}

	public static class Spy1<T> {

		/**
		 * Get the stack associated with a specific context.
		 * 
		 * @param stacks the ChoiceOfStacks instance
		 * @param ctx    the FeatureExpr context
		 * @return the stack associated with the given context or null if no such stack
		 *         exists
		 */
		public Stack<T> getStack(ChoiceOfStacks<T> stacks, FeatureExpr ctx) {
			return stacks.stacks.get(ctx);
		}

		/**
		 * Get the entire map of contexts to stacks for inspection.
		 * 
		 * @param stacks the ChoiceOfStacks instance
		 * @return the map from FeatureExpr to Stack<T>
		 */
		public Map<FeatureExpr, Stack<T>> getStacksMap(ChoiceOfStacks<T> stacks) {
			return stacks.stacks;
		}

		/**
		 * Checks if the stack for a given context is empty.
		 * 
		 * @param stacks the ChoiceOfStacks instance
		 * @param ctx    the FeatureExpr context
		 * @return true if the stack is empty or does not exist, false otherwise
		 */
		public boolean isStackEmpty(ChoiceOfStacks<T> stacks, FeatureExpr ctx) {
			Stack<T> stack = stacks.stacks.get(ctx);
			return stack == null || stack.isEmpty();
		}

		/**
		 * Force clear the stack for a given context.
		 * 
		 * @param stacks the ChoiceOfStacks instance
		 * @param ctx    the FeatureExpr context
		 */
		public void clearStack(ChoiceOfStacks<T> stacks, FeatureExpr ctx) {
			Stack<T> stack = stacks.stacks.get(ctx);
			if (stack != null) {
				stack.clear();
			}
		}

		/**
		 * Check the invariant on the given dynamic array robot.
		 * 
		 * @param r ranking to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(ChoiceOfStacks<?> r) {
			return r.wellFormed();
		}
	}

}
