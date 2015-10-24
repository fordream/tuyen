package vnp.org.moit.custom;

import one.edu.vn.sms.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.custom.CustomMenuRadioButton
public class CustomMenuRadioButton extends RadioButton {
	public CustomMenuRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);

//		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);
//
//		try {
//			mTextPos = a.getInteger(R.styleable.CustomTextView_txtCustomType, -1);
//		} finally {
//			a.recycle();
//		}
//		if (mTextPos == 0) {
//			CommonAndroid.FONT.getInstance().setTypeFace(this, "font/OpenSans-Bold.ttf");
//		} else if (mTextPos == 1) {
//			CommonAndroid.FONT.getInstance().setTypeFace(this, "font/OpenSans-Regular.ttf");
//		} else if (mTextPos == 2) {
//			CommonAndroid.FONT.getInstance().setTypeFace(this, "font/OpenSans-Semibold.ttf");
//		}
		// setUseSystemDefault(false);
	}

	private int mTextPos = -1;

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		try {
			if (isChecked()) {
				setTextColor(getContext().getResources().getColor(R.color.color_choose));
			} else {
				setTextColor(getContext().getResources().getColor(R.color.color_unchoose_menu));
			}
		} catch (Exception exception) {
		}
	}

}