package org.cp2p.impl.handlers;

import org.cutils.MID;

public interface ByteArraySource {

	byte[] get(MID streamid);

	void addDownload(MID streamid);
}
