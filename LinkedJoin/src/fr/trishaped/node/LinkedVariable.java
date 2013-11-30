package fr.trishaped.node;

public final class LinkedVariable {
	
	private final Variable left;
	private final Variable right;
	
	public LinkedVariable(Variable left, Variable right) {
		this.left = left;
		this.right = right;
	}

	public Variable getLeft() {
		return left;
	}

	public Variable getRight() {
		return right;
	}
	
	@Override
	public String toString() {
		return "{ \"left\" : "+left.toString()+", \"right\" : "+right.toString()+"}";
	}

}
