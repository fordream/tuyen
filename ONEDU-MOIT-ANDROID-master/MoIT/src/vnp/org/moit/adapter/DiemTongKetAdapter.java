package vnp.org.moit.adapter;

import java.util.ArrayList;
import java.util.List;

import one.edu.vn.sms.R;
import vnp.org.moit.utils.MOITUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseQuery.CachePolicy;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.FontsUtils;

@SuppressLint("ResourceAsColor")
public class DiemTongKetAdapter extends BaseAdapter {
	List<DiemCaNam> list = new ArrayList<DiemCaNam>();

	public DiemTongKetAdapter(List<DiemCaNam> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = CommonAndroid.getView(parent.getContext(), R.layout.diemtongket_item, null);
		}
		if (list.size() == position) {
			convertView.setVisibility(View.INVISIBLE);
		} else {
			convertView.setVisibility(View.VISIBLE);
		}
		try {

			DiemCaNam diemData = list.get(position);
			TextView mon = CommonAndroid.getView(convertView, R.id.mon);
			TextView hk1 = CommonAndroid.getView(convertView, R.id.hk1);
			TextView hk2 = CommonAndroid.getView(convertView, R.id.hk2);
			TextView cn = CommonAndroid.getView(convertView, R.id.cn);
			ImageView imghk1 = CommonAndroid.getView(convertView, R.id.imghk1);
			ImageView imghk2 = CommonAndroid.getView(convertView, R.id.imghk2);
			ImageView imgcn = CommonAndroid.getView(convertView, R.id.imgcn);
			mon.setText(diemData.mon);

			imghk1.setBackgroundResource(MOITUtils.getResDrawable(diemData.hk1, parent.getContext()));
			imghk2.setBackgroundResource(MOITUtils.getResDrawable(diemData.hk2, parent.getContext()));
			imgcn.setBackgroundResource(MOITUtils.getResDrawable(diemData.cn, parent.getContext()));

			if (position == 0) {
				mon.setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));
				convertView.findViewById(R.id.rlhk1).setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));
				convertView.findViewById(R.id.rlhk2).setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));
				convertView.findViewById(R.id.rlcn).setBackgroundColor(parent.getContext().getResources().getColor(R.color.color_header));

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

				int resHk1 = MOITUtils.getResDrawable(diemData.hk1, parent.getContext());
				//hk1.setBackgroundResource(resHk1);
				if (resHk1 == R.drawable.transfer) {
					hk1.setTextColor(convertView.getContext().getResources().getColor(R.color.color_txt_diem));
				} else {
					hk1.setTextColor(convertView.getContext().getResources().getColor(android.R.color.white));
				}

				int resHk2 = MOITUtils.getResDrawable(diemData.hk2, parent.getContext());
				//hk2.setBackgroundResource(resHk2);
				if (resHk2 == R.drawable.transfer) {
					hk2.setTextColor(convertView.getContext().getResources().getColor(R.color.color_txt_diem));
				} else {
					hk2.setTextColor(convertView.getContext().getResources().getColor(android.R.color.white));
				}

				int resHCN = MOITUtils.getResDrawable(diemData.cn, parent.getContext());
				//cn.setBackgroundResource(resHCN);
				if (resHCN == R.drawable.transfer) {
					cn.setTextColor(convertView.getContext().getResources().getColor(R.color.color_txt_diem));
				} else {
					cn.setTextColor(convertView.getContext().getResources().getColor(android.R.color.white));
				}
			}
			if (position == getCount() - 1 || position == 0) {
				CommonAndroid.FONT.getInstance().setTypeFace(mon, "font/OpenSans-Bold.ttf");
				CommonAndroid.FONT.getInstance().setTypeFace(hk1, "font/OpenSans-Bold.ttf");
				CommonAndroid.FONT.getInstance().setTypeFace(hk2, "font/OpenSans-Bold.ttf");
				CommonAndroid.FONT.getInstance().setTypeFace(cn, "font/OpenSans-Bold.ttf");
			} else {
				CommonAndroid.FONT.getInstance().setTypeFace(mon, "font/OpenSans-Regular.ttf");
				CommonAndroid.FONT.getInstance().setTypeFace(hk1, "font/OpenSans-Regular.ttf");
				CommonAndroid.FONT.getInstance().setTypeFace(hk2, "font/OpenSans-Regular.ttf");
				CommonAndroid.FONT.getInstance().setTypeFace(cn, "font/OpenSans-Regular.ttf");
			}

		} catch (Exception ex) {
		}
		return convertView;
	}

}
