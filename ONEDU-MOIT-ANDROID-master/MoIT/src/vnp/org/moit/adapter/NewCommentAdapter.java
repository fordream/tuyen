package vnp.org.moit.adapter;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduArticleAddComment;
import vnp.org.moit.db.EduPersion;
import vnp.org.moit.utils.MOITUtils;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;

public class NewCommentAdapter extends CursorAdapter {
	private Context context;

	public NewCommentAdapter(Context context) {
		super(context, null, true);
		this.context = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView avatar = CommonAndroid.getView(view, R.id.avatar);
		TextView newfeed_detail_item_coment_name = CommonAndroid.getView(view, R.id.newfeed_detail_item_coment_name);
		TextView newfeed_detail_item_coment_time = CommonAndroid.getView(view, R.id.newfeed_detail_item_coment_time);
		TextView newfeed_detail_item_coment_txt = CommonAndroid.getView(view, R.id.newfeed_detail_item_coment_txt);
		
		
		newfeed_detail_item_coment_name.setText(CommonAndroid.getString(cursor, "OwnerName"));
		newfeed_detail_item_coment_time.setText(MOITUtils.parseDate(CommonAndroid.getString(cursor, "createTime"), context));
		newfeed_detail_item_coment_txt.setText(CommonAndroid.getString(cursor, "Des"));
		avatar.setImageResource(R.drawable.no_image_round);
		
		String phone = CommonAndroid.getString(cursor, "Phone");
		String mAvatar = CommonAndroid.getString(cursor, "avatar");
		if (CommonAndroid.isBlank(mAvatar)) {
			mAvatar = new EduPersion(context).getAvatar(phone);
		}
		
		ImageLoader.getInstance(context).displayImage(mAvatar, avatar, true);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid.getView(context, R.layout.newfeed_detail_item_coment, null);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				changeCursor((Cursor) results.values);
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				String where = String.format("%s = '%s'", "ArticelId", constraint.toString());
				filterResults.values = new EduArticleAddComment(context).querry(where, "createTime DESC");
				return filterResults;
			}
		};
	}
}
