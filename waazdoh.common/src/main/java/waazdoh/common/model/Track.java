package waazdoh.common.model;

import waazdoh.cutils.MID;

public interface Track {

	MID getID();

	void save();

	void publish();

	void checkProgress(MProgress p);

	long getModifytime();

	AudioInfo getAudioInfo();

	AudioSampleStream getStream();

	void clearMemory(int time);

	String getDetailInfo();

	Float getViewSample(int isample);

}
