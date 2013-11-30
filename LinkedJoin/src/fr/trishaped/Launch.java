package fr.trishaped;

import fr.trishaped.node.NodeHandler;

public final class Launch {

	public static void main(String[] args) throws InterruptedException {

		Cleaner cleaner = new Cleaner();
		NodeHandler handler = new NodeHandler(cleaner);

		handler.init(new String[] { "x", "y", "z" });

		Thread clean = new Thread(cleaner);
		Thread inject = new Thread(new Inject(handler));
		
		inject.start();
		clean.start();
		
		clean.join();
		inject.join();
		
		
	}

}
