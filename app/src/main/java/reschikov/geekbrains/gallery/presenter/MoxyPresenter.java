package reschikov.geekbrains.gallery.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import reschikov.geekbrains.gallery.data.MyText;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.MoxyBindable;

@InjectViewState
public class MoxyPresenter extends MvpPresenter<MoxyBindable> {

    private final MyText myText;

    public MoxyPresenter() {
        myText = new MyText();
    }

    public void tie(String string){
        String text = myText.getText();
        text = (text == null) ? string : text + string;
        myText.setText(text);
        getViewState().passString(text);
    }
}
