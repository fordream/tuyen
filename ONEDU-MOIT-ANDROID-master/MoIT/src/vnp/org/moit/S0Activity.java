package vnp.org.moit;

import java.util.ArrayList;
import java.util.List;
import vnp.org.moit.view.S1View;
import vnp.org.moit.view.S2View;
import vnp.org.moit.view.S3View;
import vnp.org.moit.view.SplashView;
import one.edu.vn.sms.R;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.vnp.core.activity.BaseActivity;
import com.vnp.core.base.NoScollViewPager;
import com.vnp.core.common.CommonAndroid;

public class S0Activity extends BaseActivity {
	private NoScollViewPager noScollViewPager;
	private BaseViewPagerAdapter pagerAdapter;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s0);
		noScollViewPager = CommonAndroid.getView(this, R.id.noscrollviewpager);
		noScollViewPager.setEnableScroll(false);
		noScollViewPager.setAdapter(pagerAdapter = new BaseViewPagerAdapter(this));
		CommonAndroid.viewPagerOrListViewSetSlowChangePage(noScollViewPager, 1000);
		
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				noScollViewPager.setCurrentItem(3);
			}
		}, 3000);
	}

	public class BaseViewPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public BaseViewPagerAdapter(Context context) {
			super();
			list.add(new SplashView(context));
			list.add(new S1View(context));
			list.add(new S2View(context));
			list.add(new S3View(context));
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
