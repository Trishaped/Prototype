package fr.trishaped.node;

public interface NodeLinker {

	public void link(Node node);
	
	public void unlink(Node node);
	
	public boolean hasLink(String... boxes);
	
	public void delete();
	
}
