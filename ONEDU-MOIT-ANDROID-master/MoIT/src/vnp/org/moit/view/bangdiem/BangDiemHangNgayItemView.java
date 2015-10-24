package vnp.org.moit.view.bangdiem;

import one.edu.vn.sms.R;
import vnp.org.moit.adapter.DiemHangNgay;
import vnp.org.moit.utils.MOITUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

public class BangDiemHangNgayItemView extends LinearLayout {

	public BangDiemHangNgayItemView(Context context) {
		super(context);
		init();
	}

	public BangDiemHangNgayItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BangDiemHangNgayItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private TextView mon;
	private TextView diem;
	private TextView loai;

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.diemhangngay_item, this);
		mon = CommonAndroid.getView(this, R.id.mon);
		diem = CommonAndroid.getView(this, R.id.diem);
		loai = CommonAndroid.getView(this, R.id.loai);
	}

	public void setData(DiemHangNgay diemData, int position, int count) {
		mon.setText(diemData.mon);
		diem.setText(diemData.diem);

		int res = MOITUtils.getResDrawable(diemData.diem, getContext());

		CommonAndroid.getView(this, R.id.imgdiem).setBackgroundResource(res);
		if (res == R.drawable.transfer) {
			diem.setTextColor(this.getContext().getResources().getColor(R.color.color_txt_diem));
		} else {
			diem.setTextColor(this.getContext().getResources().getColor(android.R.color.white));
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
			mon.setBackgroundColor(getContext().getResources().getColor(R.color.color_header));
			loai.setBackgroundColor(getContext().getResources().getColor(R.color.color_header));
			this.findViewById(R.id.rldiem).setBackgroundColor(getContext().getResources().getColor(R.color.color_header));
		} else {
			mon.setBackgroundColor(getContext().getResources().getColor(R.color.color_none));
			loai.setBackgroundColor(getContext().getResources().getColor(R.color.color_none));
			findViewById(R.id.rldiem).setBackgroundColor(getContext().getResources().getColor(R.color.color_none));
		}

		if (!MOITUtils.isDiem(diemData.diem) && position != 0) {
			diem.setText("-");
		}

		loai.setText(diemData.loai);
	}
}
