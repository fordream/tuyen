package vnp.org.moit.adapter;

import java.util.ArrayList;
import java.util.List;

import one.edu.vn.sms.R;
import one.edu.vn.sms.html.HangNgayHtmlUtils;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;

public abstract class BangDiemHangNgayAdapter extends CursorAdapter {
	private Context context;
	private View empty;

	public BangDiemHangNgayAdapter(Context context, View empty) {
		super(context, null, true);
		this.context = context;
		this.empty = empty;
	}

	@Override
	public void bindView(final View view, Context context, Cursor cursor) {
		final String smsid = CommonAndroid.getString(cursor, "smsid");
		ImageView inboxitem_avatar = CommonAndroid.getView(view, R.id.inboxitem_avatar);
		CommonAndroid.setText(view, R.id.textView1, cursor, "sender");

		String diemngay = String.format(context.getString(R.string.diemngayformat), MOITUtils.parseDateDMY(CommonAndroid.getString(cursor, "timepost"), context));
		CommonAndroid.setText(view, R.id.textView2, diemngay);
		String sms = CommonAndroid.getString(cursor, "sms");
		String sms_tiengviet = CommonAndroid.getString(cursor, "sms_tiengviet");
		String jSonMark = CommonAndroid.getString(cursor, "jSonMark");
		if (CommonAndroid.isBlank(sms_tiengviet)) {
			sms_tiengviet = sms;
		}

		String studentId = CommonAndroid.getString(cursor, "studentid");
		final String strPhanHoi = MOITUtils.getPhanHoi(context, studentId, sms_tiengviet, 1, "");
		final String title = MOITUtils.getTitle(context, studentId, sms_tiengviet, 1, "");
		CommonAndroid.setText(view, R.id.textView4, MOITUtils.parseDate(CommonAndroid.getString(cursor, "timepost"), context));
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

		// ListView gallery = (ListView) view.findViewById(R.id.hListView);
		final List<DiemHangNgay> listDiem = loadDiems(jSonMark, context);

		if (listDiem.size() == 0) {
			view.findViewById(R.id.mlistx).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.mlistx).setVisibility(View.VISIBLE);
			 LinearLayout inbox_item_hangngay_list =
			 CommonAndroid.getView(view, R.id.inbox_item_hangngay_list);

			 addView(context, inbox_item_hangngay_list, listDiem);
			
			
//			view.findViewById(R.id.mlistx).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (listDiem.size() * context.getResources().getDimension(R.dimen.dimen_31dp))));
//			HangNgayHtmlUtils.post(listDiem, context, smsid);
//			WebView inbox_tongweb = CommonAndroid.getView(view, R.id.inbox_hangngayweb);
//			inbox_tongweb.getSettings().setLoadWithOverviewMode(true);
//			inbox_tongweb.getSettings().setUseWideViewPort(true);
//			inbox_tongweb.loadUrl("file://" + HangNgayHtmlUtils.getPath(context, smsid));

			// View viewItemBangDiem = CommonAndroid.getView(context,
			// R.layout.diemhangngay_item, null);
			// gallery.setLayoutParams(new
			// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)
			// (listDiem.size() *
			// context.getResources().getDimension(R.dimen.dimen_41dp))));
			// gallery.setAdapter(new DiemAdapter(listDiem));
			// try {
			// gallery.setSelection(1);
			// } catch (Exception ex) {
			// }
		}
	}

	private void addView(Context context, LinearLayout main, List<DiemHangNgay> listDiem) {
		// inbox_item_hangngay_list.removeAllViews();
		if (main.getChildCount() > 0)
			return;
		for (int position = 0; position < listDiem.size(); position++) {
			View convertView = CommonAndroid.getView(context, R.layout.diemhangngay_item, null);
			DiemHangNgay diemData = listDiem.get(position);
			// BangDiemHangNgayItemView bangDiemHangNgayItemView = new
			// BangDiemHangNgayItemView(context);
			// main.addView(bangDiemHangNgayItemView);
			// bangDiemHangNgayItemView.setData(diemData, position,
			// listDiem.size());

			try {

				TextView mon = CommonAndroid.getView(convertView, R.id.mon);
				TextView diem = CommonAndroid.getView(convertView, R.id.diem);
				TextView loai = CommonAndroid.getView(convertView, R.id.loai);
				mon.setText(diemData.mon);
				diem.setText(diemData.diem);

				int res = MOITUtils.getResDrawable(diemData.diem, context);

				CommonAndroid.getView(convertView, R.id.imgdiem).setBackgroundResource(res);
				if (res == R.drawable.transfer) {
					diem.setTextColor(convertView.getContext().getResources().getColor(R.color.color_txt_diem));
				} else {
					diem.setTextColor(convertView.getContext().getResources().getColor(android.R.color.white));
				}

				if (position == 0) {
					CommonAndroid.FONT.getInstance().setTypeFace(mon, "font/OpenSans-Bold.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(diem, "font/OpenSans-Bold.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(loai, "font/OpenSans-Bold.ttf");
				} else {
					CommonAndroid.FONT.getInstance().setTypeFace(mon, "font/OpenSans-Regular.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(diem, "font/OpenSans-Regular.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(loai, "font/OpenSans-Regular.ttf");
				}

				if (position == 0) {
					mon.setBackgroundColor(context.getResources().getColor(R.color.color_header));
					loai.setBackgroundColor(context.getResources().getColor(R.color.color_header));
					convertView.findViewById(R.id.rldiem).setBackgroundColor(context.getResources().getColor(R.color.color_header));
				} else {
					mon.setBackgroundColor(context.getResources().getColor(R.color.color_none));
					loai.setBackgroundColor(context.getResources().getColor(R.color.color_none));
					convertView.findViewById(R.id.rldiem).setBackgroundColor(context.getResources().getColor(R.color.color_none));
				}

				if (!MOITUtils.isDiem(diemData.diem) && position != 0) {
					diem.setText("-");
				}

				loai.setText(diemData.loai);

			} catch (Exception ex) {
			}

			main.addView(convertView);
		}

	}

	private List<DiemHangNgay> loadDiems(String jSonMark, Context context) {

		List<DiemHangNgay> loadDiems = new ArrayList<DiemHangNgay>();
		try {

			if (!CommonAndroid.isBlank(jSonMark)) {
				if (!jSonMark.contains("\"")) {
					jSonMark = jSonMark.replace("{mon:", "{\"mon\":\"");
					jSonMark = jSonMark.replace(",loai:", "\",\"loai\":\"");
					jSonMark = jSonMark.replace(",diem:", "\",\"diem\":\"");
					jSonMark = jSonMark.replace("}", "\"}");
				}
				JSONArray array = new JSONArray(jSonMark);
				if (array.length() > 0) {
					loadDiems.add(new DiemHangNgay(context.getString(R.string.monhoc), context.getString(R.string.diem), context.getString(R.string.loaidiem)));
				}

				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					loadDiems.add(new DiemHangNgay(object.getString("mon"), object.getString("diem"), object.getString("loai")));
				}
			}
		} catch (Exception e) {
		}
		return loadDiems;
	}

	public abstract void chiase(String smsid, Bitmap bitmap);

	public abstract void phanhoi(String smsid, String strPhanHoi, String title);

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid.getView(context, R.layout.inbox_item_hangngay, null);
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
						((TextView) empty).setText(R.string.no_item_bangdiem_hangngay);
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
				filterResults.values = new EduInbox(context).queryByStudentIdBangDiemHangNgay(studentId, true);

				return filterResults;
			}
		};
	}

}
