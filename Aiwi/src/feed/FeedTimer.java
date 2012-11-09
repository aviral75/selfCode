package feed;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

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
						if(input instanceof ArrayList<?>){
							for (SyndEntryImpl syndEntryImpl : keepReadingFeeds) {
								if(!((ArrayList<SyndEntryImpl>) input).contains(syndEntryImpl)){
									((ArrayList<SyndEntryImpl>) input).add(syndEntryImpl);
									updateTrayIcon(syndEntryImpl,viewer.getTable().getShell());
								}
							}
							
						}
//						SyndEntryImpl entryImpl=new SyndEntryImpl();
//						entryImpl.setTitle("Aviral testing");
//						entryImpl.setLink("http://algorithmicstockmarketindia.blogspot.in/");
//						updateTrayIcon(entryImpl,viewer.getTable().getShell());
						viewer.setInput(input);
						viewer.refresh();
					}
					
				}
		
			});
			
			
		}
	};

		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(uploadCheckerTimerTask, 0, 1 * 60 * 1000);
	
}
	
private static void updateTrayIcon(final SyndEntryImpl syndEntryImpl, Shell shell) {final ToolTip tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
tip.setMessage(syndEntryImpl.getTitle());
tip.setText(syndEntryImpl.getLink());
Image image = new Image(Display.getDefault(), "C:\\Aviral\\stkData\\Aiwi\\icons\\sample.gif");
Tray tray = Display.getDefault().getSystemTray();
if (tray != null) {
	final TrayItem item = new TrayItem(tray, SWT.NONE);
	item.setImage(image);			
	item.setToolTip(tip);
	final Menu menu = new Menu(shell, SWT.POP_UP);
	MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
	menuItem.setText("Button A");
	menuItem = new MenuItem(menu, SWT.PUSH);
	menuItem.setText("Button B");
	menuItem = new MenuItem(menu, SWT.PUSH);
	menuItem.setText("Show Tooltip");
	menuItem.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			tip.setVisible(true);
		}
	});
	item.addListener (SWT.MenuDetect, new Listener () {
		public void handleEvent (Event event) {
			menu.setVisible (true);
		}
	});
	item.addSelectionListener (new SelectionListener () {
        @Override public void widgetDefaultSelected (SelectionEvent aEvent) {
            widgetSelected (aEvent);
        }

        @Override public void widgetSelected (SelectionEvent aEvent) {
            if (aEvent.stateMask == SWT.CTRL)
                item.dispose ();
        }
    });
	
	tip.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			try {
				item.dispose();
				((IWebBrowser) PlatformUI.getWorkbench().getBrowserSupport().createBrowser("aiwi.browser")).openURL(new URL(syndEntryImpl.getLink()));
			} catch (PartInitException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	});
}
else {			
	// Set the tooltip location manually.
	tip.setLocation(100, 100);
}}
}
