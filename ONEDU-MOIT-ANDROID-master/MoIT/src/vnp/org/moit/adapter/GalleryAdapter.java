package vnp.org.moit.adapter;

import vnp.org.moit.db.EduTopicImage;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;

public class GalleryAdapter extends CursorAdapter {
	Gallery gallery;
	TextView forum_top_title_page;
	private View mx1;
	private Context context;

	public GalleryAdapter(Gallery gallery, TextView forum_top_title_page, View mx1, Context context) {
		super(context, null,true);
		this.gallery = gallery;
		this.forum_top_title_page = forum_top_title_page;
		this.mx1 = mx1;
		this.context = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String img = CommonAndroid.getString(cursor, "Img");
		ImageLoader.getInstance(context).displayImage(img, (ImageView) view, false);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				changeCursor((Cursor) results.values);
				forum_top_title_page.setText((gallery.getSelectedItemPosition() + 1) + " trong " + getCount());
				gallery.setVisibility(getCount() == 0 ? View.GONE : View.VISIBLE);
				mx1.setVisibility(getCount() == 0 ? View.GONE : View.VISIBLE);
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String topicId = null;
				if (constraint != null) {
					topicId = constraint.toString().trim();
				}
				FilterResults filterResults = new FilterResults();
				filterResults.values = new EduTopicImage(context).queryByTopicId(topicId);
				return filterResults;
			}
		};
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		Gallery.LayoutParams layoutParams = new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(layoutParams);
		return imageView;
	}

}
