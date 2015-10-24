package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.SplashView
public class SplashView extends LinearLayout {

	public SplashView(Context context) {
		super(context);
		init();
	}

	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.splash, this);
	}

}