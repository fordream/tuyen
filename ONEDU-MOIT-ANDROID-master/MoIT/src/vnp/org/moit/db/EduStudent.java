package vnp.org.moit.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.acv.cheerz.db.SkypeTable;
import com.vnp.core.common.CommonAndroid;

public class EduStudent extends SkypeTable {

	public EduStudent(Context context) {
		super(context, EduDBProvider.class);
		addColumns("phoneactive");
		addColumns("StudentId");
		addColumns("ClassId");
		addColumns("BlockId");
		addColumns("SchoolId");
		addColumns("ParentPhone");
		addColumns("Name");
		addColumns("ParentName");
		addColumns("ParentAvatar");
		addColumns("SchoolName");
		addColumns("SchoolLogo");
	}

	public void addStudent(String phoneactive, String StudentId, String ClassId, String BlockId, String SchoolId, String ParentPhone, String Name, String ParentName, String ParentAvatar,
			String SchoolName, String SchoolLogo) {
		ContentValues values = new ContentValues();
		values.put("phoneactive", phoneactive);
		values.put("StudentId", StudentId);
		values.put("ClassId", ClassId);
		values.put("BlockId", BlockId);
		values.put("SchoolId", SchoolId);
		values.put("ParentPhone", ParentPhone);
		values.put("Name", Name);
		values.put("ParentName", ParentName);
		values.put("ParentAvatar", ParentAvatar);
		values.put("SchoolName", SchoolName);
		values.put("SchoolLogo", SchoolLogo);
		String where = String.format("%s = '%s' and %s = '%s'", "StudentId", StudentId, "phoneactive", phoneactive);
		if (has(where)) {
			getContext().getContentResolver().update(getContentUri(), values, where, null);
		} else {
			getContext().getContentResolver().insert(getContentUri(), values);
		}
	}

	public List<String> getStudentIds() {
		List<String> SchoolIds = new ArrayList<String>();
		String phone = new EduAccount(getContext()).getPhoneActive();

		String where = String.format("%s = '%s'", "phoneactive", phone);
		Cursor cursor = querry(where);
		while (cursor != null && cursor.moveToNext()) {
			String SchoolId = cursor.getString(cursor.getColumnIndex("StudentId"));
			if (!CommonAndroid.isBlank(SchoolId) && !SchoolIds.contains(SchoolId)) {
				SchoolIds.add(SchoolId);
			}
		}

		if (cursor != null) {
			cursor.close();
		}

		return SchoolIds;
	}

	public List<String> getSchoolIds() {
		List<String> SchoolIds = new ArrayList<String>();
		String phone = new EduAccount(getContext()).getPhoneActive();

		String where = String.format("%s = '%s'", "phoneactive", phone);
		Cursor cursor = querry(where);
		while (cursor != null && cursor.moveToNext()) {
			String SchoolId = cursor.getString(cursor.getColumnIndex("SchoolId"));
			if (!CommonAndroid.isBlank(SchoolId) && !SchoolIds.contains(SchoolId)) {
				SchoolIds.add(SchoolId);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return SchoolIds;
	}

	public Cursor querry() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);
		return querry(where, null);
	}

	public String getSchoolName(String schoolId) {
		String SchoolName = null;
		String where = String.format("%s = '%s' ", "SchoolId", schoolId);
		Cursor cursor = querry(where);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				SchoolName = CommonAndroid.getString(cursor, "SchoolName");
			}
			cursor.close();
		}
		return SchoolName;
	}

	public String getSchoolAvatar(String schoolId) {
		String SchoolName = null;
		String where = String.format("%s = '%s' ", "SchoolId", schoolId);
		Cursor cursor = querry(where);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				SchoolName = CommonAndroid.getString(cursor, "SchoolLogo");
			}
			cursor.close();
		}
		return SchoolName;
	}

	public String getStudenName(String studentId) {
		String Name = null;
		String where = String.format("%s = '%s' ", "StudentId", studentId);
		Cursor cursor = querry(where);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				Name = CommonAndroid.getString(cursor, "Name");
			}
			cursor.close();
		}
		return Name;
	}

	public void deleteAll() {
		String phone = new EduAccount(getContext()).getPhoneActive();
		String where = String.format("%s = '%s' ", "phoneactive", phone);
		if (getContext() != null)
			getContext().getContentResolver().delete(getContentUri(), where, null);
	}

	public String getSchoolNameFromSchooldID(String studentid) {
		String SchoolName = null;
		String where = String.format("%s = '%s' ", "StudentId", studentid);
		Cursor cursor = querry(where);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				SchoolName = CommonAndroid.getString(cursor, "SchoolName");
			}
			cursor.close();
		}
		return SchoolName;
	}

	public String getSchoolAvatarByStudentId(String studentid) {
		String SchoolName = null;
		String where = String.format("%s = '%s' ", "StudentId", studentid);
		Cursor cursor = querry(where);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				SchoolName = CommonAndroid.getString(cursor, "SchoolLogo");
			}
			cursor.close();
		}
		return SchoolName;
	}
}