package fr.trishaped.node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class NodeContainer {
	
	private final String name;
	private final Map<Long, Node> nodes;
	
	private String[] links;
	private volatile boolean exclude;
	private volatile boolean unlink;

	public NodeContainer(String name) {
		this.name = name;
		this.nodes = new HashMap<>();
		this.links = new String[0];
	}

	public String getName() {
		return name;
	}
	
	public boolean add(Node node) {
		
		Long identifier = Long.valueOf(node.getIdentifier());
		
		if(exclude && !nodes.containsKey(identifier)) {
			return false;
		}
		
		if(!exclude) {
			System.err.println("Add: "+node);
			nodes.put(identifier, node);
		}
		
		return true;
	}
	
	public Node get(Long identifier) {
		return nodes.get(identifier);
	}
	
	public void remove(Node node) {
		System.err.println("Delete: "+node);
		nodes.remove(node);
	}

	public void hasInserted() {
		
		exclude = true;
		System.out.println(this + " has flags exclude");
		
	}

	public void hasLinked() {
		
		unlink = true;
		System.out.println(this + " has flags unlink");
		
	}
	
	public boolean shouldUnlink() {
		return unlink;
	}
	
	public void clean() {
		if(unlink) {
			
			Iterator<Entry<Long, Node>> iterator = nodes.entrySet().iterator();
			
			while (iterator.hasNext()) {
				
				Entry<Long, Node> entries = iterator.next();
				
				Node node = entries.getValue();
				
				boolean status = node.hasLink(links);
				
				System.out.println("Status for "+node+": "+status);
				
				if(!status) {
					System.err.println(node+" has not "+Arrays.toString(links));
					node.delete();
					iterator.remove();
				}
				
			}
			
		}
	}
	
	@Override
	public String toString() {
		return "Container: "+name;
	}

	public void link(String container) {
		
		for(String link : links) {
			if(link.equals(container)) {
				return;
			}
		}
		
		int index = links.length;
		links = Arrays.copyOf(links, index + 1);
		links[index] = container;

	}

}
