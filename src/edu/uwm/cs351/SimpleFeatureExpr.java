package edu.uwm.cs351;

public class SimpleFeatureExpr implements FeatureExpr {
	private final boolean value;

	public SimpleFeatureExpr(boolean value) {
		this.value = value;
	}

	@Override
	public boolean evaluate() {
		return value;
	}
}
