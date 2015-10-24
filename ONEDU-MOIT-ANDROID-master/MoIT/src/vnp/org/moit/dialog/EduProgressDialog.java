package vnp.org.moit.dialog;

import one.edu.vn.sms.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class EduProgressDialog extends ProgressDialog {

	public EduProgressDialog(Context context) {
		super(context,android.R.style.Theme_Translucent_NoTitleBar);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
	}
}