package vnp.org.moit.adapter;

import java.util.ArrayList;
import java.util.List;

import one.edu.vn.sms.R;
import one.edu.vn.sms.html.TongKetHtmlUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import vnp.org.moit.db.EduMarkTableGet;
import vnp.org.moit.db.EduStudent;
import vnp.org.moit.db.ServerStatus;
import vnp.org.moit.utils.MOITUtils;
import vnp.org.moit.view.bangdiem.BangDiemTongKetItemView;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.internal.cp;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;
import com.vnp.core.common.LogUtils;

public abstract class BangDiemTongKetAdapter extends CursorAdapter {
	private Context context;
	private View empty;

	public BangDiemTongKetAdapter(Context context, View empty) {
		super(context, null, true);
		this.context = context;
		this.empty = empty;
	}

	@Override
	public void bindView(final View view, Context context, Cursor cursor) {
		final String smsid = CommonAndroid.getString(cursor, "smsid");
		final String NamHoc = CommonAndroid.getString(cursor, "NamHoc");
		final String studentid = CommonAndroid.getString(cursor, "studentid");
		String jSonMark = CommonAndroid.getString(cursor, "jSonMark");
		String avatarSchool = new EduStudent(context)
				.getSchoolAvatarByStudentId(studentid);
		String time = CommonAndroid.getString(cursor, "time");

		final String strPhanHoi = MOITUtils.getPhanHoi(context, studentid, "",
				2, NamHoc);
		final String title = MOITUtils.getTitle(context, studentid, "", 2, "");
		CommonAndroid.setText(view, R.id.textView1,
				new EduStudent(context).getSchoolNameFromSchooldID(studentid));
		CommonAndroid.setText(view, R.id.textView2, String.format(
				context.getString(R.string.diemtongketnamformat), NamHoc));
		String nameStudent = new EduStudent(context).getStudenName(studentid);
		if (!CommonAndroid.isBlank(nameStudent)) {
			nameStudent = nameStudent.toUpperCase();
		}
		CommonAndroid.setText(view, R.id.textView3, nameStudent);
		CommonAndroid.setText(view, R.id.textView4,
				MOITUtils.parseDate(time, context));

		ImageView inboxitem_avatar = CommonAndroid.getView(view,
				R.id.inboxitem_avatar);
		if (CommonAndroid.isBlank(avatarSchool)) {
			inboxitem_avatar.setImageResource(R.drawable.no_image_round);
		} else {
			ImageLoader.getInstance(context).displayImage(avatarSchool,
					inboxitem_avatar, true);
		}
		MOITUtils.setAsLink(view, R.id.chiase, R.string.chiase);
		MOITUtils.setAsLink(view, R.id.phanhoi, R.string.phanhoi);
		if (view.findViewById(R.id.chiase) != null)
			view.findViewById(R.id.chiase).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							view.setDrawingCacheEnabled(false);
							view.setDrawingCacheEnabled(true);
							Bitmap bitmap = view.getDrawingCache(true).copy(
									Config.ARGB_8888, false);
							chiase(smsid, bitmap);
						}
					});
		if (view.findViewById(R.id.phanhoi) != null)
			view.findViewById(R.id.phanhoi).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							phanhoi(smsid, strPhanHoi, title);
						}
					});

		final List<DiemCaNam> listDiem = loadDiems(jSonMark, context);

		if (listDiem.size() == 0) {
			view.findViewById(R.id.mlistx).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.mlistx).setVisibility(View.VISIBLE);
			LinearLayout inbox_item_hangngay_list = CommonAndroid.getView(view,
					R.id.inbox_item_hangngay_list);

