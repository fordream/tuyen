package vnp.org.moit.dialog;

import one.edu.vn.sms.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vnp.core.base.BaseAdialog;
import com.vnp.core.common.CommonAndroid;

public class ThongBaoDialog extends BaseAdialog implements android.view.View.OnClickListener {
	private String message;
	private String textbutton1;// null is not show
	private String textbutton2;// null is not show

	public ThongBaoDialog(Context context, int message, int textbutton1, int textbutton2) {
		super(context);
		this.message = getContext().getString(message);
		if (textbutton1 > 0)
			this.textbutton1 = getContext().getString(textbutton1);
		if (textbutton2 > 0)
			this.textbutton2 = getContext().getString(textbutton2);

	}

	public ThongBaoDialog(Context context, String message, int textbutton1, int textbutton2) {
		super(context);
		this.message = message;
		if (textbutton1 > 0)
			this.textbutton1 = getContext().getString(textbutton1);
		if (textbutton2 > 0)
			this.textbutton2 = getContext().getString(textbutton2);

	}

	public ThongBaoDialog(Context context, String message, String textbutton1, String textbutton2) {
		super(context);
		this.message = message;
		this.textbutton1 = textbutton1;
		this.textbutton2 = textbutton2;
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		((TextView) findViewById(R.id.dialog_thongbao_text)).setText(message);
		((TextView) findViewById(R.id.dialog_thongbao_btn_1)).setText(textbutton1);
		((TextView) findViewById(R.id.dialog_thongbao_btn_2)).setText(textbutton2);
		if (CommonAndroid.isBlank(textbutton1)) {
			((TextView) findViewById(R.id.dialog_thongbao_btn_1)).setVisibility(View.GONE);
		}

		if (CommonAndroid.isBlank(textbutton2)) {
			((TextView) findViewById(R.id.dialog_thongbao_btn_2)).setVisibility(View.GONE);
		}
		findViewById(R.id.dialog_thongbao_btn_1).setOnClickListener(this);
		findViewById(R.id.dialog_thongbao_btn_2).setOnClickListener(this);
	}

	@Override
	public int getLayout() {
		return R.layout.dialog_thongbao;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v.getId() == R.id.dialog_thongbao_btn_1) {
			onClickButton1();
		} else if (v.getId() == R.id.dialog_thongbao_btn_2) {
			onClickButton2();
		}
	}

	public void onClickButton2() {

	}

	public void onClickButton1() {

	}
}