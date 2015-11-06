package vnp.mr.mintmedical;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.fragment.S4F;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.vnp.core.common.CommonAndroid;

public class S5Activity extends MBaseActivity {

	@Override
	public int getLayout() {
		return R.layout.s5activity;
	}

	private MBaseFragment baseFagment;
	HeaderView headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		headerView = (HeaderView) findViewById(R.id.activitymain_headerview);
		headerView.showButton(false, true);

		headerView.updateTextButtonRight(R.string.cancel);
		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		baseFagment = new S51();
		changeFragemtn(R.id.s5_content_main, baseFagment);
	}

	@Override
	public void changeFragemtn(int r_id_content_frame, Fragment rFragment) {
		super.changeFragemtn(r_id_content_frame, rFragment);
		if (baseFagment != null)
			headerView.updateText(baseFagment.getHeaderRes());
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@SuppressLint("ValidFragment")
	private class S51 extends MBaseFragment implements OnClickListener {
		@Override
		public int getLayout() {
			return R.layout.s51;
		}

		@Override
		public void onHiddenKeyBoard() {
			super.onHiddenKeyBoard();
			CommonAndroid.hiddenKeyBoard(getActivity());
		}

		@Override
		public void onResume() {
			super.onResume();
			resize.resizeSacle(findViewById(R.id.s5_content), 300,LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.textView1), 280, 50);
			//resize.resizeSacle(findViewById(R.id.s4cet1), 300, 100);
			resize.resizeSacle(findViewById(R.id.id1), 280, 100);
			
			resize.resizeSacle(findViewById(R.id.s4cbtn), 580/2,45);
			resize.setTextsize(findViewById(R.id.s4cbtn), MintUtils.TEXTSIZE_BUTTON);
			
			resize.setTextsize(findViewById(R.id.textView1), MintUtils.TEXTSIZE_S4_HEADER);
			resize.setTextsize(findViewById(R.id.s4cet1), MintUtils.TEXTSIZE_EDITTEXT);
			
			findViewById(R.id.s4cbtn).setOnClickListener(this);
		}

		@Override
		public int getHeaderRes() {
			return R.string.s5header;
		}

		@Override
		public void onClick(View v) {
			baseFagment = new S52();
			changeFragemtn(R.id.s5_content_main, baseFagment);
			
			onHiddenKeyBoard();
			headerView.updateTextButtonRight(R.string.done);
		}
	}

	@SuppressLint("ValidFragment")
	private class S52 extends S4F {
		//@Override
		//public int getTextContentRes() {
		//	return R.string.s5face2;
		//}
	}
}