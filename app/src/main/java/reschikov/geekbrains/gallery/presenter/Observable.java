package reschikov.geekbrains.gallery.presenter;

public interface Observable {
	void subscribe(Observer observer);
	void unsubscribe();
}
