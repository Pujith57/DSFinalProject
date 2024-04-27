package edu.uwm.cs351;

//Interface for the variational stack
public interface VariationalStack<T> {
	void push(FeatureExpr ctx, Conditional<T> value);

	Conditional<T> pop(FeatureExpr ctx);
}