package vnp.org.moit.adapter;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduTopic;
import vnp.org.moit.db.ServerStatus;
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

public class ForumAdapter extends CursorAdapter {
	private Context context;
	private View empty;

	public ForumAdapter(Context context, View empty) {
		super(context, null, true);
		this.context = context;
		this.empty = empty;
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		try {
			TextView textView = CommonAndroid.getView(arg0, R.id.textView1);
			TextView textView2 = CommonAndroid.getView(arg0, R.id.textView2);
			TextView textView3 = CommonAndroid.getView(arg0, R.id.textView3);
			TextView textView4 = CommonAndroid.getView(arg0, R.id.textView4);
			ImageView inboxitem_avatar = CommonAndroid.getView(arg0, R.id.inboxitem_avatar);

			textView.setText(CommonAndroid.getString(arg2, "Title"));

			String sender = CommonAndroid.getString(arg2, "OwnerName");
			sender = String.format(context.getString(R.string.boi), sender);

			textView2.setText(MOITUtils.parseDate(CommonAndroid.getString(arg2, "createTime"), arg1) + " " + sender);
			textView3.setText(MOITUtils.parseNumberLikeOrNumberComment(CommonAndroid.getString(arg2, "likeNumber")));
			textView4.setText(MOITUtils.parseNumberLikeOrNumberComment(CommonAndroid.getString(arg2, "commentNumber")));
			inboxitem_avatar.setImageResource(R.drawable.no_image);
			// avatar
			String MainImage = CommonAndroid.getString(arg2, "MainImage");
			ImageLoader.getInstance(arg1).displayImage(MainImage, inboxitem_avatar, false, (int) arg1.getResources().getDimension(R.dimen.dimen_50dp));
		} catch (Exception ex) {
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid.getView(context, R.layout.forum_item, null);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				changeCursor((Cursor) results.values);
				if (empty != null) {
					if (!ServerStatus.getIn().isLoaded(ServerStatus.LOADMOREDIENDAN)) {
						((TextView) empty).setText(R.string.taotopic);
					} else {
						((TextView) empty).setText(R.string.taidulieu);
					}
					empty.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);
				}
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String idSchool = null;
				if (constraint != null) {
					idSchool = constraint.toString();
				}
				FilterResults filterResults = new FilterResults();
				filterResults.values = new EduTopic(context).queryIdSchool(idSchool);

				return filterResults;
			}
		};
	}
}
