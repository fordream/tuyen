package vnp.mr.mintmedical.view;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.S4Activity;
import vnp.mr.mintmedical.S2Activity.S2Item;
import vnp.mr.mintmedical.S6Activity.S6Item;
import vnp.mr.mintmedical.base.MintUtils;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.db.DBHome;
import com.viewpagerindicator.db.DBUserLogin;
import com.vnp.core.common.VNPResize;
import com.vnp.core.view.CustomLinearLayoutView;

public class S2ItemView extends CustomLinearLayoutView {
	DBUserLogin dbUserLogin;
	DBHome dbHome;

	public S2ItemView(Context context) {
		super(context);
		init(R.layout.mainactivityitemview);
	}

	public S2ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.mainactivityitemview);
	}

	@Override
	public void init(int res) {
		super.init(res);
		dbUserLogin = new DBUserLogin(getContext());
		dbHome = new DBHome(getContext());
		setWillNotDraw(false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getData() instanceof S2Item) {
			S2Item item = (S2Item) getData();
			TextView textView = ((TextView) findViewById(R.id.mainactivityitemview_text2));
			if (item.res == R.drawable.main_img_1 && dbHome != null) {
				textView.setText(dbHome.getAppointments());
			} else if (R.drawable.main_img_3 == item.res) {
				textView.setText(dbHome.getPrescription());
			} else if (R.drawable.main_img_8 == item.res) {
				textView.setText(dbHome.getNewAndAlerts());
			}
		}
		invalidate();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		VNPResize resize = VNPResize.getInstance();
		resize.resizeSacle(findViewById(R.id.s11itemview_m1), 300, 70);
		resize.resizeSacle(findViewById(R.id.mainactivityitemview_img1),
				MintUtils.IMGITEM_WIDTH, MintUtils.IMGITEM_WIDTH);
		resize.resizeSacle(findViewById(R.id.mainactivityitemview_img2),
				MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		resize.resizeSacle(findViewById(R.id.mainactivityitemview_text1), 220,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		resize.resizeSacle(findViewById(R.id.mainactivityitemview_text2), 220,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		resize.setTextsize(findViewById(R.id.mainactivityitemview_text1),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.mainactivityitemview_text2),
				MintUtils.TEXTSIZE_ITEM2);
	}

	@Override
	public void setGender() {
		if (getData() instanceof S2Item) {
			S2Item item = (S2Item) getData();
			((TextView) findViewById(R.id.mainactivityitemview_text1))
					.setText(item.header);
			((TextView) findViewById(R.id.mainactivityitemview_text2))
					.setText(item.suHeader);
			findViewById(R.id.mainactivityitemview_img1).setBackgroundResource(
					item.res);

			if (item.suHeader == null) {
				findViewById(R.id.mainactivityitemview_text2).setVisibility(
						View.GONE);
			} else {
				findViewById(R.id.mainactivityitemview_text2).setVisibility(
						View.VISIBLE);
			}
		} else if (getData() instanceof S6Item) {
			S6Item item = (S6Item) getData();
			TextView textView = (TextView) findViewById(R.id.mainactivityitemview_text1);
			textView.setText(item.header);

			textView.setSingleLine(false);
			textView.setMaxLines(2);
			textView.setEllipsize(TruncateAt.END);

			((TextView) findViewById(R.id.mainactivityitemview_text2))
					.setText(item.suHeader);
			findViewById(R.id.mainactivityitemview_img1).setBackgroundResource(
					item.res);

			if (item.suHeader == null) {
				findViewById(R.id.mainactivityitemview_text2).setVisibility(
						View.GONE);
			} else {

				findViewById(R.id.mainactivityitemview_text2).setVisibility(
						View.VISIBLE);
			}
		}
	}
}
