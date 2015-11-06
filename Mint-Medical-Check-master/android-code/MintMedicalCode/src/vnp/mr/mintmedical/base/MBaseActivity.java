package vnp.mr.mintmedical.base;

import vnp.mr.mintmedical.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.viewpagerindicator.db.DBUserLogin;
import com.vnp.core.common.VNPResize;
import com.vnp.core.datastore.DataStore;

public abstract class MBaseActivity extends FragmentActivity {
	protected VNPResize resize = VNPResize.getInstance();
	public DBUserLogin dbUserLogin;

	public void changeFragemtn(int r_id_content_frame, Fragment rFragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(r_id_content_frame, rFragment);
		ft.commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbUserLogin = new DBUserLogin(this);
		resize.init(this, 320, 480, null);
		DataStore.getInstance().init(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(getLayout());
	}

	public <T extends View> T getView(int res) {
		@SuppressWarnings("unchecked")
		T view = (T) findViewById(res);
		return view;
	}

	public abstract int getLayout();

	ProgressDialog dialog;

	public void showDialogLoading(boolean isShow) {
		if (isShow) {
			if (dialog == null) {

				dialog = ProgressDialog.show(this, null,
						getString(R.string.loadding));
			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}

	public void setupListView(View view) {
		if (view != null && view instanceof ListView) {
			ListView listView = (ListView) view;
			listView.setDivider(null);
			listView.setSelector(R.drawable.listview_selected);
		}
	}
}