package waazdoh.cp2p.impl.handlers;

import waazdoh.cutils.MID;

public interface ByteArraySource {

	byte[] get(MID streamid);

	void addDownload(MID streamid);
}
