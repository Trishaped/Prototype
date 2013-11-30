package fr.umlv.waterfowl.queryProcessor;

public class OptimTriple {
	private int queryNumber;
	private Integer type;
	
	public OptimTriple(int queryNumber) {
		this.queryNumber = queryNumber;
		type = null;
	}	
	public int getQueryNumber() {
		return queryNumber;
	}

	public void setQueryNumber(int queryNumber) {
		this.queryNumber = queryNumber;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public void prettyPrint() {
		System.out.println("#"+queryNumber +"\ttype = "+ type );
	}
	
}
