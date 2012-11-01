package cmusic.swt;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wb.swt.SWTResourceManager;

import cmusic.app.App;
import cmusic.app.ESong;
import cmusic.app.audio.MAudioListener;
import cmusic.swt.components.AudioLevelMeter;
import cmusic.swt.layouts.SplitLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class SongToolsComposite extends Composite {
	private App app;
	private Label ltime;
	private ESong currentsong;
	private float lasttimechanged;
	private AudioLevelMeter inputLevelMeter;
	private AudioLevelMeter outputLevelMeter;
	private Label laudiostate;

	public SongToolsComposite(App napp, Composite parent, int style) {
		super(parent, style);
		this.app = napp;
		GridLayout gridLayout = new GridLayout(10, false);
		gridLayout.marginWidth = 1;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 1;
		setLayout(gridLayout);
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 4, 1));
		RowLayout rl_composite_1 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_1.spacing = 0;
		rl_composite_1.marginTop = 0;
		rl_composite_1.marginRight = 0;
		rl_composite_1.marginLeft = 0;
		rl_composite_1.marginBottom = 0;
		composite_1.setLayout(rl_composite_1);
		Button bplay = new Button(composite_1, SWT.NONE);
		bplay.setText("play");
		bplay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				app.play();
			}
		});
		bplay.setImage(SWTResourceManager.getImage(this.getClass(),
				"/media-playback-start.png"));
		Button bstop = new Button(composite_1, SWT.NONE);
		bstop.setText("stop");
		bstop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				app.stop();
			}
		});
		bstop.setImage(SWTResourceManager.getImage(SongToolsComposite.class,
				"/media-playback-stop.png"));
		Button bnext = new Button(composite_1, SWT.NONE);
		bnext.setText(">");
		bnext.setImage(SWTResourceManager.getImage(SongToolsComposite.class,
				"/media-skip-forward.png"));
		Button bprev = new Button(composite_1, SWT.NONE);
		bprev.setText("<");
		bprev.setImage(SWTResourceManager.getImage(SongToolsComposite.class,
				"/media-skip-backward.png"));
		Button brecord = new Button(composite_1, SWT.NONE);
		brecord.setText("rec");
		brecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				app.record();
			}
		});
		brecord.setImage(SWTResourceManager.getImage(SongToolsComposite.class,
				"/media-record.png"));

		Composite clevels = new Composite(this, SWT.BORDER);
		GridData gd_clevels = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_clevels.heightHint = 28;
		gd_clevels.widthHint = 63;
		clevels.setLayoutData(gd_clevels);
		clevels.setLayout(new SplitLayout(clevels));
		outputLevelMeter = new AudioLevelMeter(clevels, SWT.NONE);
		inputLevelMeter = new AudioLevelMeter(clevels, SWT.NONE);
		ltime = new Label(this, SWT.BORDER | SWT.CENTER);
		GridData gd_ltime = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_ltime.heightHint = 33;
		ltime.setLayoutData(gd_ltime);
		ltime.setAlignment(SWT.CENTER);
		ltime.setFont(SWTResourceManager.getFont("Meiryo", 16, SWT.NORMAL));
		ltime.setText("00:00:0000");
		
		laudiostate = new Label(this, SWT.BORDER | SWT.WRAP);
		GridData gd_laudiostate = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_laudiostate.heightHint = 30;
		gd_laudiostate.widthHint = 84;
		laudiostate.setLayoutData(gd_laudiostate);
		
				Label lblZoom = new Label(this, SWT.NONE);
				lblZoom.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
				lblZoom.setText("zoom");
		
				final Spinner spinner = new Spinner(this, SWT.BORDER);
				spinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
				spinner.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent arg0) {
						if (app != null) {
							app.zoom(Integer.parseInt("0" + spinner.getText()));
						}
					}
				});
				spinner.setMaximum(100000);
				spinner.setMinimum(1);
				spinner.setSelection(100);
		
		Composite actionscomposite = new Composite(this, SWT.NONE);
		RowLayout rl_actionscomposite = new RowLayout(SWT.HORIZONTAL);
		rl_actionscomposite.marginBottom = 0;
		rl_actionscomposite.spacing = 0;
		rl_actionscomposite.marginLeft = 0;
		rl_actionscomposite.marginRight = 0;
		rl_actionscomposite.marginTop = 0;
		actionscomposite.setLayout(rl_actionscomposite);
		actionscomposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		Button bpublish = new Button(actionscomposite, SWT.RIGHT);
		bpublish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				app.publish();
			}
		});
		bpublish.setText("publish");
		//
		if (app != null) {
			app.getAudio().addListener(new MAudioListener() {
				@Override
				public void timeChanged(float time) {
					audioTimeChanged(time);
				}

				@Override
				public void level(float input, float output) {
					inputLevelChanged(input);
					outputLevelChanged(output);
				}
				
				@Override
				public void stopped() {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	private void inputLevelChanged(float level) {
		inputLevelMeter.setLevel(level);
	}

	private void outputLevelChanged(float level) {
		outputLevelMeter.setLevel(level);
	}

	private void audioTimeChanged(float time) {
		if (time - lasttimechanged > 9 || time < lasttimechanged) {
			this.lasttimechanged = time;
			long msec = (long) (time);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(msec);
			final String s = getDigitString(cal.get(Calendar.MINUTE), 2) + ":"
					+ getDigitString(cal.get(Calendar.SECOND), 2) + ":"
					+ getDigitString(msec % 1000, 3);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					ltime.setText(s + " "
							+ Calendar.getInstance().get(Calendar.SECOND));
				}
			});
		}
	}

	private String getDigitString(long value, int i) {
		String s = "" + value;
		while (s.length() < i) {
			s = "0" + s;
		}
		return s;
	}

	public void setSong(ESong s) {
		currentsong = s;
	}

	public void setAudioState(String string) {
		laudiostate.setText(string);
	}
}
