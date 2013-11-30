package fr.trishaped;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class Cleaner implements Runnable {

	private final BlockingQueue<Cleanable> dirty;

	public Cleaner() {
		dirty = new LinkedBlockingQueue<Cleanable>();
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				
				Cleanable cleanable = dirty.take();
				cleanable.onClean();
				
			} catch (Exception e) {
				System.err.println(e);
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void request(Cleanable cleanable) throws InterruptedException {
		dirty.put(cleanable);
	}

}
