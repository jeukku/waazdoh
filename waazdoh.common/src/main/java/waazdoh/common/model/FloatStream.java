package waazdoh.common.model;


public interface FloatStream {
	AudioSample read(int i);

	float getSamplesPerSecond();

	int getLength();
}
