package edu.uwm.cs351;

public class SimpleConditional<T> implements Conditional<T> {
	private final T value;

	public SimpleConditional(T value) {
		this.value = value;
	}

	@Override
	public T getValue(FeatureExpr ctx) {
		return value;
	}
}
