package waazdoh.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import waazdoh.app.App;
import waazdoh.client.ClientListener;
import waazdoh.client.MClient;
import waazdoh.common.model.Song;
import waazdoh.swt.AppListenerAdapter;

public class WaitView extends Composite {

	private App app;
	private Label label;

	public WaitView(App app, Composite parent, int swtarg) {
		super(parent, swtarg);
		//
		this.app = app;
		GridLayout gridLayout = new GridLayout(1, false);
		setLayout(gridLayout);

		Label ltitle = new Label(this, SWT.CENTER);
		ltitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		ltitle.setText("WAIT");

		label = new Label(this, SWT.CENTER);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		label.setText("Message");

		Label label_1 = new Label(this, SWT.CENTER);
		label_1.setText("...");

		if (app != null) {
			app.addListener(new AppListenerAdapter() {

				@Override
				public void waitState(String nwaitstate) {
					appWaitState(nwaitstate);
				}

				@Override
				public void error(String title, String message, Exception e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void clientAdded(MClient c) {
					c.addClientListener(new ClientListener() {

						@Override
						public void newSong(Song s) {
							// TODO Auto-generated method stub

						}

						@Override
						public void songLoaded(Song song) {
							// TODO Auto-generated method stub

						}

						@Override
						public void loggedIn() {
							// TODO Auto-generated method stub

						}
					});
				}
			});
		}
	}

	protected void appWaitState(final String nwaitstate) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (nwaitstate != null) {
					label.setText(nwaitstate);
				}
			}
		});
	}
}
