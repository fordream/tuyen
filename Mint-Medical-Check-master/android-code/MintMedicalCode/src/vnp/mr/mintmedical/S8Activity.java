package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.base.BaseItem;
import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

public class S8Activity extends MBaseActivity {
	ListView listView;

	@Override
	protected void onResume() {
		super.onResume();
//		resize.resizeSacle(listView, 300, LayoutParams.MATCH_PARENT);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HeaderView headerView = getView(R.id.activitymain_headerview);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		headerView.showButton(true, false);
		headerView.updateText(getHeaderRes());
		listView = getView(R.id.s8listview);
		listView.setAdapter(new BaseAdapter(this, getData(),
				new CommonGenderView() {

					@Override
					public CustomLinearLayoutView getView(Context arg0,
							Object arg1) {
						return new S8ItemView(arg0);
					}
				}));

		setupListView(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				S8GItem item = (S8GItem) parent.getItemAtPosition(position);
				if (item.intent != null) {
					try {
						startActivity(item.intent);
					} catch (Exception exception) {
						Toast.makeText(S8Activity.this,
								"There are no email clients installed.",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private List<Object> getData() {
		List<Object> list = new ArrayList<Object>();
		if (dbUserLogin.isLogin()) {
			S8GItem item = new S8GItem("My doctor",
					"Active membership required", null);
			list.add(item);
		}

		list.add(new S8GItem("One medical support",
				"Billing, general questions", createIntent(new String[] {
						"admin@mintmedical.sg", "admin@ilokun.com" },
						"Support", "")));
		list.add(new S8GItem("Feedback", "Tell us how you are doing",
				createIntent(new String[] { "feedback@mintmedical.sg",
						"feedback@ilokun.com" }, "Feedback", "")));
		list.add(new S8GItem("Share", "Refer a friend to Mint  Medical",
				createIntent(new String[] { "feedback@mintmedical.sg",
						"feedback@ilokun.com" },
						"Mint Medical Service – Register yours", "")));
		return list;
	}

	private Intent createIntent(String[] emalTo, String subject, String body) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, emalTo);
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, body);
		return i;
	}

	public int getLayout() {
		return R.layout.s8activity;
	}

	private class S8ItemView extends CustomLinearLayoutView {

		public S8ItemView(Context context) {
			super(context);
			init(R.layout.s8gitemview);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();

			resize.resizeSacle(findViewById(R.id.s4gitemview_content_main_1),
					300, LayoutParams.MATCH_PARENT);
			resize.setTextsize(findViewById(R.id.s4gitemview_textview1),
					MintUtils.TEXTSIZE_ITEM1);
			resize.setTextsize(findViewById(R.id.s4gitemview_textview2),
					MintUtils.TEXTSIZE_ITEM2);

			resize.resizeSacle(findViewById(R.id.s4gitemview_content_main),
					300, 60);

			resize.resizeSacle(findViewById(R.id.s4gitemview_content),
					300 - MintUtils.IMGITEM_WIDTH2, LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.s4gitemview_img2),
					MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		}

		@Override
		public void setGender() {
			S8GItem item = (S8GItem) getData();
			setText(R.id.s4gitemview_textview1, item.textHeader);
			setText(R.id.s4gitemview_textview2, item.textSubHeader);
		}

		private void setText(int res, String day) {
			((TextView) findViewById(res)).setText(day);
		}
	}

	public int getHeaderRes() {
		return R.string.contact;
	}

	public class S8GItem extends BaseItem {
		Intent intent;

		public S8GItem(String textHeader, String textSubHeader, Intent intent) {
			this.textHeader = textHeader;
			this.textSubHeader = textSubHeader;
			this.intent = intent;
		}

		public String textHeader = "Office Name";
		public String textSubHeader = "Office Address";
	}

}