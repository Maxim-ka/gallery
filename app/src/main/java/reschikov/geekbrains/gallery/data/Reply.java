package reschikov.geekbrains.gallery.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reply {

	@SerializedName("hits")
	private List<MyImage> hits;

	public List<MyImage> getHits() {
		return hits;
	}
}
