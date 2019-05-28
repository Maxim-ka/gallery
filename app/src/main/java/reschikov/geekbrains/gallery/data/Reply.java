package reschikov.geekbrains.gallery.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reply {

	@SerializedName("total")
	private int total;

	@SerializedName("totalHits")
	private int totalHits;

	@SerializedName("hits")
	private List<MyImage> hits;

	public int getTotal() {
		return total;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public List<MyImage> getHits() {
		return hits;
	}
}
