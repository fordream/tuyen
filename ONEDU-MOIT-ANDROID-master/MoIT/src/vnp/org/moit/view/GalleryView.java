package vnp.org.moit.view;

import one.edu.vn.sms.R;
import vnp.org.moit.adapter.GalleryAdapter;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

public class GalleryView extends LinearLayout {
	Gallery gallery;
	TextView forum_top_title_page;
	View m1x;
	private String topicId;

	public void updateTopicId(String topicId) {
		this.topicId = topicId;
	}

	public GalleryView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.forumdetail_gallery, this);
		m1x = CommonAndroid.getView(this, R.id.m1x);
		forum_top_title_page = CommonAndroid.getView(this, R.id.forum_top_title_page);
		gallery = CommonAndroid.getView(this, R.id.gallery1);
		// gallery.setAdapter(new CursorAdapter(getContext(), null, true) {
		//
		// @Override
		// public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// ImageView imageView = new ImageView(arg2.getContext());
		// Gallery.LayoutParams layoutParams = new
		// Gallery.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// imageView.setLayoutParams(layoutParams);
		// // imageView.setScaleType(ScaleType.FIT);
		// return imageView;
		// }
		//
		// @Override
		// public void bindView(View arg0, Context arg1, Cursor arg2) {
		// String img = CommonAndroid.getString(arg2, "Img");
		// ImageLoader.getInstance(getContext()).displayImage(img, (ImageView)
		// arg0, false);
		// }
		//
		// @Override
		// public Filter getFilter() {
		// return new Filter() {
		//
		// @Override
		// protected void publishResults(CharSequence constraint, FilterResults
		// results) {
		// changeCursor((Cursor) results.values);
		// forum_top_title_page.setText((gallery.getSelectedItemPosition() + 1)
		// + " trong " + getCount());
		// gallery.setVisibility(getCount() == 0 ? View.GONE : View.VISIBLE);
		// m1x.setVisibility(getCount() == 0 ? View.GONE : View.VISIBLE);
		// }
		//
		// @Override
		// protected FilterResults performFiltering(CharSequence constraint) {
		// FilterResults filterResults = new FilterResults();
		// filterResults.values = new
		// EduTopicImage(getContext()).queryByTopicId(topicId);
		// return filterResults;
		// }
		// };
		// }
		// });
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				forum_top_title_page.setText((gallery.getSelectedItemPosition() + 1) + " trong " + parent.getCount());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				forum_top_title_page.setText("0 trong 0");
			}
		});

		View.OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = gallery.getSelectedItemPosition();
				if (v.getId() == R.id.left) {
					position--;
				} else {
					position++;
				}

				if (position > gallery.getAdapter().getCount() - 1) {
					position--;
				}
				if (position <= 0) {
					position = 0;
				}

				gallery.setSelection(position);
			}
		};
		findViewById(R.id.left).setOnClickListener(clickListener);
		findViewById(R.id.right).setOnClickListener(clickListener);

		galleryAdapter = new GalleryAdapter(gallery, forum_top_title_page, m1x, context);
		gallery.setAdapter(galleryAdapter);
	}

	GalleryAdapter galleryAdapter;

	public void update(Cursor cursor) {
		TextView forum_top_title = (TextView) findViewById(R.id.forum_top_title);
		forum_top_title.setText(CommonAndroid.getString(cursor, "Title"));
		TextView forum_top_title_page_content = (TextView) findViewById(R.id.forum_top_title_page_content);
		forum_top_title_page_content.setText(CommonAndroid.getString(cursor, "Des"));

		forum_top_title_page.setText("0/0");
	}

	public void update() {
		galleryAdapter.getFilter().filter(topicId);
	}

}
