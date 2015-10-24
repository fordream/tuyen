package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

public class EmptyView extends LinearLayout {
	private String message;

	public EmptyView(Context context, String message) {
		super(context);
		this.message = message;
		init();
	}

	public EmptyView(Context context, int message) {
		super(context);
		this.message = context.getString(message);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.empty, this);
		TextView message = CommonAndroid.getView(this, R.id.empty_txt);
		message.setText(this.message);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		findViewById(R.id.empty_main).setVisibility(visibility);
		setLayoutParams(visibility == View.GONE);
	}

	public void setLayoutParams(boolean is0) {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int height = display.getHeight() - (int) getContext().getResources().getDimension(R.dimen.dimen_130dp);
		if (is0)
			height = 0;
		AbsListView.LayoutParams l = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
		setLayoutParams(l);
	}
}