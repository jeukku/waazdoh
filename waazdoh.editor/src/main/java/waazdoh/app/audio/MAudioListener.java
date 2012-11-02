package waazdoh.app.audio;

public interface MAudioListener {

	void timeChanged(float time);

	void level(float input, float output);

	void stopped();
}
