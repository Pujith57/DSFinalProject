package edu.uwm.cs351;

//Interface for conditional values, using generics for flexibility.
public interface Conditional<T> {
	T getValue(FeatureExpr ctx);
}
