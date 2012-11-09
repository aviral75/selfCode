package event;

public class EventApplication {

	public EventSource createEvent(String type){
		EventSource es=EventFactory.getInstance().createEventSource(type);
		es.eventStarted();
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			Thread.interrupted();
		}
		es.eventFinished();
		return es;
	}
	
	public void startApplication(){
		createEvent("Widget");
	}
}
