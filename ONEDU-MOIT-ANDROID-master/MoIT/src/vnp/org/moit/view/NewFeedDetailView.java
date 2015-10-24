package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.NewFeedView
public class NewFeedDetailView extends LinearLayout {

	public NewFeedDetailView(Context context) {
		super(context);
		CommonAndroid.getView(context, R.layout.newfeeddetail, this);
		init();
	}

	public NewFeedDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(context, R.layout.newfeeddetail, this);
		init();
	}

	public NewFeedDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(context, R.layout.newfeeddetail, this);
		init();
	}

	private void init() {
	}

	public void loadData() {

	}
}