package edu.uwm.cs351;

import java.util.LinkedList;
import java.util.List;

public class BufferedStack<T> implements VariationalStack<T> {
	private VariationalStack<T> underlyingStack;
	public List<Conditional<T>> buffer;
	private FeatureExpr currentContext;

	public BufferedStack(VariationalStack<T> underlyingStack) {
		this.underlyingStack = underlyingStack;
		this.buffer = new LinkedList<>();
		this.currentContext = null;
	}

	private boolean wellFormed() {
		if (underlyingStack == null || buffer == null)
			return false;
		if (currentContext != null) {
			return buffer.stream().allMatch(cond -> cond != null);
		}
		if (underlyingStack instanceof BufferedStack && !((BufferedStack<T>) underlyingStack).wellFormed()) {
			return false;
		}
		return true;
	}

	@Override
	public void push(FeatureExpr ctx, Conditional<T> value) {
		if (currentContext != null && !ctx.equals(currentContext)) {
			flushBuffer();
		}
		currentContext = ctx;
		buffer.add(value);
	}

	@Override
	public Conditional<T> pop(FeatureExpr ctx) {
		if (buffer.isEmpty() || !ctx.equals(currentContext)) {
			flushBuffer();
			return underlyingStack.pop(ctx);
		}
		return buffer.remove(buffer.size() - 1);
	}

	public void flushBuffer() {
		while (!buffer.isEmpty()) {
			Conditional<T> value = buffer.remove(0);
			underlyingStack.push(currentContext, value);
		}
		currentContext = null;
	}

	public static class Spy<T> {

		/**
		 * Get the underlying stack from a BufferedStack instance for inspection.
		 * 
		 * @param stack the BufferedStack instance
		 * @return the underlying VariationalStack
		 */
		public VariationalStack<T> getUnderlyingStack(BufferedStack<T> stack) {
			return stack.underlyingStack;
		}

		/**
		 * Get the buffer list from a BufferedStack instance for inspection.
		 * 
		 * @param stack the BufferedStack instance
		 * @return the buffer as a List of Conditional<T>
		 */
		public List<Conditional<T>> getBuffer(BufferedStack<T> stack) {
			return stack.buffer;
		}

		/**
		 * Get the current context from a BufferedStack instance for inspection.
		 * 
		 * @param stack the BufferedStack instance
		 * @return the current FeatureExpr context
		 */
		public FeatureExpr getCurrentContext(BufferedStack<T> stack) {
			return stack.currentContext;
		}

		/**
		 * Force flush the buffer in a BufferedStack instance.
		 * 
		 * @param stack the BufferedStack instance to manipulate
		 */
		public void flushBuffer(BufferedStack<T> stack) {
			stack.flushBuffer();
		}

		/**
		 * Directly set the current context of a BufferedStack for testing purposes.
		 * 
		 * @param stack the BufferedStack to manipulate
		 * @param ctx   the new FeatureExpr context to set
		 */
		public void setCurrentContext(BufferedStack<T> stack, FeatureExpr ctx) {
			stack.currentContext = ctx;
		}

		/**
		 * Check the invariant on the given dynamic array robot.
		 * 
		 * @param r ranking to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(BufferedStack<?> r) {
			return r.wellFormed();
		}
	}
}
