package vnp.mr.mintmedical.fragment;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.view.AvatarView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

public class S4A extends MBaseFragment implements OnClickListener {

	public S4A() {
		super();
	}

	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.id1).setOnClickListener(this);
		findViewById(R.id.id2).setOnClickListener(this);
		findViewById(R.id.id3).setOnClickListener(this);
		
		resize.resizeSacle(findViewById(R.id.s4a_main_content), 300, LayoutParams.MATCH_PARENT);
		
		resize.resizeSacle(findViewById(R.id.textView1), 300, 50);
		
		resize.resizeSacle(findViewById(R.id.id1), 287 -50, 70);
		resize.resizeSacle(findViewById(R.id.id2), 287 -50, 70);
		resize.resizeSacle(findViewById(R.id.id3), 287 -50, 70);
		
		resize.resizeSacle(findViewById(R.id.imageView1), MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		resize.resizeSacle(findViewById(R.id.imageView2), MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		resize.resizeSacle(findViewById(R.id.imageView3), MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);

		resize.setTextsize(findViewById(R.id.textView1), MintUtils.TEXTSIZE_S4_HEADER);
		resize.setTextsize(findViewById(R.id.textView2), MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.textView3), MintUtils.TEXTSIZE_ITEM2);
		resize.setTextsize(findViewById(R.id.textView4), MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.textView5), MintUtils.TEXTSIZE_ITEM1);
		
		AvatarView avatarView = (AvatarView)findViewById(R.id.avatarView1);
		avatarView.setType(4);
		avatarView.loadAvartar(R.drawable.s4a1);
		AvatarView avatarView1 = (AvatarView)findViewById(R.id.avatarView2);
		avatarView1.setType(4);
		avatarView1.loadAvartar(R.drawable.s4a2);
		AvatarView avatarView3 = (AvatarView)findViewById(R.id.avatarView3);
		avatarView3.setType(4);
		avatarView3.loadAvartar(R.drawable.s4a3);
	}

	public int getLayout() {
		return R.layout.s4aactivity;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id1) {
			// startActivity(new Intent(this, S4BActivity.class));
			getOnClickListener().onClick(null, 1);
		} else if (v.getId() == R.id.id2) {
			// startActivity(new Intent(this, S4GActivity.class));
			getOnClickListener().onClick(null, 2);
		} else if (v.getId() == R.id.id3) {
			getOnClickListener().onClick(null, 3);
			// startActivity(new Intent(this, S4HActivity.class));
		}
		// dismiss();
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4a_header;
	}

}