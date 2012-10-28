package waazdoh.common.model;

public class MProgress {
	private int total = 0;
	private int progress = 0;

	public void add(boolean b) {
		total++;
		if (b) {
			progress++;
		}
	}

	public boolean ready() {
		return total == 0 || total == progress;
	}

	public int getPersentage() {
		if (total == 0) {
			return 0;
		} else {
			return (int) (100.0f * progress / total);
		}
	}

	@Override
	public String toString() {
		return "Progress[" + getPersentage() + "]";
	}
}
