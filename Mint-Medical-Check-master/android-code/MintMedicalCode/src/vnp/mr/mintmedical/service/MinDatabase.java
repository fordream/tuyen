package vnp.mr.mintmedical.service;

import android.content.Context;

import com.vnp.core.datastore.CommonTable;
import com.vnp.core.datastore.DBAdapter;

public class MinDatabase {
	private static MinDatabase instance = new MinDatabase();
	DBAdapter adapter;

	private MinDatabase() {
	}

	public static MinDatabase getInstance() {
		return instance == null ? instance = new MinDatabase() : instance;
	}

	public void init(Context context) {
		if (context != null) {
			if (adapter == null) {
				adapter = new DBAdapter(context, "db.sqlite",
						context.getPackageName());
				adapter.createDB();
			}
		}
	}

	public void insert(CommonTable table) {
		adapter.insert(table);
	}

	public void selected(UserTable table) {
		adapter.selectAll(table);
	}
}