package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.SplashView
public class S3View extends LinearLayout {

	public S3View(Context context) {
		super(context);
		init();
	}

	public S3View(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public S3View(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.s3, this);
	}
}