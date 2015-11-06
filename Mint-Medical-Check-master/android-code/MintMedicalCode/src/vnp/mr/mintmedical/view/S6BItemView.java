package vnp.mr.mintmedical.view;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MintUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vnp.core.common.VNPResize;
import com.vnp.core.view.CustomLinearLayoutView;

public class S6BItemView extends CustomLinearLayoutView {

	public S6BItemView(Context context) {
		super(context);
		init(R.layout.s6bitemview);
	}

	public S6BItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.s6bitemview);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		try {
			VNPResize resize = VNPResize.getInstance();
			resize.resizeSacle(this,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, 50);
			resize.resizeSacle(findViewById(R.id.s6bcheckbok), 40, 40);
			resize.resizeSacle(findViewById(R.id.s6bitemtext), 260,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			resize.setTextsize(findViewById(R.id.s6bitemtext),
					MintUtils.TEXTSIZE_ITEM1);
		} catch (Exception exception) {

		}
	}

	@Override
	public void setGender() {
	}

	public void update(int s6b1) {
		((TextView) findViewById(R.id.s6bitemtext)).setText(s6b1);
	}
}
