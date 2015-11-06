package vnp.mr.mintmedical;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.view.S6BItemView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class S6BActivity extends MBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HeaderView headerView = (HeaderView) findViewById(R.id.activitymain_headerview);
		headerView.showButton(true, false);
		headerView.updateText(R.string.s6_1);
		headerView.enableScale();
		headerView.setOnClickListenerButtonBack(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		update(R.id.s6bitem1, R.string.s6b1);
		update(R.id.s6bitem2, R.string.s6b2);
		update(R.id.s6bitem3, R.string.s6b3);
		update(R.id.s6bitem4, R.string.s6b4);
		update(R.id.s6bitem5, R.string.s6b5);
		update(R.id.s6bitem6, R.string.s6b6);
	}

	private void update(int s6bitem1, int s6b1) {
		((S6BItemView) findViewById(s6bitem1)).update(s6b1);
	}

	@Override
	protected void onResume() {
		super.onResume();
		resize.resizeSacle(findViewById(R.id.s6bcontent), 300,LayoutParams.WRAP_CONTENT);
		resize.resizeSacle(findViewById(R.id.s6bbutton), 300, 45);
		resize.resizeSacle(findViewById(R.id.s6btextview), 300, 60);
		resize.setTextsize(findViewById(R.id.s6btextview), MintUtils.TEXTSIZE_ITEM1);
	}

	@Override
	public int getLayout() {
		return R.layout.s6bactivity;
	}

}