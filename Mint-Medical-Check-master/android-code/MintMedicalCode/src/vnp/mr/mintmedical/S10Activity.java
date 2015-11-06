package vnp.mr.mintmedical;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.base.MBaseActivity;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.service.MintService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.db.DBUserLogin;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.view.CustomLinearLayoutView;

public class S10Activity extends MBaseActivity implements OnItemClickListener {
	private HeaderView headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		headerView = getView(R.id.aboutactivity_headerview);
		headerView.updateText(R.string.about);
		headerView.showButton(true, false);
		headerView.setOnClickListenerButtonBack(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ListView listView = getView(R.id.aboutactivity_listview);
		listView.setOnItemClickListener(this);

		setupListView(listView);
		listView.setAdapter(new BaseAdapter(this, getlData(),
				new CommonGenderView() {

					@Override
					public CustomLinearLayoutView getView(Context context,
							Object data) {
						return new AboutActivityItemView(context);
					}
				}));

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.s10a_1);
		((ImageView) findViewById(R.id.aboutactivity_img))
				.setImageBitmap(bitmap);
	}

	private List<Object> getlData() {
		List<Object> data = new ArrayList<Object>();

		data.add(new Item(0, "What We Offer", null, new Intent(this,
				S10AActivity.class)));

		data.add(new Item(1, "Location", "", new Intent(this, S7Activity.class)));

		return data;
	}

	@Override
	protected void onResume() {
		super.onResume();
		resize.resizeSacle(findViewById(R.id.aboutactivity_img),
				LayoutParams.MATCH_PARENT, 150);
		resize.resizeSacle(findViewById(R.id.s10maon_content), 300,
				LayoutParams.WRAP_CONTENT);

		resize.resizeSacle(findViewById(R.id.s10a_textview1),
				LayoutParams.MATCH_PARENT, 40);
		resize.resizeSacle(findViewById(R.id.s10a_textview2),
				LayoutParams.MATCH_PARENT, 80);
		resize.setTextsize(findViewById(R.id.s10a_textview1),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.s10a_textview2),
				MintUtils.TEXTSIZE_ITEM2);

	}

	@Override
	public int getLayout() {
		return R.layout.aboutativity;
	}

	private class AboutActivityItemView extends CustomLinearLayoutView {

		public AboutActivityItemView(Context context) {
			super(context);
			init(R.layout.s10teimview);
		}

		public AboutActivityItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(R.layout.s10teimview);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();

			resize.resizeSacle(findViewById(R.id.aboutactivityitemview_ll1),
					300, 60);
			resize.resizeSacle(findViewById(R.id.s10item_content), 300,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			resize.resizeSacle(findViewById(R.id.aboutactivityitemview_img2),
					13, 13);
			resize.resizeSacle(findViewById(R.id.aboutactivityitemview_text1),
					287, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			resize.setTextsize(findViewById(R.id.aboutactivityitemview_text1),
					MintUtils.TEXTSIZE_ITEM1);
		}

		@Override
		public void setGender() {
			if (getData() instanceof Item) {
				Item item = (Item) getData();
				((TextView) findViewById(R.id.aboutactivityitemview_text1))
						.setText(item.header);
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

		Item item = (Item) parent.getItemAtPosition(position);
		if (item.res == 1) {
			DBUserLogin dbUserLogin = new DBUserLogin(this);
			if (dbUserLogin.isLogin()) {
				startActivity(item.intent);
			} else {
				DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// headerView.updateButton(dbUserLogin.isLogin());
						// MintService.startService(S2Activity.this);
						Toast.makeText(S10Activity.this, "Login Sucess", Toast.LENGTH_SHORT).show();
					}
				};
				new S3Activity(this, onClick).show();
			}

		} else {
			startActivity(item.intent);
		}
	}
}