package fr.trishaped.node;


public final class Node implements NodeLinker {

	private final NodeContainer container;
	private final long id;
	private final NodeLinker links;

	public Node(NodeContainer box, long id) {
		this.container = box;
		this.id = id;
		this.links = new HighNodeLinker(this);
	}

	public String getContainerName() {
		return container.getName();
	}
	
	public long getIdentifier() {
		return id;
	}

	public void link(Node node) {
		
		System.err.println("Link : "+this+" with "+node);
		
		links.link(node);
		container.link(node.getContainerName());
	}
	
	public void unlink(Node node) {
		
		System.err.println("Unlink : "+this+" with "+node);
		
		links.unlink(node);
		if(!links.hasLink(node.getContainerName())) {
			delete();
		}
	}

	@Override
	public boolean hasLink(String... boxes) {
		return links.hasLink(boxes);
	}

	@Override
	public void delete() {
		links.delete();
		container.remove(this);
	}
	
	@Override
	public String toString() {
		return "Node: "+id+"@"+getContainerName();
	}

}
