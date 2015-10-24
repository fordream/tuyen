package vnp.org.moit.view;

import one.edu.vn.sms.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vnp.core.common.CommonAndroid;

//vnp.org.moit.view.ArticleGetListView
public class ArticleGetListView extends LinearLayout {
	private ListView list;

	public ArticleGetListView(Context context) {
		super(context);
		CommonAndroid.getView(context, R.layout.forrum, this);
		init();
	}

	public ArticleGetListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(context, R.layout.forrum, this);
		init();
	}

	public ArticleGetListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(context, R.layout.forrum, this);
		init();
	}

	private void init() {
		list = CommonAndroid.getView(this, R.id.forum_list);
	}
	
	public void loadData(){
		
		
	}
}