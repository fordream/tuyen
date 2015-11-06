package vnp.mr.mintmedical.base;

import vnp.mr.mintmedical.R;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

import com.vnp.core.common.VNPResize;
import com.vnp.core.datastore.DataStore;

public abstract class MbaseAdialog extends Dialog {
	public DialogInterface.OnClickListener clickListener;
	protected VNPResize resize = VNPResize.getInstance();

	public MbaseAdialog(Context context,
			DialogInterface.OnClickListener clickListener) {
		super(context, R.style.AppTheme);
		this.clickListener = clickListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		setContentView(getLayout());
	}

	public abstract int getLayout();



	ProgressDialog dialog;

	public void showDialogLoading(boolean isShow) {
		if (isShow) {
			if (dialog == null) {

				dialog = ProgressDialog.show(getContext(), null, getContext()
						.getString(R.string.loadding));
			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

	}
}