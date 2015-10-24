package vnp.org.moit.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.LogUtils;

public class EduMarkTableGet extends SkypeTable {

	public EduMarkTableGet(Context context) {
		super(context, EduDBProvider.class);
		addColumns("studentid");
		addColumns("NamHoc");
		addColumns("jSonMark");
		addColumns("time");
		addColumns("smsid");
	}

	@Override
	public void add(JSONObject object) {
		String NamHoc = CommonAndroid.getString(object, "NamHoc");
		String studentid = CommonAndroid.getString(object, "studentid");
		String where = String.format("%s = '%s' and %s = '%s'", "NamHoc", NamHoc, "studentid", studentid);
		if (has(where)) {
			update(createContentValuesFormJsonObject(object), where);
		} else {
			super.add(object);
		}
	}

	public List<String> getListNamHocs(String studenId) {
		List<String> list = new ArrayList<String>();

		String where = String.format("%s = '%s' ", "studentid", studenId);

		Cursor cursor = querry(where);
		while (cursor != null && cursor.moveToNext()) {
			String NamHoc = CommonAndroid.getString(cursor, "NamHoc");
			if (!CommonAndroid.isBlank(NamHoc) && !list.contains(NamHoc)) {
				list.add(NamHoc);
			}
		}

		if (cursor != null)
			cursor.close();
		return list;
	}

	public void deleteBySmsId(String studentID, String NamHoc) {
		String where = String.format("%s = '%s' and %s ='%s'", "NamHoc", NamHoc, "studentid", studentID);
		delete(where);
	}

	public Cursor queryByStudentIdCanam(String studentId) {
		String where = null;
		if (CommonAndroid.isBlank(studentId)) {
			where = null;
		} else {
			where = String.format("%s = '%s' ", "studentid", studentId);
		}
		
		LogUtils.e("StudentIdX", studentId);
		return querry(where, "NamHoc DESC");
	}
}