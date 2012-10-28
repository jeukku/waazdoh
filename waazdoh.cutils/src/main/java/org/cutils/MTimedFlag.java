package org.cutils;

public class MTimedFlag {
	private long lastreset;
	private int delaytime;
	
	public MTimedFlag(int readytimer) {
		delaytime = readytimer;
		reset();
	}
	
	public void reset() {
		this.lastreset = System.currentTimeMillis();
	}
	
	public boolean isTriggered() {
		return System.currentTimeMillis() - lastreset > delaytime;
	}
	
	public void trigger() {
		lastreset = 0;
		synchronized (this) {
			notifyAll();
		}
	}
	
	@Override
	public String toString() {
		return "Trigger[" + isTriggered() + "]["
				+ (System.currentTimeMillis() - lastreset) + "]";
	}

	public void waitTimer() {
		while(!isTriggered()) {
			synchronized (this) {
				try {
					wait(delaytime/10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
