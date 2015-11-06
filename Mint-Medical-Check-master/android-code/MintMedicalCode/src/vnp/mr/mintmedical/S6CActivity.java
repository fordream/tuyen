package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.Gcm;
import vnp.mr.mintmedical.item.S6cItem;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

public class S6CActivity extends MBaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HeaderView headerView = (HeaderView) findViewById(R.id.activitymain_headerview);
		headerView.showButton(true, false);
		headerView.enableScale();
		headerView.updateText(R.string.s6_1);
		headerView.setOnClickListenerButtonBack(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		listView = (ListView) findViewById(R.id.s6listview);
		setupListView(listView);

		listView.setAdapter(new BaseAdapter(this, getData(),
				new CommonGenderView() {

					@Override
					public CustomLinearLayoutView getView(Context context,
							Object data) {
						return new S6cItemView(context);
					}
				}));

		setupListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// S6Item item = (S6Item) parent.getItemAtPosition(position);
				// if (item.res == R.drawable.main_img_7
				// || item.res == R.drawable.main_img_4) {
				// } else {
				// }
			}
		});
	}

	private List<Object> getData() {

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < 20; i++) {
			S6cItem item = new S6cItem();
			item.time = "2014-12-31";
			item.name = "Request approved. Please";
			list.add(item);
		}
		return list;
	}

	@Override
	public int getLayout() {
		return R.layout.s6activity;
	}

	private class S6cItemView extends CustomLinearLayoutView {

		public S6cItemView(Context context) {
			super(context);
			init(R.layout.s6citemview);
		}

		public S6cItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(R.layout.s6citemview);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			resize.resizeSacle(findViewById(R.id.s11itemimg), 0, 0);
			resize.resizeSacle(findViewById(R.id.s11itemview_m1), 300,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s11itemcontent), 300,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			resize.setTextsize(findViewById(R.id.s11itemview_text1),
					MintUtils.TEXTSIZE_ITEM1);
			resize.setTextsize(findViewById(R.id.s11itemview_text2),
					MintUtils.TEXTSIZE_ITEM2);
			resize.setTextsize(findViewById(R.id.s11itemview_text3),
					MintUtils.TEXTSIZE_ITEM1);
		}

		@Override
		public void setGender() {
			if (getData() instanceof S6cItem) {
				S6cItem item = (S6cItem) getData();
				((TextView) findViewById(R.id.s11itemview_text1))
						.setText(item.time);
				((TextView) findViewById(R.id.s11itemview_text2)).setText("");
				((TextView) findViewById(R.id.s11itemview_text3))
						.setText(item.name);
			}
		}
	}

}