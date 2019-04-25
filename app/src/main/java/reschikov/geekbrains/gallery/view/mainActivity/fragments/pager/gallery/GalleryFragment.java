package reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;
import reschikov.geekbrains.gallery.R;
import reschikov.geekbrains.gallery.data.MyImage;
import reschikov.geekbrains.gallery.data.MyViewModelSpanCount;
import reschikov.geekbrains.gallery.presenter.GalleryPresenter;
import reschikov.geekbrains.gallery.view.mainActivity.fragments.pager.ViewPagerFragment;

public class GalleryFragment extends MvpAppCompatFragment implements Watchable {

    private MyAdapterRecycleView myAdapter;
    private RecyclerView recyclerView;
    private Transition transition;
    private int spanCount;

    @InjectPresenter
    GalleryPresenter presenter;

    @ProvidePresenter
	GalleryPresenter providePresenter(){
	    return new GalleryPresenter();
    }

	public MyAdapterRecycleView getMyAdapter() {
		return myAdapter;
	}

	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_gallery, container, false);
        transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.transition_item_gallery);
        if (getActivity() != null){
            MyViewModelSpanCount modelSpanCount =  new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory()).get(MyViewModelSpanCount.class);
            modelSpanCount.getLiveData().observe(this, this::setLayoutManager);
        }
        if (savedInstanceState != null) {
            spanCount = savedInstanceState.getInt("spanCount");
        }
        if (getParentFragment() != null) presenter.subscribe(((ViewPagerFragment)getParentFragment()).getPresenter());
        myAdapter = new MyAdapterRecycleView(presenter.getRecyclePresenter());
        recyclerView.setAdapter(myAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        new ItemTouchHelper(new MyItemTouchHelperCallback(myAdapter)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    private void setLayoutManager(int spanCount){
        this.spanCount = spanCount;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        TransitionManager.beginDelayedTransition(recyclerView, transition);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spanCount", spanCount);
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		presenter.unsubscribe();
	}

	@Override
	public void delete(int pos) {
		myAdapter.notifyItemRemoved(pos);
	}

	@Override
	public void check(int pos) {
		myAdapter.notifyItemChanged(pos);
	}

	@Override
	public void toLook(MyImage myImage) {
    	if (getActivity() == null) return;
		getActivity().getSupportFragmentManager().beginTransaction()
			.add(R.id.frame_master, ItemFragment.newInstance(myImage))
			.addToBackStack("resource")
			.commit();
	}
}
