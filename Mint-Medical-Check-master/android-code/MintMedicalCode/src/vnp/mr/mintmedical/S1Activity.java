package vnp.mr.mintmedical;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.service.MintService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.resizetextview.VNPConfigSizeTextView;
import com.vnp.core.common.resizetextview.VNPConfigSizeTextView.IVNPConfigSizeTextView;

public class S1Activity extends MBaseActivity implements
		IVNPConfigSizeTextView, OnPageChangeListener, OnClickListener {

	private PageIndicator mIndicator;
	private ViewPager pager;
	private Button button;
	private static final int RES[] = new int[] { R.drawable.s1a,
			R.drawable.s1b, R.drawable.s1c, R.drawable.s1d };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonAndroid.GPS.onpenGPS(this);
		MintService.startService(this);

		button = (Button) findViewById(R.id.spalshactivity_btn);

		button.setOnClickListener(this);
		pager = (ViewPager) findViewById(R.id.spalshactivity_viewpager);
		// pager.setOnPageChangeListener(this);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

		VNPConfigSizeTextView configSizeTextView = (VNPConfigSizeTextView) findViewById(R.id.vnpConfigSizeTextView);
		configSizeTextView.setIvnpConfigSizeTextView(this);
	}

	@Override
	public void onCompare() {

		findViewById(R.id.container).setVisibility(View.VISIBLE);

		View view = findViewById(R.id.spalshactivity_top);
		resize.resizeSacle(view, LayoutParams.MATCH_PARENT, 76);

		resize.resizeSacle(button, 80, 46);
		resize.setTextsize(button, MintUtils.TEXTSIZE_BUTTON);

		pager.setAdapter(new PagerAdapter() {
			public Object instantiateItem(View collection, int position) {
				ImageView view = new ImageView(S1Activity.this);
				view.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				view.setScaleType(ScaleType.FIT_XY);
				view.setBackgroundResource(RES[position]);
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
			public int getCount() {
				return RES.length;
			}
		});

		mIndicator.setViewPager(pager);
		mIndicator.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == RES.length - 1) {
			button.setBackgroundResource(R.drawable.done);
		} else {
			button.setBackgroundResource(R.drawable.nexr);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == button) {
			if (pager.getCurrentItem() == RES.length - 1) {
				startActivity(new Intent(this, S2Activity.class));
				finish();
			} else {
				pager.setCurrentItem(pager.getCurrentItem() + 1);
			}
		}
	}

	@Override
	public int getLayout() {
		return R.layout.splashactivity;
	}
}