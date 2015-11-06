/**
 * 
 */
package vnp.mr.mintmedical.base;

import vnp.mr.mintmedical.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vnp.core.common.VNPResize;
import com.vnp.core.datastore.DataStore;

/**
 * @author teemo
 * 
 */
public abstract class MBaseFragment extends Fragment {
	private Object item;

	public void onHiddenKeyBoard() {

	}
	public void setupListView(View view) {
		if (view != null && view instanceof ListView) {
			ListView listView = (ListView) view;
			listView.setDivider(null);
			listView.setSelector(R.drawable.listview_selected);
		}
	}
	public void setData(Object item) {
		this.item = item;
	}

	public Object getData() {
		return item;
	}

	protected VNPResize resize = VNPResize.getInstance();
	private Fragment parent;

	public interface IMBaseFragmentOnClickListener {
		public void onClick(Object dialog, int which);
	}

	private IMBaseFragmentOnClickListener onClickListener;

	public void setOnClickListener(IMBaseFragmentOnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public IMBaseFragmentOnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setParent(Fragment parent) {
		this.parent = parent;
	}

	public Fragment getParent() {
		return parent;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(getLayout(), null);
	}

	public View findViewById(int res) {
		return getView().findViewById(res);
	}

	/**
	 * 
	 */
	public MBaseFragment() {
	}

	public abstract int getLayout();



	ProgressDialog dialog;

	public void showDialogLoading(boolean isShow) {
		if (isShow) {
			if (dialog == null) {
				dialog = ProgressDialog.show(getActivity(), null,
						getString(R.string.loadding));
			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

	}

	public abstract int getHeaderRes();
}
