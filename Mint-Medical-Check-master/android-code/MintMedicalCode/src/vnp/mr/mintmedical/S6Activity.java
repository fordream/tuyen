package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

import vnp.mr.mintmedical.S2Activity.S2Item;
import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.view.S2ItemView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class S6Activity extends MBaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HeaderView headerView = (HeaderView) findViewById(R.id.activitymain_headerview);
		headerView.showButton(true, false);

		// headerView.updateTextButtonRight(R.string.cancel);
		headerView.updateText(R.string.s6header);
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
		// resize.resizeSacle(findViewById(R.id.s6content), 300,
		// LayoutParams.WRAP_CONTENT);

		listView = (ListView) findViewById(R.id.s6listview);
		setupListView(listView);

		List<Object> list = new ArrayList<Object>();
		templete(list);
		listView.setAdapter(new BaseAdapter(this, list, new CommonGenderView() {
			@Override
			public CustomLinearLayoutView getView(Context context, Object data) {
				return new S2ItemView(context);
			}
		}));

		setupListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				S6Item item = (S6Item) parent.getItemAtPosition(position);
				
				if(item.clazz!= null){
					startActivity(new Intent(S6Activity.this,item.clazz));
				}
			}
		});
	}

	private void templete(List<Object> list) {
		list.add(new S6Item(R.drawable.s61, getString(R.string.s6_1), null,
				S6CActivity.class));
		list.add(new S6Item(R.drawable.s62, getString(R.string.s6_2), null,
				S6BActivity.class));
		//list.add(new S6Item(R.drawable.s63, getString(R.string.s6_3), null,
		//		S6CActivity.class));

	}

	public class S6Item {
		public int res;
		public String header;
		public String suHeader;
		public Class clazz;

		public S6Item(int i, String string, String string2, Class clazz) {
			res = i;
			header = string;
			suHeader = string2;
			this.clazz = clazz;
		}

	}

	@Override
	public int getLayout() {
		return R.layout.s6activity;
	}

}