package fr.trishaped.node;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.trishaped.Cleanable;
import fr.trishaped.Cleaner;

public final class NodeHandler implements Cleanable {

	private final Map<String, NodeContainer> containers;
	private final Object lock;
	private final Cleaner cleaner;
	
	private volatile boolean dirty;

	public NodeHandler(Cleaner cleaner) {
		this.containers = new HashMap<>();
		this.cleaner = cleaner;
		this.lock = new Object();
	}

	public void init(String[] names) {
		synchronized (lock) {
			for (String name : names) {
				containers.put(name, new NodeContainer(name));
			}
		}
	}

	public void add(Variable... variable) throws InterruptedException {
		synchronized (lock) {
			
			Set<String> loop = new LinkedHashSet<>();
			
			for(Variable var : variable) {
				
				System.out.println(var);
				
				if(add(var)) {
					loop.add(var.getBox());
				}
				
			}
			
			for(String box : loop) {
				containers.get(box).exclude();
			}
			
			request();
		}
	}

	public void add(LinkedVariable... linked) throws InterruptedException {
		synchronized (lock) {
			
			Set<String> loop = new LinkedHashSet<>();
			
			for(LinkedVariable link : linked) {
				
				Variable left = link.getLeft();
				Variable right = link.getRight();
				
				System.out.println(link);
				
				if(add(left)) {
					
					loop.add(left.getBox());
					
					if(add(right)) {
						loop.add(right.getBox());
						link(left, right);
					}
					
				}
				
			}
			
			for(String box : loop) {
				containers.get(box).exclude();
				containers.get(box).unlink();
			}
			
			request();
		}
	}
	
	private void link(Variable left, Variable right) {
			
		Node lnode = containers.get(left.getBox()).get(left.getIdentifier());
		Node rnode = containers.get(right.getBox()).get(right.getIdentifier());
		
		lnode.link(rnode);
		rnode.link(lnode);
		
	}

	private boolean add(Variable variable) {
		
		NodeContainer box = containers.get(variable.getBox());
		Node node = new Node(box, variable.getIdentifier());
		
		return box.add(node);
		
	}
	
	private void request() throws InterruptedException {
		dirty = true;
		cleaner.request(this);
	}

	public void finish() throws InterruptedException {
		
		synchronized (lock) {
			while(dirty) {
				lock.wait();
			}
		}
		
		System.out.println("Finish");
	}
	
	public void onClean() {
		synchronized (lock) {
			
			if(dirty) {
				for(Entry<String, NodeContainer> entry : containers.entrySet()) {
					
					NodeContainer container = entry.getValue();
					container.clean();
					
				}
			}

			dirty = false;
			lock.notifyAll();
		}
	}

}
