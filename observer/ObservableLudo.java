package observer;

public interface ObservableLudo {
	public abstract void addObserver (ObserverLudo o);
	public abstract void removeObserver (ObserverLudo o);
	public Object getPioes();
}
