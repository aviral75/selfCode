package event;

public class EventFactory {

	private static EventFactory eInstance;
	
	private EventFactory(){
	}
	
	public static EventFactory getInstance(){
		if(eInstance==null){
			eInstance=new EventFactory();
		}
		return eInstance;
	}
	
	public EventSource createEventSource(String type){
		EventSource s=null;
		if(type.equals("Widget")){
			s=new WidgetEventSource();
		}
		return s;
	}
}
