package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vnp.core.common.CommonAndroid;

public class LoadingView extends LinearLayout {

	public LoadingView(Context context) {
		super(context);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}



	private void init() {
		CommonAndroid.getView(getContext(), R.layout.loading, this);
		CommonAndroid.getView(this, R.id.loading).setOnClickListener(null);
	}
}