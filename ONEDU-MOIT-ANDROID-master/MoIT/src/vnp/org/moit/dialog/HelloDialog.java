package vnp.org.moit.dialog;

import one.edu.vn.sms.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.vnp.core.base.BaseAdialog;

public class HelloDialog extends BaseAdialog implements android.view.View.OnClickListener {

	public HelloDialog(Context context) {
		super(context);
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);setCancelable(true);
		findViewById(R.id.dialog_thongbao_btn_2).setOnClickListener(this);

	}

	@Override
	public int getLayout() {
		return R.layout.dialoghello;
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}
}