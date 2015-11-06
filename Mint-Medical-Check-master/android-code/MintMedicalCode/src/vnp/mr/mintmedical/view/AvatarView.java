package vnp.mr.mintmedical.view;

// vnp.mr.mintmedical.view.ListViewHeader
import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MintUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;

import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.ImageLoaderUtils;
import com.vnp.core.common.VNPResize;
import com.vnp.core.view.CustomLinearLayoutView;

public class AvatarView extends CustomLinearLayoutView {

	public AvatarView(Context context) {
		super(context);
		init(R.layout.avatarview);
	}

	public AvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(R.layout.avatarview);
	}

	@Override
	public void init(int res) {
		super.init(res);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.avatar);
		((ImageView) findViewById(R.id.avartarview_imageView1))
				.setImageBitmap(bitmap);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		VNPResize resize = VNPResize.getInstance();
		int x = 1;
		if (type == 0) {
			x = 1;
		} else if (type == 1) {
			x = 2;
		} else if (type == 2) {
			x = 2;
		}

		if (type == 3) {
			int width = MintUtils.IMGITEM_WIDTH - 10;
			resize.resizeSacle(this, width, width);
			resize.resizeSacle(findViewById(R.id.avartarview_imageView1),
					(width - 10), (width - 10));
			return;
		} else if (type == 4) {
			int width = MintUtils.IMGITEM_WIDTH - 10;
			resize.resizeSacle(this, width, width);
			resize.resizeSacle(findViewById(R.id.avartarview_imageView1),
					(width - 5), (width - 5));
			return;
		} else if (type == 5) {
			int width = 60;
			resize.resizeSacle(this, width, width);
			resize.resizeSacle(findViewById(R.id.avartarview_imageView1),
					(width - 5), (width - 5));
			return;
		}
		resize.resizeSacle(this, MintUtils.IMGITEM_WIDTH * x,
				MintUtils.IMGITEM_WIDTH * x);
		resize.resizeSacle(findViewById(R.id.avartarview_imageView1),
				(MintUtils.IMGITEM_WIDTH - 6) * x,
				(MintUtils.IMGITEM_WIDTH - 6) * x);
	}

	@Override
	public void setGender() {

	}

	/**
	 * 
	 * @param url
	 */
	public void loadAvartar(int res) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
		((ImageView) findViewById(R.id.avartarview_imageView1))
				.setImageBitmap(bitmap);
	}

	/**
	 * 
	 * @param url
	 */
	public void loadAvartar(String url) {
		if (url != null)
			ImageLoaderUtils.getInstance(getContext()).DisplayImage(url,
					(ImageView) findViewById(R.id.avartarview_imageView1));
	}

	public void onHiddenKeyBoard() {
		CommonAndroid.hiddenKeyBoard((Activity) getContext());
	}

	int type = 0;

	public void setType(int i) {
		type = i;
	}

}