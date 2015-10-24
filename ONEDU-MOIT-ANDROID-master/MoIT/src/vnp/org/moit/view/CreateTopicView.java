package vnp.org.moit.view;

import java.util.ArrayList;
import java.util.List;

import vnp.org.moit.CreateTopicActivity;
import one.edu.vn.sms.R;
import one.edu.vn.sms.S1S2S3Activity;
import vnp.org.moit.dialog.ThongBaoDialog;
import vnp.org.moit.service.CreateTopicRunable;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.CreateTopicView
public class CreateTopicView extends LinearLayout implements OnClickListener {
	private EditText editText1;
	private EditText editText2;
	private String schoolid;
	private List<Bitmap> imgs = new ArrayList<Bitmap>();
	private Activity s1s2s3Activity;
	private GridView gridView;
	private String topicId;

	private void setBackgroundEditText(int res, boolean errorBackground) {
		CommonAndroid.getView(this, res).setBackgroundResource(errorBackground ? R.drawable.bg_editext_quare_error : android.R.color.transparent);
	}

	public CreateTopicView(Context context) {
		super(context);
		init();
	}

	public CreateTopicView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CreateTopicView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.createtopic, this);
		editText1 = CommonAndroid.getView(this, R.id.editText1);
		editText2 = CommonAndroid.getView(this, R.id.editText2);

		findViewById(R.id.newfeeddetail_back).setOnClickListener(this);
		findViewById(R.id.forum_top_dang).setOnClickListener(this);

		gridView = CommonAndroid.getView(this, R.id.gridView1);
		gridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = CommonAndroid.getView(getContext(), R.layout.create_topic_img, null);
				}
				int size = (int) getContext().getResources().getDimension(R.dimen.dimen_80dp);
				AbsListView.LayoutParams params = new AbsListView.LayoutParams(size, size);
				convertView.setLayoutParams(params);
				ImageView imageView = CommonAndroid.getView(convertView, R.id.imageView1);
				imageView.setBackgroundResource(R.drawable.add_img);
				if (imgs.size() > position) {
					Bitmap bitmap = imgs.get(position);
					imageView.setImageBitmap(bitmap);
				} else {
					imageView.setImageResource(R.drawable.add_img);
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				int count = imgs.size();
				if (count < 6) {
					count = count + 1;
				}
				return count;
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				if (position == imgs.size()) {
					if (s1s2s3Activity instanceof S1S2S3Activity)
						((S1S2S3Activity) s1s2s3Activity).openChooserImageForTopic();
					else
						((CreateTopicActivity) s1s2s3Activity).openChooserImageForTopic();
				} else {
					new ThongBaoDialog(getContext(), getContext().getString(R.string.banmuonxoaanh), getContext().getString(R.string.huy), getContext().getString(R.string.ok)) {
						public void onClickButton2() {
							imgs.remove(position);
							((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
						};
					}.show();
				}
			}
		});
	}

	public void reset(String schoolid) {
		this.schoolid = schoolid;
		editText1.setText("");
		editText2.setText("");
		imgs.clear();
		((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
		setBackgroundEditText(R.id.editText1, false);
		setBackgroundEditText(R.id.editText2, false);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.newfeeddetail_back) {
			s1s2s3Activity.onBackPressed();
		} else if (v.getId() == R.id.forum_top_dang) {
			if (CommonAndroid.isBlank(editText1.getText().toString())) {
				setBackgroundEditText(R.id.editText1, CommonAndroid.isBlank(editText1.getText().toString()));
				new ThongBaoDialog(getContext(), getContext().getString(R.string.vuilongnhaptieude), null, getContext().getString(R.string.ok)).show();
				return;
			} else if (CommonAndroid.isBlank(editText2.getText().toString())) {
				setBackgroundEditText(R.id.editText2, CommonAndroid.isBlank(editText2.getText().toString()));
				new ThongBaoDialog(getContext(), getContext().getString(R.string.vuilongnhapnoidung), null, getContext().getString(R.string.ok)).show();
				return;
			}

			if(!CommonAndroid.NETWORK.haveConnected(getContext())){
				new ThongBaoDialog(getContext(), getContext().getString(R.string.thuchienkhongthanhcong), null, getContext().getString(R.string.ok)).show();
				return;
			}
			
			
			if (imgs.size() == 0) {
				new ThongBaoDialog(getContext(), R.string.bancomuonhapanhdaidien, R.string.boqua, R.string.nhapdanh) {
					@Override
					public void onClickButton1() {
						super.onClickButton1();
						add();
					}

					@Override
					public void onClickButton2() {
						super.onClickButton2();
						
						if (s1s2s3Activity instanceof S1S2S3Activity)
							((S1S2S3Activity) s1s2s3Activity).openChooserImageForTopic();
						else
							((CreateTopicActivity) s1s2s3Activity).openChooserImageForTopic();
					}
				}.show();
			} else
				add();
		}
	}

	private void add() {
		Bitmap imgMain = null;
		if (imgs.size() >= 1) {
			imgMain = imgs.get(0);
			imgMain = CommonAndroid.getScaledBitmap(imgMain, 100, 100);
		}

		String title = editText1.getText().toString();
		String description = editText2.getText().toString();
		new CreateTopicRunable(getContext(), title, description, schoolid, imgs) {
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (isSuccess()) {
					new ThongBaoDialog(getContext(), getContext().getString(R.string.createtopicthanhcong), null, getContext().getString(R.string.ok)) {
						public void onClickButton2() {
							s1s2s3Activity.onBackPressed();
						};
					}.show();
				} else {
					new ThongBaoDialog(getContext(), getContext().getString(R.string.khongthetaotopic), null, getContext().getString(R.string.ok)).show();
				}
			};
		}.start();

	}

	public void imageForCreateTopPic(Bitmap bit) {
		if (bit != null) {
			imgs.add(bit);
			((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
		}
	}

	public void setActivity(Activity s1s2s3Activity2) {
		this.s1s2s3Activity = s1s2s3Activity2;
	}
}