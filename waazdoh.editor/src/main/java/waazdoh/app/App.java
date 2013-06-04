/*******************************************************************************
 * Copyright (c) 2013 Juuso Vilmunen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Juuso Vilmunen - initial API and implementation
 ******************************************************************************/
package waazdoh.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import waazdoh.app.audio.MAudio;
import waazdoh.app.audio.MAudioListener;
import waazdoh.app.swing.AppListener;
import waazdoh.client.ClientListener;
import waazdoh.client.MClient;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.Song;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.model.WaveTrack;
import waazdoh.cp2p.impl.P2PBinarySource;
import waazdoh.cutils.AppPreferences;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.MPreferences;
import waazdoh.emodel.ETrack;

public class App {
	private static final String WAIT_STATE_PUBLISHING = "PUBLISHING";
	public static final String VERSION = "20121113_1718";
	public static final String NOTIFICATION_AUDIO = "audio";
	protected static final String NOTIFICATION_AUDIO_ERROR = "audioerror";

	private MClient client;
	private MAudio player;
	//
	private List<AppListener> listeners = new LinkedList<AppListener>();
	private ESong _currentsong;
	private WaveTrack recordingtrack;
	private MPreferences preferences;

	private MLogger log = MLogger.getLogger(this);

	public App(final AppPreferences p) {
		final App me = this;
		player = new MAudio(new IMessages() {
			@Override
			public void add(String string) {
				notification(App.NOTIFICATION_AUDIO, string);
			}
		}, new IMessages() {

			@Override
			public void add(String string) {
				notification(App.NOTIFICATION_AUDIO_ERROR, string);
			}
		});
		//
		player.addListener(new MAudioListener() {

			@Override
			public void timeChanged(float time) {
				// TODO Auto-generated method stub

			}

			@Override
			public void stopped() {
				audioStopped();
			}

			@Override
			public void level(float input, float output) {
				// TODO Auto-generated method stub
			}
		});
		//
		this.preferences = p;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					me.client = new MClient(p, getSource(p));
					client.addClientListener(new ClientListener() {
						@Override
						public void newSong(Song s) {
							clientNewSong(s);
						}

						@Override
						public void songLoaded(Song song) {
							clientSongLoaded(song);
						}

						@Override
						public void loggedIn() {
							clientLoggedIn();
						}
					});
					//
					fireClientAdded(client);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

	private void fireClientAdded(MClient client) {
		List<AppListener> ls = listeners;
		for (AppListener appListener : ls) {
			appListener.clientAdded(client);
		}
	}

	private void fireRecordingTrackChanged(WaveTrack track) {
		List<AppListener> ls = listeners;
		for (AppListener appListener : ls) {
			appListener.recordingTrackChanged(track);
		}
	}

	private void clientLoggedIn() {
		Song s = client.newSong(); // TODO remove
		TrackGroup tg = s.addTrackGroup();
		tg.newInstrumentTrack();
		tg.newTrack();
	}

	private void clientSongLoaded(Song song) {
		selectSong(new ESong(song));
	}

	private void clientNewSong(Song s) {
		selectSong(new ESong(s));
	}

	public void selectSong(ESong s) {
		songChanged(s);
	}

	private MBinarySource getSource(MPreferences p) {
		MBinarySource testsource = new P2PBinarySource(p, true);
		return testsource;
	}

	public MClient getClient() {
		return client;
	}

	public void newSong() {
		client.newSong();
	}

	public void songChanged(ESong s) {
		setCurrentSong(s);
		//
		LinkedList<AppListener> ls = getListeners();
		for (AppListener listener : ls) {
			listener.songChanged(s);
		}
	}

	private LinkedList<AppListener> getListeners() {
		return new LinkedList<AppListener>(listeners);
	}

	public void addListener(AppListener songsListener) {
		listeners.add(songsListener);
		if (client != null) {
			songsListener.clientAdded(client);
		}
	}

	public synchronized void play() {
		try {
			if (getCurrentsong().getSong().checkTracks().ready()) {
				getCurrentsong().getSong().save();
				player.play(getAudioBuffer(), getCurrentsong().getSong());
			} else {
				notification("THIS", "play pressed. sowut?");
			}
		} catch (Exception e) {
			error("App", "play", e);
		}
	}

	public void record() {
		try {
			if (recordingtrack != null) {
				recordingtrack.clear();
			}

			getCurrentsong().getSong().save();

			player.record(getAudioBuffer(), getAudioSampleSkip(),
					getCurrentsong().getSong());
		} catch (Exception e) {
			notification("record", "" + e);
		}
	}

	public MAudio getAudio() {
		return player;
	}

	public synchronized void stop() {
		player.stopAudio();
	}

	protected synchronized void audioStopped() {
		if (recordingtrack != null) {
			recordingtrack.replaceWave(getAudio().getRecordedTrack());
			client.save(getCurrentsong().getSong());
		}
	}

	public void publish() {
		fireWaitState(App.WAIT_STATE_PUBLISHING);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				client.publish(getCurrentsong().getSong());
				fireWaitState(null);
			}
		});
		t.start();
	}

	protected ESong getCurrentsong() {
		return _currentsong;
	}

	private void fireWaitState(String nwaitstate) {
		List<AppListener> ls = this.listeners;
		for (AppListener appListener : ls) {
			appListener.waitState(nwaitstate);
		}
	}

	public void shutdown() {
		player.stopAudio();
		client.stop();
	}

	public void setRecording(ESong song, WaveTrack track) {
		this.setCurrentSong(song);
		this.recordingtrack = track;
		fireRecordingTrackChanged(track);
		//

	}

	private void setCurrentSong(ESong song) {
		this._currentsong = song;
	}

	public ETrack importTrack(String sfile) {
		try {
			ETrack track = getCurrentsong().getSong().newTrack();
			track.importFile(sfile);
			return track;
		} catch (UnsupportedAudioFileException e) {
			this.error("Track import", "unknow filetype " + sfile, e);
		} catch (IOException e) {
			this.error("Track import", "reading file failed " + sfile, e);
		} catch (Exception e) {
			this.error("ERROR", "ERROR " + sfile, e);
		}

		return null;
	}

	private void error(String title, String message, Exception e) {
		List<AppListener> ls = listeners;
		for (AppListener listener : ls) {
			listener.error(title, message, e);
		}
	}

	public MPreferences getPreferences() {
		return preferences;
	}

	public void notification(String notificationsource, String string) {
		for (AppListener l : this.listeners) {
			l.notification(notificationsource, string);
		}
	}

	public int getAudioBuffer() {
		return getPreferences().getInteger("audio.buffer", 8800);
	}

	private int getAudioSampleSkip() {
		return getPreferences().getInteger("audio.samples.skip", 200);
	}

	public String getInfo() {

		return (getClient() != null ? getClient().getInfoText() : "Client null")
				+ " version:" + VERSION;
	}

	public void zoom(int zoom) {
		if (getCurrentsong() != null) {
			getCurrentsong().zoomChanged(zoom);
		}
	}

	public void exportSongTo(String path) {
		log.info("export song to " + path);
		try {
			getCurrentsong()
					.getSong()
					.getOutputWave()
					.writeWAV(
							new FileOutputStream(path + File.separator
									+ "output.wav"));
			log.info("export done");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			error("export", "failed to export output to " + path, e);
		} catch (IOException e) {
			e.printStackTrace();
			error("export", "failed to export output to " + path, e);
		}
	}

}