//			addView(context, inbox_item_hangngay_list, listDiem);

			view.findViewById(R.id.mlistx).setLayoutParams(
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							(int) (listDiem.size() * context.getResources()
									.getDimension(R.dimen.dimen_31dp))));
			ListView listView = CommonAndroid.getView(view, R.id.hListView);
			listView.setAdapter(new DiemTongKetAdapter(listDiem));

		}
	}

	private void addView(Context context, LinearLayout main,
			List<DiemCaNam> listDiem) {
		main.removeAllViews();
		if (main.getChildCount() > 0)
			return;
		for (int position = 0; position < listDiem.size(); position++) {

			// BangDiemTongKetItemView bangDiemTongKetItemView = new
			// BangDiemTongKetItemView(context);
			// main.addView(bangDiemTongKetItemView);
			// bangDiemTongKetItemView.setData(listDiem.get(position), position,
			// listDiem.size());

			try {
				View convertView = CommonAndroid.getView(context,
						R.layout.diemtongket_item, null);
				DiemCaNam diemData = listDiem.get(position);

				TextView mon = CommonAndroid.getView(convertView, R.id.mon);
				TextView hk1 = CommonAndroid.getView(convertView, R.id.hk1);
				TextView hk2 = CommonAndroid.getView(convertView, R.id.hk2);
				TextView cn = CommonAndroid.getView(convertView, R.id.cn);
				ImageView imghk1 = CommonAndroid.getView(convertView,
						R.id.imghk1);
				ImageView imghk2 = CommonAndroid.getView(convertView,
						R.id.imghk2);
				ImageView imgcn = CommonAndroid
						.getView(convertView, R.id.imgcn);
				mon.setText(diemData.mon);

				imghk1.setBackgroundResource(MOITUtils.getResDrawable(
						diemData.hk1, context));
				imghk2.setBackgroundResource(MOITUtils.getResDrawable(
						diemData.hk2, context));
				imgcn.setBackgroundResource(MOITUtils.getResDrawable(
						diemData.cn, context));

				if (position == 0) {
					mon.setBackgroundColor(context.getResources().getColor(
							R.color.color_header));
					convertView.findViewById(R.id.rlhk1).setBackgroundColor(
							context.getResources().getColor(
									R.color.color_header));
					convertView.findViewById(R.id.rlhk2).setBackgroundColor(
							context.getResources().getColor(
									R.color.color_header));
					convertView.findViewById(R.id.rlcn).setBackgroundColor(
							context.getResources().getColor(
									R.color.color_header));

					hk1.setText(diemData.hk1);
					hk2.setText(diemData.hk2);
					cn.setText(diemData.cn);
				} else {
					hk1.setText(diemData.hk1);
					if (MOITUtils.isChuaCodiem(diemData.hk1)) {
						hk1.setText("-");
					}
					hk2.setText(diemData.hk2);
					if (MOITUtils.isChuaCodiem(diemData.hk2)) {
						hk2.setText("-");
					}
					cn.setText(diemData.cn);
					if (MOITUtils.isChuaCodiem(diemData.cn)) {
						cn.setText("-");
					}

					int resHk1 = MOITUtils
							.getResDrawable(diemData.hk1, context);
					// hk1.setBackgroundResource(resHk1);
					if (resHk1 == R.drawable.transfer) {
						hk1.setTextColor(convertView.getContext()
								.getResources()
								.getColor(R.color.color_txt_diem));
					} else {
						hk1.setTextColor(convertView.getContext()
								.getResources().getColor(android.R.color.white));
					}

					int resHk2 = MOITUtils
							.getResDrawable(diemData.hk2, context);
					// hk2.setBackgroundResource(resHk2);
					if (resHk2 == R.drawable.transfer) {
						hk2.setTextColor(convertView.getContext()
								.getResources()
								.getColor(R.color.color_txt_diem));
					} else {
						hk2.setTextColor(convertView.getContext()
								.getResources().getColor(android.R.color.white));
					}

					int resHCN = MOITUtils.getResDrawable(diemData.cn, context);
					// cn.setBackgroundResource(resHCN);
					if (resHCN == R.drawable.transfer) {
						cn.setTextColor(convertView.getContext().getResources()
								.getColor(R.color.color_txt_diem));
					} else {
						cn.setTextColor(convertView.getContext().getResources()
								.getColor(android.R.color.white));
					}
				}

				if (position == listDiem.size() - 1 || position == 0) {
					CommonAndroid.FONT.getInstance().setTypeFace(mon,
							"font/OpenSans-Bold.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(hk1,
							"font/OpenSans-Bold.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(hk2,
							"font/OpenSans-Bold.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(cn,
							"font/OpenSans-Bold.ttf");
				} else {
					CommonAndroid.FONT.getInstance().setTypeFace(mon,
							"font/OpenSans-Regular.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(hk1,
							"font/OpenSans-Regular.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(hk2,
							"font/OpenSans-Regular.ttf");
					CommonAndroid.FONT.getInstance().setTypeFace(cn,
							"font/OpenSans-Regular.ttf");
				}
				main.addView(convertView);
			} catch (Exception ex) {
			}
		}
	}

	private List<DiemCaNam> loadDiems(String jSonMark, Context context) {

		List<DiemCaNam> loadDiems = new ArrayList<DiemCaNam>();
		try {
			if (!CommonAndroid.isBlank(jSonMark)) {
				if (!jSonMark.contains("\"")) {

				}

				JSONArray array = new JSONArray(jSonMark);

				if (array.length() > 0) {
					loadDiems.add(new DiemCaNam(context
							.getString(R.string.monhoc), context
							.getString(R.string.hky1), context
							.getString(R.string.hky2), context
							.getString(R.string.cn)));
				}

				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String mon = CommonAndroid.getString(object, "mon");
					String hocky1 = CommonAndroid.getString(object, "hocky1");
					String hocky2 = CommonAndroid.getString(object, "hocky2");
					String canam = CommonAndroid.getString(object, "canam");

					loadDiems.add(new DiemCaNam(mon, hocky1, hocky2, canam));
				}
			}
		} catch (Exception e) {
		}
		return loadDiems;
	}

	public abstract void chiase(String smsid, Bitmap bitmap);

	public abstract void phanhoi(String smsid, String namhocStudenId,
			String title);

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return CommonAndroid
				.getView(context, R.layout.inbox_item_tongket, null);
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				Cursor cursor = (Cursor) results.values;
				changeCursor(cursor);

				if (empty != null) {
					if (!ServerStatus.getIn().isLoaded(
							ServerStatus.LOADMOREMarkTableGet)) {
						((TextView) empty)
								.setText(R.string.no_item_bangdiem_tongket);
					} else {
						((TextView) empty).setText(R.string.taidulieu);
					}

					empty.setVisibility(getCount() == 0 ? View.VISIBLE
							: View.GONE);
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
				filterResults.values = new EduMarkTableGet(context)
						.queryByStudentIdCanam(studentId);

				return filterResults;
			}
		};
	}

}
