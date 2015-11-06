package vnp.mr.mintmedical.fragment;

import java.util.ArrayList;
import java.util.List;

import vnp.mr.mintmedical.R;
import vnp.mr.mintmedical.base.MBaseFragment;
import vnp.mr.mintmedical.base.MintUtils;
import vnp.mr.mintmedical.item.VisitType;
import vnp.mr.mintmedical.view.OfficeItemView;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.viewpagerindicator.db.DBConfig;
import com.vnp.core.callback.ExeCallBack;
import com.vnp.core.callback.ExeCallBackOption;
import com.vnp.core.callback.ResClientCallBack;
import com.vnp.core.common.BaseAdapter;
import com.vnp.core.common.BaseAdapter.CommonGenderView;
import com.vnp.core.service.RestClient;
import com.vnp.core.view.CustomLinearLayoutView;

public class S4B extends MBaseFragment implements OnClickListener {
	ListView listView;

	public void onResume() {
		super.onResume();
		listView = (ListView) findViewById(R.id.s4bListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VisitType visitType = (VisitType)parent.getItemAtPosition(position);
				getOnClickListener().onClick(visitType.key, 2);
			}
		});
		resize.resizeSacle(findViewById(R.id.s4bmaincontent), 300,
				LayoutParams.MATCH_PARENT);
		findViewById(R.id.id1).setOnClickListener(this);
		findViewById(R.id.id2).setOnClickListener(this);
		findViewById(R.id.id3).setOnClickListener(this);
		resize.resizeSacle(findViewById(R.id.s4bListView), 300,
				LayoutParams.MATCH_PARENT);
		resize.resizeSacle(findViewById(R.id.textView1), 280, 50);
		resize.resizeSacle(findViewById(R.id.id1), 280, 70);
		resize.resizeSacle(findViewById(R.id.id2), 280, 70);
		resize.resizeSacle(findViewById(R.id.id3), 280, 70);

		resize.resizeSacle(findViewById(R.id.imageView1),
				MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		resize.resizeSacle(findViewById(R.id.imageView2),
				MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		resize.resizeSacle(findViewById(R.id.imageView3),
				MintUtils.IMGITEM_WIDTH2, MintUtils.IMGITEM_WIDTH2);
		resize.setTextsize(findViewById(R.id.textView1),
				MintUtils.TEXTSIZE_S4_HEADER);
		resize.setTextsize(findViewById(R.id.textView2),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.textView3),
				MintUtils.TEXTSIZE_ITEM2);
		resize.setTextsize(findViewById(R.id.textView4),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.textView41),
				MintUtils.TEXTSIZE_ITEM2);
		resize.setTextsize(findViewById(R.id.textView5),
				MintUtils.TEXTSIZE_ITEM1);
		resize.setTextsize(findViewById(R.id.textView51),
				MintUtils.TEXTSIZE_ITEM2);

		ExeCallBack exeCallBack = new ExeCallBack();
		exeCallBack.setExeCallBackOption(new ExeCallBackOption(getActivity(),
				true, R.string.loadding));
		exeCallBack.executeAsynCallBack(new ResClientCallBack() {
			@Override
			public void onCallBack(Object arg0) {
				RestClient restClient = (RestClient) arg0;
				DBConfig dbConfig = new DBConfig(getActivity());

				if (restClient.getResponseCode() == 200) {
					dbConfig.save(restClient.getResponse());
				}

				List<Object> list = new ArrayList<Object>();
				list.addAll(dbConfig.getVisitTypeList());
				setupListView(listView);
				listView.setAdapter(new BaseAdapter(getActivity(), list,
						new CommonGenderView() {

							@Override
							public CustomLinearLayoutView getView(Context arg0,
									Object arg1) {
								return new OfficeItemView(arg0);
							}
						}));
			}

			@Override
			public String getUrl() {
				return MintUtils.URL_VISITYPE;
			}
		});
	}

	public int getLayout() {
		return R.layout.s4b;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id1) {
			getOnClickListener().onClick(null, 1);
		} else if (v.getId() == R.id.id2) {
			getOnClickListener().onClick(null, 2);
		} else if (v.getId() == R.id.id3) {
			getOnClickListener().onClick(null, 31);
		}
	}

	@Override
	public int getHeaderRes() {
		return R.string.s4b_header;
	}
}