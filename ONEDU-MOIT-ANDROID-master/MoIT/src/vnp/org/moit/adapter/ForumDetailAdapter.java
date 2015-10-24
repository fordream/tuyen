package vnp.org.moit.adapter;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduPersion;
import vnp.org.moit.db.EduTopicCommentGetList;
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

public class ForumDetailAdapter extends CursorAdapter {
	private Context context;

	public ForumDetailAdapter(Context context) {
		super(context, null, true);
		this.context = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView inboxitem_avatar = CommonAndroid.getView(view, R.id.inboxitem_avatar);
		TextView name = CommonAndroid.getView(view, R.id.textView1);
		TextView time = CommonAndroid.getView(view, R.id.textView2);
		TextView comment = CommonAndroid.getView(view, R.id.textView3);

		name.setText(CommonAndroid.getString(cursor, "OwnerName"));
		time.setText(MOITUtils.parseDate(CommonAndroid.getString(cursor, "createTime"), context));
		comment.setText(CommonAndroid.getString(cursor, "Des"));
		inboxitem_avatar.setImageResource(R.drawable.no_image_round);
		String phone = CommonAndroid.getString(cursor, "Phone");
		String mAvatar = CommonAndroid.getString(cursor, "avatar");
		if (CommonAndroid.isBlank(mAvatar)) {
			mAvatar = new EduPersion(context).getAvatar(phone);
		}
		ImageLoader.getInstance(context).displayImage(mAvatar, inboxitem_avatar, true);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid.getView(context, R.layout.forumdetail_item, null);
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
				String TopicID = constraint.toString();
				FilterResults filterResults = new FilterResults();
				filterResults.values = new EduTopicCommentGetList(context).queryByTopicId(TopicID);

				return filterResults;
			}
		};
	}
}
