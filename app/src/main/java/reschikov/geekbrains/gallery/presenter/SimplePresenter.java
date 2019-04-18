package reschikov.geekbrains.gallery.presenter;

import reschikov.geekbrains.gallery.data.MyText;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.SimpleBindable;

public class SimplePresenter {

    private final SimpleBindable simpleBindable;
    private final MyText myText;

    public SimplePresenter(SimpleBindable simpleBindable) {
        this.simpleBindable = simpleBindable;
        myText = new MyText();
    }

    public void tie(String string){
        String text = myText.getText();
        text = (text == null) ? string : text + string;
        myText.setText(text);
        simpleBindable.passString(text);
    }

    public void restore(String string){
        if (string == null) return;
        myText.setText(string);
    }
}
