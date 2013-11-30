package fr.trishaped.node;

public class Variable {

	private final String box;
	private final long identifier;
	
	public Variable(String box, long identifier) {
		this.box = box;
		this.identifier = identifier;
	}

	public String getBox() {
		return box;
	}

	public long getIdentifier() {
		return identifier;
	}
	
	@Override
	public String toString() {
		return "{ \"box\" : "+box+", \"identifier\" : "+identifier+" }";
	}
	
}
