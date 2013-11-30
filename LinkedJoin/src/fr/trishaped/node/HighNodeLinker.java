package fr.trishaped.node;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class HighNodeLinker implements NodeLinker {

	private final LinkedHashSet<Node> links;
	private final Node instance;
	
	public HighNodeLinker(Node instance) {
		 this.links = new LinkedHashSet<>();
		 this.instance = instance;
	}
	
	@Override
	public void link(Node node) {
		links.add(node);
	}

	@Override
	public void unlink(Node node) {
		links.remove(node);
	}
	
	@Override
	public boolean hasLink(String... boxes) {
		
		if(links.isEmpty()) {
			return false;
		}
		
		Set<String> hasLink = new HashSet<>();
		
		for(String box : boxes) {
			hasLink.add(box);
		}
		
		hasLink.remove(instance.getContainerName());
		
		for(Node node : links) {

			hasLink.remove(node.getContainerName());
			
			if(hasLink.isEmpty()) {
				return true;
			}
			
		}
		
		return false;
	}
	
	@Override
	public void delete() {
		for(Node node : links) {
			node.unlink(instance);
		}
	}

}
