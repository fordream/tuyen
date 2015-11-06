package vnp.mr.mintmedical.view;

// vnp.mr.mintmedical.view.ListViewHeader
import vnp.mr.mintmedical.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.common.VNPResize;
import com.vnp.core.view.CustomLinearLayoutView;

public class ListViewHeader extends CustomLinearLayoutView {
	private ListView listView;
	private TextView textView;

	public ListViewHeader(Context context) {
		super(context);
		init(R.layout.listviewheader);
	}

	public ListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(R.layout.listviewheader);
	}
	public void setupListView(View view) {
		if (view != null && view instanceof ListView) {
			ListView listView = (ListView) view;
			listView.setDivider(null);
			listView.setSelector(R.drawable.listview_selected);
		}
	}
	@Override
	public void init(int res) {
		super.init(res);
		listView = (ListView) findViewById(R.id.listviewheader_listView);
		setupListView(listView);
		textView = getView(R.id.listviewheader_textview);

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				try {
					String name = listView.getAdapter()
							.getItem(firstVisibleItem).toString();
					if (textView != null)
						textView.setText(name);
				} catch (Exception exception) {
				}
			}
		});
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		try {
			VNPResize resize = VNPResize.getInstance();
			resize.resizeSacle(textView,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, 35);
			resize.setTextsize(textView, 15);
		} catch (Exception ex) {
		}
	}

	@Override
	public void setGender() {

	}

	public void setAdapter(ListAdapter baseAdapter) {
		listView.setAdapter(baseAdapter);
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener){
		
		listView.setOnItemClickListener(clickListener);
	}
}