package one.edu.vn.sms;

import java.util.ArrayList;
import java.util.List;

import vnp.org.moit.view.BangDiemViewNew;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.facebook.avnp.BaseFacebookActivity;
import com.facebook.avnp.Facebook415Utils.IFacebookListener;
import com.vnp.core.common.CommonAndroid;

public class MainActivity extends BaseFacebookActivity {
	public static MainActivity instance;
	private com.vnp.core.base.NoScollViewPager home_content;
	private BaseViewPagerAdapter baseViewPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.main);

		// ListView listView = CommonAndroid.getView(this, R.id.listView1);
		// NewFeedAdapter adapter = null;
		// listView.setAdapter(adapter = new NewFeedAdapter(this, null));
		// adapter.getFilter().filter("");

		home_content = CommonAndroid.getView(this, R.id.home_content);
		// home_content.setEnableScroll(false);
		home_content
				.setAdapter(baseViewPagerAdapter = new BaseViewPagerAdapter(
						this));
		CommonAndroid.viewPagerOrListViewSetSlowChangePage(home_content, 1000);
	}

	@Override
	public IFacebookListener createIFacebookListener() {
		return null;
	}

	public class BaseViewPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public BaseViewPagerAdapter(Context context) {
			super();
			// list.add(new NewFeedView(context));
			// list.add(new InboxView(context));
			list.add(new BangDiemViewNew(context));
			// list.add(new ForumView(context));
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			View data = list.get(position);
			((ViewPager) collection).addView(data);
			return data;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}