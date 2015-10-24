package vnp.org.moit.adapter;

import it.sephiroth.android.library.widget.HListView;

import java.util.ArrayList;
import java.util.List;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduAccount;
import vnp.org.moit.db.EduInbox;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.utils.MOITUtils;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;

public abstract class InboxAdapter extends CursorAdapter {
	private Context context;
	private View empty;

	public InboxAdapter(Context context, View empty) {
		super(context, null, true);
		this.context = context;
		this.empty = empty;
	}

	@Override
	public void bindView(final View view, Context context, Cursor cursor) {
		final String smsid = CommonAndroid.getString(cursor, "smsid");
		ImageView inboxitem_avatar = CommonAndroid.getView(view, R.id.inboxitem_avatar);
		CommonAndroid.setText(view, R.id.textView1, cursor, "sender");

		CommonAndroid.setText(view, R.id.textView2, MOITUtils.parseDate(CommonAndroid.getString(cursor, "timepost"), context));
		String sms = CommonAndroid.getString(cursor, "sms");
		String sms_tiengviet = CommonAndroid.getString(cursor, "sms_tiengviet");
		String jSonMark = CommonAndroid.getString(cursor, "jSonMark");
		if (CommonAndroid.isBlank(sms_tiengviet)) {
			sms_tiengviet = sms;
		}

		String studentId = CommonAndroid.getString(cursor, "studentid");
		final String strPhanHoi = MOITUtils.getPhanHoi(context, studentId, sms_tiengviet, 0, "");
		final String title = MOITUtils.getTitle(context, studentId, sms_tiengviet, 0, "");
		CommonAndroid.setText(view, R.id.textView3, sms_tiengviet);

		String schoolLogo = new EduStudent(context).getSchoolAvatar(CommonAndroid.getString(cursor, "schoolid"));
		ImageLoader.getInstance(context).displayImage(schoolLogo, inboxitem_avatar, true);
		MOITUtils.setAsLink(view, R.id.chiase, R.string.chiase);
		MOITUtils.setAsLink(view, R.id.phanhoi, R.string.phanhoi);
		if (view.findViewById(R.id.chiase) != null)
			view.findViewById(R.id.chiase).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					view.setDrawingCacheEnabled(false);
					view.setDrawingCacheEnabled(true);
					Bitmap bitmap = view.getDrawingCache(true).copy(Config.ARGB_8888, false);
					chiase(smsid, bitmap);
				}
			});
		if (view.findViewById(R.id.phanhoi) != null)
			view.findViewById(R.id.phanhoi).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					phanhoi(smsid, strPhanHoi, title);
				}
			});

//		HListView gallery = (HListView) view.findViewById(R.id.hListView);
//		final List<DiemHangNgay> listDiem = loadDiems(jSonMark);
//
//		if (listDiem.size() == 0) {
//			gallery.setVisibility(View.GONE);
//		} else {
//			gallery.setVisibility(View.VISIBLE);
//			gallery.setAdapter(new DiemAdapter(listDiem));
//			try {
//				gallery.setSelection(1);
//			} catch (Exception ex) {
//			}
//		}
	}

	private List<DiemHangNgay> loadDiems(String jSonMark) {

		List<DiemHangNgay> loadDiems = new ArrayList<DiemHangNgay>();
		return loadDiems;
	}

	public abstract void chiase(String smsid, Bitmap bitmap);

	public abstract void phanhoi(String smsid, String strstrPhanHoi, String title);

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid.getView(context, R.layout.inbox_item, null);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				Cursor cursor = (Cursor) results.values;
				changeCursor(cursor);

				if (empty != null) {
					if (!ServerStatus.getIn().isLoaded(ServerStatus.LOADMORETINTUC)) {
						((TextView) empty).setText(R.string.inbox_noitem);
					} else {
						((TextView) empty).setText(R.string.taidulieu);
					}

					empty.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);
				}

				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				String studentId = null;
				if (constraint != null) {
					studentId = constraint.toString().trim();
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = new EduInbox(context).queryByStudentIdNotDiem(studentId);

				return filterResults;
			}
		};
	}
}
