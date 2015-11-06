/**
 * 
 */
package vnp.mr.mintmedical;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vnp.core.common.VNPResize;
import com.vnp.core.view.CustomLinearLayoutView;

/**
 * @author teemo
 * 
 */
public class HeaderView extends CustomLinearLayoutView {
	@Override
	public void setOnClickListener(OnClickListener l) {
		button.setOnClickListener(l);
	}

	private TextView textView;
	private Button button;
	private Button buttonBack;

	/**
	 * @param context
	 */
	public HeaderView(Context context) {
		super(context);
		init(R.layout.headerview);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.headerview);
	}

	@Override
	public void init(int res) {
		super.init(res);
		textView = getView(R.id.header_txt);
		button = getView(R.id.header_button);
		buttonBack = getView(R.id.header_button_back);
		buttonBack.setVisibility(GONE);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		try {
			VNPResize resize = VNPResize.getInstance();
			resize.resizeSacle(this,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, 45);

			if (scale) {

			}
			resize.resizeSacle(button, 58, 30);
			resize.resizeSacle(buttonBack, 58, 30);
			resize.setTextsize(textView, 25);
			resize.setTextsize(button, 15);

			if (scale) {
				resize.resizeSacle(textView, 300 - 60 * 2,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				textView.setSingleLine(true);
				textView.setEllipsize(TruncateAt.END);

			}
			resize.setTextsize(findViewById(R.id.radiolist), 15);
			resize.setTextsize(findViewById(R.id.radiomap), 15);

			resize.resizeSacle(findViewById(R.id.radiolist), 72, 30);
			resize.resizeSacle(findViewById(R.id.radiomap), 72, 30);
			// resize.resizeSacle(textView, 180, LayoutParams.WRAP_CONTENT);
			// textView.setSingleLine(true);
			// textView.setEllipsize(TruncateAt.END);
		} catch (Exception exception) {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vnp.core.view.CustomLinearLayoutView#setGender()
	 */
	@Override
	public void setGender() {

	}

	public void updateButton(boolean login) {
		if (login) {
			updateTextButtonRight(R.string.logout);
		} else {
			updateTextButtonRight(R.string.login);
		}
	}

	public void updateText(int login) {
		textView.setText(login);
	}

	public void showButton(boolean b, boolean c) {
		buttonBack.setVisibility(b ? VISIBLE : GONE);
		button.setVisibility(c ? VISIBLE : GONE);
	}

	public void setOnClickListenerButtonBack(OnClickListener onClickListener) {
		buttonBack.setOnClickListener(onClickListener);
	}

	public void showControlMap(RadioGroup.OnCheckedChangeListener changeListener) {
		RadioGroup radioGButton = (RadioGroup) findViewById(R.id.header_button_radiogroup);
		radioGButton.setVisibility(View.VISIBLE);
		radioGButton.setOnCheckedChangeListener(changeListener);
	}

	public void updateTextButtonRight(int cancel) {
		// button.setText(cancel);
		if (R.string.cancel == cancel) {
			button.setBackgroundResource(R.drawable.cacel);
		} else if (R.string.login == cancel) {
			button.setBackgroundResource(R.drawable.login);
		} else if (R.string.logout == cancel) {
			button.setBackgroundResource(R.drawable.loginout);
		} else if (R.string.s4new == cancel) {
			button.setBackgroundResource(R.drawable.btnnew);
		} else if (R.string.done == cancel) {
			button.setBackgroundResource(R.drawable.done);
		}
	}

	boolean scale = false;

	public void enableScale() {
		scale = true;
	}
}