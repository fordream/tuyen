package vnp.mr.mintmedical;

import com.viewpagerindicator.db.DBUserLogin;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.UserLogin;
import vnp.mr.mintmedical.view.AvatarView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class S9Activity extends MBaseActivity {

	public int getLayout() {
		return R.layout.s9activity;
	}

	AvatarView avatarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HeaderView headerView = (HeaderView) findViewById(R.id.activitymain_headerview);
		headerView.updateText(R.string.account);
		headerView.showButton(true, false);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		avatarView = getView(R.id.s9avatarView1);
		avatarView.setType(2);
		findViewById(R.id.s9button).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// CommonAndroid.callWeb(context, url)
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		resize.resizeSacle(findViewById(R.id.s9content_main), 300,LayoutParams.MATCH_PARENT);
		resize.resizeSacle(findViewById(R.id.s9main_name), 300,150);
		resize.resizeSacle(findViewById(R.id.s9_main_block2), 300,70 );
		resize.resizeSacle(findViewById(R.id.s9_main_block3), 300,70);
		resize.resizeSacle(findViewById(R.id.s9button), 300,45);
		resize.setTextsize(findViewById(R.id.s9texxt1),MintUtils.TEXTSIZE_ACCOUNT_HEADER);
		resize.setTextsize(findViewById(R.id.s9texxt2),MintUtils.TEXTSIZE_ACCOUNT1);
		resize.setTextsize(findViewById(R.id.s9texxt3),MintUtils.TEXTSIZE_ACCOUNT_HEADER);
		resize.setTextsize(findViewById(R.id.s9texxt4),MintUtils.TEXTSIZE_ACCOUNT1);
		resize.setTextsize(findViewById(R.id.s9texxt5),MintUtils.TEXTSIZE_ACCOUNT_HEADER);
		resize.setTextsize(findViewById(R.id.s9texxt6),MintUtils.TEXTSIZE_ACCOUNT1);
		resize.setTextsize(findViewById(R.id.s9button),MintUtils.TEXTSIZE_BUTTON);
		
		if(new DBUserLogin(this).isLogin()){
			UserLogin userLogin = (UserLogin) new DBUserLogin(this).getData();
			setText(R.id.s9texxt1, userLogin.custName);
			setText(R.id.s9texxt2, userLogin.custName);
			setText(R.id.s9texxt4, userLogin.custName);
			setText(R.id.s9texxt6, userLogin.email);
		}
	}

	private void setText(int s9texxt1, String custName) {
		((TextView)findViewById(s9texxt1)).setText(custName);
	}
}