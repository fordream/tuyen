package vnp.mr.mintmedical.fragment;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MintUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class S4F extends MBaseFragment implements OnClickListener {

	@Override
	public void onResume() {
		super.onResume();

		resize.resizeSacle(findViewById(R.id.s4fcontent), 574/2,560/2);
		//resize.resizeSacle(findViewById(R.id.s4fimageView1), 100, 100);
		//resize.setTextsize(findViewById(R.id.textView1), MintUtils.TEXTSIZE_ITEM1);
		//resize.setTextsize(findViewById(R.id.textView2), MintUtils.TEXTSIZE_ITEM2);
		//resize.setTextsize(findViewById(R.id.textView3), MintUtils.TEXTSIZE_ITEM2);
		//((TextView)findViewById(R.id.textView2)).setText(getTextContentRes());
	}

	public int getTextContentRes() {
		return R.string.s4f2;
	}

	@Override
	public int getLayout() {
		return R.layout.s4f;
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4f_header;
	}

}