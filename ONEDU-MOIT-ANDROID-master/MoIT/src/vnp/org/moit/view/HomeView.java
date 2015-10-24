package vnp.org.moit.view;

import one.edu.vn.sms.R;
import one.edu.vn.sms.S1S2S3Activity;
import vnp.org.moit.service.EduSevice;
import vnp.org.moit.service.EduSevice.TypeMore;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.HomeView
public class HomeView extends LinearLayout {
	private ForumView home_forum;
	private InboxView home_inbox;
	private NewFeedView home_newfeed;
	private BangDiemView homeBangDiem;

	public HomeView(Context context) {
		super(context);

		init();
	}

	public HomeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		try {
			CommonAndroid.getView(getContext(), R.layout.home, this);
			home_forum = CommonAndroid.getView(this, R.id.home_forum);
			home_inbox = CommonAndroid.getView(this, R.id.home_inbox);
			home_newfeed = CommonAndroid.getView(this, R.id.newfeed);
			homeBangDiem = CommonAndroid.getView(this, R.id.bangdiem);
			RadioGroup group = CommonAndroid.getView(this, R.id.home_menu);
			group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					home_inbox.setVisibility(((RadioButton) findViewById(R.id.home_menu_1)).isChecked());
					home_newfeed.setVisibility(((RadioButton) findViewById(R.id.home_menu_2)).isChecked());
					home_forum.setVisibility(((RadioButton) findViewById(R.id.home_menu_3)).isChecked());
					homeBangDiem.setVisibility(((RadioButton) findViewById(R.id.home_menu_4)).isChecked());
					loadMore();
				}
			});
		} catch (Exception ex) {
		}
	}

	private void loadMore() {
		Intent intent = new Intent(getContext(), EduSevice.class);
		intent.putExtra("api", "loadmore");
		getContext().startService(intent);
	}

	public void loadFirstData(boolean isByNotifycation) {
		if (isByNotifycation) {
			((RadioButton) findViewById(R.id.home_menu_1)).setChecked(true);
			home_inbox.reloadToFirst();
			homeBangDiem.reloadToFirst();
		} else {
			home_newfeed.setVisibility(((RadioButton) findViewById(R.id.home_menu_2)).isChecked());
		}

	}

	public void setActivity(S1S2S3Activity s1s2s3Activity) {
		home_inbox.setActivity(s1s2s3Activity);
		home_newfeed.setActivity(s1s2s3Activity);
		home_forum.setActivity(s1s2s3Activity);
		homeBangDiem.setActivity(s1s2s3Activity);
	}

	public void updateData(TypeMore typeMore, String id) {
		home_inbox.updateData(typeMore, id);
		home_newfeed.updateData(typeMore, id);
		home_forum.updateData(typeMore, id);
		homeBangDiem.updateData(typeMore, id);
	}

	public void updateTopic() {
		home_forum.updateData();
	}
}