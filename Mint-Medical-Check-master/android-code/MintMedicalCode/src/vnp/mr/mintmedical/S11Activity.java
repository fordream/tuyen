package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.Gcm;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.viewpagerindicator.db.DBGcm;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

public class S11Activity extends MBaseActivity implements OnItemClickListener {
	private HeaderView headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		headerView = getView(R.id.s11headerview);
		headerView.updateText(R.string.s11header);
		headerView.showButton(true, true);
		headerView.updateTextButtonRight(R.string.cancel);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView listView = getView(R.id.s11listview);
		listView.setOnItemClickListener(this);

		setupListView(listView);

		List<Object> list = (List<Object>) new DBGcm(this).getData();
		
		Gcm gcm = new Gcm();
		
		gcm.message ="Please tell me the card idea this year does not inclue us in ugly, ";
		gcm.name ="Admin";
		gcm.time = "14/06/2014";
		gcm.type ="1";
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		list.add(gcm);
		
		listView.setAdapter(new BaseAdapter(this, list, new CommonGenderView() {

			@Override
			public CustomLinearLayoutView getView(Context context, Object data) {
				return new S11ItemView(context);
			}
		}));
	}

	@Override
	public int getLayout() {
		return R.layout.s11activity;
	}

	private class S11ItemView extends CustomLinearLayoutView {

		public S11ItemView(Context context) {
			super(context);
			init(R.layout.s11itemview);
		}

		public S11ItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(R.layout.s11itemview);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			//resize.resizeSacle(this, LayoutParams.MATCH_PARENT, 70);
			resize.resizeSacle(findViewById(R.id.s11itemimg), 13, 13);
			resize.resizeSacle(findViewById(R.id.s11itemview_m1), 300,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s11itemcontent), 287,
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
			if (getData() instanceof Gcm) {
				Gcm item = (Gcm) getData();
				((TextView) findViewById(R.id.s11itemview_text1))
						.setText(item.name);
				((TextView) findViewById(R.id.s11itemview_text2))
						.setText(item.time);
				((TextView) findViewById(R.id.s11itemview_text3))
						.setText(item.message);
			}
		}
	}

	private class Item {
		int res;
		String header;
		String suHeader;
		Intent intent;

		public Item(int i, String string, String string2, Intent intent) {
			res = i;
			header = string;
			this.intent = intent;
			suHeader = string2;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// Item item = (Item) parent.getItemAtPosition(position);
		// startActivity(item.intent);
	}
}