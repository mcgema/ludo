package observer;

public interface ObservableIF {
	public abstract void addObserver (ObserverTom o);
	public abstract void removeObserver (ObserverTom o);
	public Object getPioes();
}
