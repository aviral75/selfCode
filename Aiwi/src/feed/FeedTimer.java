package feed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import aiwi.Activator;

import com.sun.syndication.feed.synd.SyndEntryImpl;

import feed.view.FeederView;

public class FeedTimer {

	public static void getUpdates(final String url) {
		TimerTask uploadCheckerTimerTask = new TimerTask(){

			public void run() {
				final FeedReader reader=new FeedReader();
				Display.getDefault().asyncExec(new Runnable() {

					@SuppressWarnings("unchecked")
					@Override
					public void run() {
						IViewPart findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(FeederView.ID);
						if(findView instanceof FeederView){
							FeederView fView=(FeederView) findView;
							TableViewer viewer = fView.getViewer();
							Object input = viewer.getInput();
							List<SyndEntryImpl> keepReadingFeeds = reader.keepReadingFeeds(url);
							List<SyndEntryImpl> toAdd=new ArrayList<SyndEntryImpl>();
							if(input instanceof ArrayList<?>)
								for (SyndEntryImpl syndEntryImpl : keepReadingFeeds){
									if(!((ArrayList<SyndEntryImpl>) input).contains(syndEntryImpl))
										toAdd.add(syndEntryImpl);
								}
							((ArrayList<SyndEntryImpl>) input).addAll(toAdd);
							viewer.setInput(input);
							viewer.refresh();
							updateTrayIcon(toAdd,viewer.getTable().getShell());
						}

					}

				});


			}
		};

		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(uploadCheckerTimerTask, 0, 1 * 60 * 1000);

	}

	@SuppressWarnings("deprecation")
	private static void updateTrayIcon(final List<SyndEntryImpl> toAdd, final Shell shell) {
		if(toAdd.size() <=0)
			return;
		final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
			tip.setMessage(toAdd.size() + " new feed + tweets");
			tip.setText(toAdd.get(0).getUpdatedDate().toLocaleString());
			URL url = Activator.getDefault().getBundle().getEntry("/icons/bulb.gif");

			String fileURL = null;
			try {
				fileURL = FileLocator.toFileURL(url).toURI().getPath();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Image image = new Image(Display.getDefault(),fileURL);
			Tray tray = Display.getDefault().getSystemTray();
			if (tray != null) {
				final TrayItem item = new TrayItem(tray, SWT.NONE);
				item.setImage(image);			
				item.setToolTip(tip);
				item.addSelectionListener (new SelectionListener () {
					@Override public void widgetDefaultSelected (SelectionEvent aEvent) {
						widgetSelected (aEvent);
					}

					@Override public void widgetSelected (SelectionEvent aEvent) {
						if (aEvent.stateMask == SWT.CTRL)
							item.dispose ();
						else
							{
								tip.setVisible(true);

							}
					}
				});

				tip.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						try {
							if(item.isDisposed())
								return;
							shell.setActive();
							shell.setVisible(true);
							shell.setFocus();
							shell.setMinimized(false);
							((IWebBrowser) PlatformUI.getWorkbench().getBrowserSupport().createBrowser("aiwi.browser")).openURL(new URL(toAdd.get(0).getLink()));
							item.dispose();
						} catch (PartInitException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				
			} else
				// Set the tooltip location manually.
				tip.setLocation(100, 100);
		}
//	}
}
