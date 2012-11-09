package event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Queue;

public class AbstractEventSourceProvider implements EventSource {
	
	public static final int EventStarted = 1 << 1;

	public static final int EventRunning = 1 << 2;

	public static final int EventCancelled = 1 << 3;

	public static final int EventCompleted = 1 << 4;
	
	protected Queue<EventSource> queue;
	
	private int status;
	
	@Override
	public boolean isEventStarted() {
		// TODO Auto-generated method stub
		return status>=EventStarted;
	}

	@Override
	public void eventStarted() {
		status=EventStarted;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEventComplete() {
		// TODO Auto-generated method stub
		return status==EventCompleted;
	}

	@Override
	public void eventFinished() {
		// TODO Auto-generated method stub
		status=EventCompleted;
	}

	@Override
	public boolean isEventCancelled() {
		// TODO Auto-generated method stub
		return status==EventCancelled;
	}

	@Override
	public void eventCancelled() {
		// TODO Auto-generated method stub
		status=EventCancelled;
	}
	
}
