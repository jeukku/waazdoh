package cmusic.swt;

import java.util.Date;
import java.util.List;

import org.cutils.MLogger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import waazdoh.CMusic;
import waazdoh.common.model.Track;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.model.TrackGroupListener;
import waazdoh.common.waves.WaveGenerator;
import waazdoh.common.waves.WaveGeneratorSample;
import waazdoh.emodel.ETrack;

import cmusic.app.App;
import cmusic.app.ESong;
import cmusic.swt.layouts.RowFillLayout;

public class TrackGroupComposite extends Composite {
	private TrackGroup trackgroup;
	private Composite ctracks;
	private ESong song;
	final private App app;
	private MLogger log = MLogger.getLogger(this);

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param nsong
	 * @param napp
	 */
	public TrackGroupComposite(ESong nsong, TrackGroup eTrackGroup,
			Composite parent, App napp) {
		super(parent, SWT.NONE);
		this.trackgroup = eTrackGroup;
		this.song = nsong;
		this.app = napp;
		//
		setLayout(new TitleLayout());

		final Text lgroupname = new Text(this, SWT.BORDER);
		lgroupname.setText("trackgroup");
		lgroupname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				trackgroup.setName(lgroupname.getText());
			}
		});

		Composite cbuttons = new Composite(this, SWT.NONE);
		cbuttons.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button bnewtrack = new Button(cbuttons, SWT.NONE);
		bnewtrack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createTestTrack();
				// trackgroup.newTrack();
			}
		});
		bnewtrack.setText("New Tack");

		ctracks = new Composite(this, SWT.BORDER);
		RowFillLayout ctrackslayout = new RowFillLayout();
		ctrackslayout.setWidthComposite(this);
		ctracks.setLayout(ctrackslayout);
		addTracks();
		//
		trackgroup.addListener(new TrackGroupListener() {
			@Override
			public void trackAdded(Track track) {
				add(track);
			}
		});

	}

	// public org.eclipse.swt.graphics.Point computeSize(int wHint, int hHint) {
	// return new Point(400 * ctracks.getChildren().length, 200);
	// };

	private void addTracks() {
		List<Track> ts = trackgroup.getTracks();
		for (Track eTrack : ts) {
			add(eTrack);
		}
	}

	private void add(Track eTrack) {
		new MixerTrackComposite(app, song, eTrack, ctracks);
		layout();
		ctracks.layout();
	}

	public TrackGroup getTrackgroup() {
		return this.trackgroup;
	}

	private void createTestTrack() {
		ETrack et = song.getSong().newTrack();

		final int length = CMusic.DEFAULT_SAMPLERATE * 10;
		WaveGenerator gen = new WaveGenerator();
		gen.generate(et, 0, length, new WaveGeneratorSample() {
			@Override
			public float getSample(float sample) {
				float sec = sample / CMusic.DEFAULT_SAMPLERATE;
				float sin = (float) Math.sin(sec * 2 * Math.PI * 110.0f);

				if (sec < 0.1f) {
					log.info("sec " + sample + " " + sin);
				}
				return sin;
			}
		});
		//
		Track t = trackgroup.newTrack();
		t.setName("testname" + new Date());
		t.replaceWave(et);
	}
}
