package vnp.org.moit.dialog;

import java.util.ArrayList;
import java.util.List;

import one.edu.vn.sms.R;
import vnp.org.moit.db.EduStudent;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.vnp.core.base.BaseAdialog;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoader;

public class ChonTruongOrHocSinhDialog extends BaseAdialog implements android.view.View.OnClickListener {

	private boolean isStudent = true;
	private String shooserId = null;
	private List<String> ids = new ArrayList<String>();
	private List<String> names = new ArrayList<String>();
	private List<String> logos = new ArrayList<String>();

	public ChonTruongOrHocSinhDialog(Context context, boolean isStudent, String isShowChooseAll) {
		super(context);
		this.isStudent = isStudent;
		this.shooserId = isShowChooseAll;
		setCancelable(true);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		findViewById(R.id.dialog_thongbao_btn_2).setOnClickListener(this);

		TextView txt1 = (TextView) findViewById(R.id.txt1);
		txt1.setText(isStudent ? R.string.luachonhocsinh : R.string.luachontruong);

		if (CommonAndroid.isBlank(shooserId)) {
			shooserId = "0";
		}
		// ids.add(null);
		// if (isStudent) {
		// names.add(getContext().getString(R.string.tatcahocsinh));
		// } else {
		// names.add(getContext().getString(R.string.tatcacactruong));
		// }
		//
		// logos.add("");

		Cursor cursor = new EduStudent(getContext()).querry();
		while (cursor != null && cursor.moveToNext()) {
			String StudentId = CommonAndroid.getString(cursor, "StudentId");
			String StudentName = CommonAndroid.getString(cursor, "Name");
			String SchoolId = CommonAndroid.getString(cursor, "SchoolId");
			String SchoolName = CommonAndroid.getString(cursor, "SchoolName");
			String SchoolLogo = CommonAndroid.getString(cursor, "SchoolLogo");

			if (isStudent) {
				if (!ids.contains(StudentId)) {
					ids.add(StudentId);
					names.add(StudentName);
					logos.add("");
				}

			} else {
				if (!ids.contains(SchoolId)) {
					ids.add(SchoolId);
					names.add(SchoolName);
					logos.add(SchoolLogo);
				}
			}
		}
		if (cursor != null)
			cursor.close();
		//if (isStudent) {
			ids.add("0");
			names.add(getContext().getString(R.string.tatca));
			logos.add("");

		//}

		final GridView dialog_thongbao_list = (GridView) findViewById(R.id.list);

		if (isStudent)
			dialog_thongbao_list.setNumColumns(1);
		dialog_thongbao_list.setAdapter(new ArrayAdapter<String>(getContext(), 0, names) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = CommonAndroid.getView(parent.getContext(), R.layout.item_choose, null);
					if (isStudent)
						convertView = CommonAndroid.getView(parent.getContext(), R.layout.item_choose_student, null);
				}
				TextView textView = CommonAndroid.getView(convertView, R.id.name);
				textView.setText(getItem(position));

				textView.setTextColor(getContext().getResources().getColor((R.color.chooser_normal)));
				convertView.findViewById(R.id.chooser_main).setBackgroundResource(R.drawable.a_chooser_normal);
				if (!CommonAndroid.isBlank(shooserId) && ids.get(position).equals(shooserId)) {
					textView.setTextColor(getContext().getResources().getColor(android.R.color.white));
					convertView.findViewById(R.id.chooser_main).setBackgroundResource(R.drawable.a_chooser_checked);
				}
				ImageLoader.getInstance(getContext()).display(convertView, R.id.img, R.drawable.no_image_round, logos.get(position), true);
				return convertView;
			}
		});
		dialog_thongbao_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// shooserId =
				if (CommonAndroid.isBlank(shooserId))
					shooserId = ids.get(position);
				else if (ids.get(position).equals(shooserId)) {
					shooserId = null;
				} else {
					shooserId = ids.get(position);
				}

				((ArrayAdapter<String>) dialog_thongbao_list.getAdapter()).notifyDataSetChanged();

				dismiss();
				fillter();
			}
		});

	}

	@Override
	public int getLayout() {
		return R.layout.dialog_luachon;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v.getId() == R.id.dialog_thongbao_btn_2) {
			// dong
			fillter();
		}
	}

	private void fillter() {
		if (CommonAndroid.isBlank(shooserId)) {
			onClickChooseAll();
		} else {
			String name = new EduStudent(getContext()).getSchoolName(shooserId);

			if (isStudent) {
				name = new EduStudent(getContext()).getStudenName(shooserId);
				if (CommonAndroid.isBlank(name))
					name = getContext().getString(R.string.tatcahocsinh);
			} else {
				if (CommonAndroid.isBlank(name))
					name = getContext().getString(R.string.tatcacactruong);
			}
			//CommonAndroid.toast(getContext(), shooserId + "");
			if ("0".equals(shooserId)) {
				onClickChooseAll();
			} else
				onItemClickListener(false, shooserId, name);
		}
	}

	public void onItemClickListener(boolean isStudent, String idStr, String name) {

	}

	public void onClickChooseAll() {

	}
}