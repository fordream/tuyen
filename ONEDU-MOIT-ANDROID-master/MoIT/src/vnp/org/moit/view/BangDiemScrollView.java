package vnp.org.moit.view;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.widget.CursorAdapter;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BangDiemScrollView extends LinearLayout implements Runnable {
	// vnp.org.moit.view.BangDiemScrollView
	private Handler handler = new Handler();

	public BangDiemScrollView(Context context) {
		super(context);
	}

	public BangDiemScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BangDiemScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private android.widget.CursorAdapter adapter;

	public void setAdapter(android.widget.CursorAdapter adapter) {
		this.adapter = adapter;
		handler.postDelayed(this, 100);

	}

	@Override
	public void run() {
		this.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			addView(adapter.getView(i, null, this));
		}
	}
}
