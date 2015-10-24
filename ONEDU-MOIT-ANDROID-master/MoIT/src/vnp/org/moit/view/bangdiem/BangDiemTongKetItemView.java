package vnp.org.moit.view.bangdiem;

import one.edu.vn.sms.R;
import vnp.org.moit.adapter.DiemCaNam;
import vnp.org.moit.utils.MOITUtils;

import com.vnp.core.common.CommonAndroid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BangDiemTongKetItemView extends LinearLayout {

	public BangDiemTongKetItemView(Context context) {
		super(context);
		init();
	}

	public BangDiemTongKetItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BangDiemTongKetItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private TextView mon;
	private TextView hk1;
	private TextView hk2;
	private TextView cn;
	private ImageView imghk1;
	private ImageView imghk2;
	private ImageView imgcn;

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.diemtongket_item, this);
		mon = CommonAndroid.getView(this, R.id.mon);
		hk1 = CommonAndroid.getView(this, R.id.hk1);
		hk2 = CommonAndroid.getView(this, R.id.hk2);
		cn = CommonAndroid.getView(this, R.id.cn);
		imghk1 = CommonAndroid.getView(this, R.id.imghk1);
		imghk2 = CommonAndroid.getView(this, R.id.imghk2);
		imgcn = CommonAndroid.getView(this, R.id.imgcn);
	}

	public void setData(DiemCaNam diemData, int position, int count) {
		mon.setText(diemData.mon);

		imghk1.setBackgroundResource(MOITUtils.getResDrawable(diemData.hk1, getContext()));
		imghk2.setBackgroundResource(MOITUtils.getResDrawable(diemData.hk2, getContext()));
		imgcn.setBackgroundResource(MOITUtils.getResDrawable(diemData.cn, getContext()));

		if (position == 0) {
			mon.setBackgroundColor(getContext().getResources().getColor(R.color.color_header));
			findViewById(R.id.rlhk1).setBackgroundColor(getContext().getResources().getColor(R.color.color_header));
			findViewById(R.id.rlhk2).setBackgroundColor(getContext().getResources().getColor(R.color.color_header));
			findViewById(R.id.rlcn).setBackgroundColor(getContext().getResources().getColor(R.color.color_header));

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

			int resHk1 = MOITUtils.getResDrawable(diemData.hk1, getContext());
			if (resHk1 == R.drawable.transfer) {
				hk1.setTextColor(getContext().getResources().getColor(R.color.color_txt_diem));
			} else {
				hk1.setTextColor(getContext().getResources().getColor(android.R.color.white));
			}

			int resHk2 = MOITUtils.getResDrawable(diemData.hk2, getContext());
			if (resHk2 == R.drawable.transfer) {
				hk2.setTextColor(getContext().getResources().getColor(R.color.color_txt_diem));
			} else {
				hk2.setTextColor(getContext().getResources().getColor(android.R.color.white));
			}

			int resHCN = MOITUtils.getResDrawable(diemData.cn, getContext());
			if (resHCN == R.drawable.transfer) {
				cn.setTextColor(getContext().getResources().getColor(R.color.color_txt_diem));
			} else {
				cn.setTextColor(getContext().getResources().getColor(android.R.color.white));
			}
		}

		if (position == count - 1 || position == 0) {
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
	}
}
