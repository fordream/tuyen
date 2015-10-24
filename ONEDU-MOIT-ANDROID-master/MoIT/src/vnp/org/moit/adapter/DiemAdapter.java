package vnp.org.moit.adapter;

import java.util.ArrayList;
import java.util.List;

import one.edu.vn.sms.R;
import vnp.org.moit.utils.MOITUtils;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

public class DiemAdapter extends BaseAdapter {
	List<DiemHangNgay> list = new ArrayList<DiemHangNgay>();

	public DiemAdapter(List<DiemHangNgay> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = CommonAndroid.getView(parent.getContext(), R.layout.diemhangngay_item, null);
		}
		if (list.size() == position) {
			convertView.setVisibility(View.INVISIBLE);
		} else {
			convertView.setVisibility(View.VISIBLE);
		}
		try {

			DiemHangNgay diemData = list.get(position);
			TextView mon = CommonAndroid.getView(convertView, R.id.mon);
			TextView diem = CommonAndroid.getView(convertView, R.id.diem);
			TextView loai = CommonAndroid.getView(convertView, R.id.loai);
			mon.setText(diemData.mon);
			diem.setText(diemData.diem);

			int res = MOITUtils.getResDrawable(diemData.diem, parent.getContext());

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
				mon.setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));
				loai.setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));
				convertView.findViewById(R.id.rldiem).setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));
			} else {
				mon.setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_none));
				loai.setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_none));
				convertView.findViewById(R.id.rldiem).setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_none));
			}

			if (!MOITUtils.isDiem(diemData.diem) && position != 0) {
				diem.setText("-");
			}

			loai.setText(diemData.loai);

		} catch (Exception ex) {
		}
		return convertView;
	}

}
