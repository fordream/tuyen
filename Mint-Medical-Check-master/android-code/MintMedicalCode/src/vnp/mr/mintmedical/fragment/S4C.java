package vnp.mr.mintmedical.fragment;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MintUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.vnp.core.common.CommonAndroid;

public class S4C extends MBaseFragment implements OnClickListener {

	@Override
	public void onHiddenKeyBoard() {
		super.onHiddenKeyBoard();
		CommonAndroid.hiddenKeyBoard(getActivity());
	}
	
	public void onResume() {
		super.onResume();
		resize.resizeSacle(findViewById(R.id.s4cmaincontent), 300, LayoutParams.MATCH_PARENT);
		resize.resizeSacle(findViewById(R.id.textView1), 300, 50);
		
		resize.resizeSacle(findViewById(R.id.id1), 300, LayoutParams.WRAP_CONTENT);
		resize.resizeSacle(findViewById(R.id.s4cbtn), 300,45);
		resize.setTextsize(findViewById(R.id.s4cbtn), MintUtils.TEXTSIZE_BUTTON);
		
		resize.setTextsize(findViewById(R.id.textView1), MintUtils.TEXTSIZE_S4_HEADER);
		resize.setTextsize(findViewById(R.id.s4cet1), MintUtils.TEXTSIZE_EDITTEXT);
		findViewById(R.id.s4cbtn).setOnClickListener(this);
	}

	public int getLayout() {
		return R.layout.s4c;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.s4cbtn) {
			getOnClickListener().onClick(((EditText)findViewById(R.id.s4cet1)).getText().toString(), 1);
		}
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4c_header;
	}
}