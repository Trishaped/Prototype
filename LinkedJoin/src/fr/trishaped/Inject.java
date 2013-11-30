package fr.trishaped;

import java.util.Random;

import fr.trishaped.node.LinkedVariable;
import fr.trishaped.node.NodeHandler;
import fr.trishaped.node.Variable;

public final class Inject implements Runnable {

	private final NodeHandler query;

	public Inject(NodeHandler query) {
		this.query = query;
	}

	@Override
	public void run() {
		
		try {
			
			query.add(
					new Variable("x", 1), 
					new Variable("x", 2), 
					new Variable("x", 3), 
					new Variable("x", 4)
			);
			
			Thread.sleep(delay());
			
			query.add(
					new LinkedVariable(
							new Variable("x", 1), 
							new Variable("y", 5)
					),
					new LinkedVariable(
							new Variable("x", 1), 
							new Variable("y", 6)
					),
					new LinkedVariable(
							new Variable("x", 2), 
							new Variable("y", 7)
					),
					new LinkedVariable(
							new Variable("x", 2), 
							new Variable("y", 8)
					),
					new LinkedVariable(
							new Variable("x", 3), 
							new Variable("y", 9)
					)
			);
			
			Thread.sleep(delay());
			
			query.add(
					new Variable("z", 97), 
					new Variable("z", 98), 
					new Variable("z", 99), 
					new Variable("z", 100)
			);
			
			Thread.sleep(delay());
			
			query.add(
					new LinkedVariable(
							new Variable("z", 97), 
							new Variable("x", 1)
					),
					new LinkedVariable(
							new Variable("z", 99), 
							new Variable("x", 3)
					),
					new LinkedVariable(
							new Variable("z", 100), 
							new Variable("x", 3)
					),
					new LinkedVariable(
							new Variable("z", 100), 
							new Variable("x", 4)
					),
					new LinkedVariable(
							new Variable("z", 100), 
							new Variable("x", 5)
					)
			);
			
			query.finish();
			
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public int delay() {
		
		int min = 1;
		int max = 126;
		
		// Ignore Optimization
	    return new Random().nextInt((max - min) + 1) + min;
	    
	}

}
