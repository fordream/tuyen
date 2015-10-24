package vnp.org.moit.adapter.test;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduArticleGetList;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.utils.MOITUtils;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.imgloader.v1.ImageLoaderTypeConvert;

public class TestNewFeedAdapter extends CursorAdapter {
	private Context context;
	private View empty;

	public TestNewFeedAdapter(Context context, View empty) {
		super(context, null, true);
		this.context = context;
		this.empty = empty;
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		// if (arg0 instanceof NewFeedItemView) {
		// ((NewFeedItemView) arg0).update(arg2);
		// }

		TextView nameSchool = CommonAndroid.getView(view, R.id.textView1);
		TextView timeSchool = CommonAndroid.getView(view, R.id.textView2);
		TextView numberLike = CommonAndroid.getView(view, R.id.textView3);
		TextView numberComment = CommonAndroid.getView(view, R.id.textView4);

		ImageView avatar = CommonAndroid.getView(view, R.id.newfeed_avatar);
		ImageView img = CommonAndroid.getView(view, R.id.newfeed_img_main);
		TextView title = CommonAndroid.getView(view, R.id.textView5);
		TextView subContent = CommonAndroid.getView(view, R.id.textView6);
		String SchoolId = CommonAndroid.getString(cursor, "SchoolId");
		EduStudent eduStudent = new EduStudent(context);
		nameSchool.setText(eduStudent.getSchoolName(SchoolId));

		timeSchool.setText(MOITUtils.parseDate(CommonAndroid.getString(cursor, "createTime"), context));
		numberLike.setText(MOITUtils.parseNumberLikeOrNumberComment(CommonAndroid.getString(cursor, "likeNumber")));
		numberComment.setText(MOITUtils.parseNumberLikeOrNumberComment(CommonAndroid.getString(cursor, "commentNumber")));

		title.setText(Html.fromHtml(CommonAndroid.getString(cursor, "Title")));
		subContent.setText(Html.fromHtml(CommonAndroid.getString(cursor, "subDes")));

		String imgPath = CommonAndroid.getString(cursor, "imagePath");
		if (!CommonAndroid.isBlank(imgPath) && !MOITUtils.isStartHtml(imgPath)) {
			String userver_base = context.getString(R.string.userver_base);
			imgPath = userver_base + imgPath;
		}

		// img.setImageResource(R.drawable.no_image);
		// avatar.setImageResource(R.drawable.no_image_round);
		// ImageLoader.getInstance(context).displayImage(imgPath, img, false);
		// ImageLoader.getInstance(context).displayImage(eduStudent.getSchoolAvatar(SchoolId),
		// avatar, true);

		com.vnp.core.common.imgloader.v1.ImageLoader.getInstance(context).displayImage(imgPath, img, ImageLoaderTypeConvert.NONE, 200, R.drawable.no_image);
		com.vnp.core.common.imgloader.v1.ImageLoader.getInstance(context).displayImage(eduStudent.getSchoolAvatar(SchoolId), avatar, ImageLoaderTypeConvert.ROUND, 100, R.drawable.no_image_round);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid.getView(context, R.layout.testnewfeeditem, null);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				changeCursor((Cursor) results.values);
				if (empty != null) {
					empty.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);

					if (!ServerStatus.getIn().isLoaded(ServerStatus.LOADMORETINTUC)) {
						((TextView) empty).setText(R.string.forum_noitem);
					} else {
						((TextView) empty).setText(R.string.taidulieu);
					}
				}
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String schoolId = null;
				if (constraint != null)
					schoolId = constraint.toString();

				FilterResults filterResults = new FilterResults();
				filterResults.values = new EduArticleGetList(context).queryBySchoolId(schoolId);

				return filterResults;
			}
		};
	}
}
