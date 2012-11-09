package event;

public interface EventSource {

	boolean isEventStarted();
	void eventStarted();
	boolean isEventComplete();
	void eventFinished();
	boolean isEventCancelled();
	void eventCancelled();
}
