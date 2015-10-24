package vnp.org.moit.home;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.vnp.core.base.NoScollViewPager;
import com.vnp.core.common.CommonAndroid;

public class HomePager extends NoScollViewPager {

	public HomePager(Context context) {
		super(context);
		setEnableScroll(false);
		CommonAndroid.viewPagerOrListViewSetSlowChangePage(this, 500);
	}

	public HomePager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEnableScroll(false);
		CommonAndroid.viewPagerOrListViewSetSlowChangePage(this, 500);
	}

	public class ViewPagerAdapter extends PagerAdapter {

		Activity activity;
		int imageArray[];

		public ViewPagerAdapter(Activity act, int[] imgArra) {
			imageArray = imgArra;
			activity = act;
		}

		public int getCount() {
			return imageArray.length;
		}

		public Object instantiateItem(View collection, int position) {
			ImageView view = new ImageView(activity);
			view.setScaleType(ScaleType.FIT_XY);
			view.setBackgroundResource(imageArray[position]);
			((ViewPager) collection).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}