package vnp.org.moit.custom;

import one.edu.vn.sms.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.custom.CustomTextView
public class CustomTextView extends com.rockerhieu.emojicon.EmojiconTextView {
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);

		try {
			mTextPos = a.getInteger(R.styleable.CustomTextView_txtCustomType, -1);
		} finally {
			a.recycle();
		}
		if (mTextPos == 0) {
			CommonAndroid.FONT.getInstance().setTypeFace(this, "font/OpenSans-Bold.ttf");
		} else if (mTextPos == 1) {
			CommonAndroid.FONT.getInstance().setTypeFace(this, "font/OpenSans-Regular.ttf");
		} else if (mTextPos == 2) {
			CommonAndroid.FONT.getInstance().setTypeFace(this, "font/OpenSans-Semibold.ttf");
		}
		setUseSystemDefault(false);
	}

	private int mTextPos = -1;

}