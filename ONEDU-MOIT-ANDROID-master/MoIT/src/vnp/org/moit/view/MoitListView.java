package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.MoitListView
public class MoitListView extends LinearLayout {
	private TextView empty_txt;
	private ListView forum_list;

	public MoitListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MoitListView(Context context) {
		super(context);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.list, this);
		forum_list = CommonAndroid.getView(this, R.id.forum_list);
		empty_txt = CommonAndroid.getView(this, R.id.empty_txt);
		empty_txt.setVisibility(View.GONE);
	}

	public void setText(int res) {
		empty_txt.setText(res);
	}

	private CursorAdapter adapter;

	public void setAdapter(CursorAdapter adapter) {
		this.adapter = adapter;
		forum_list.setAdapter(adapter);
	}

	public final void filter(CharSequence constraint) {
		if (adapter != null)
			adapter.getFilter().filter(constraint);
	}

	public void setSelection(int i) {
		forum_list.setSelection(i);
	}

	public View getEmptyMView() {
		return empty_txt;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		forum_list.setOnItemClickListener(onItemClickListener);
	}

	public void setOnClickListenerForCreateTopic(OnClickListener onClickListener) {
		empty_txt.setOnClickListener(onClickListener);
	}
}