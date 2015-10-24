package vnp.org.moit.db;

import com.acv.cheerz.db.DBDatabaseHelper;
import com.acv.cheerz.db.DBProvider;

//vnp.org.moit.db.EduDBProvider
public class EduDBProvider extends DBProvider {

	public EduDBProvider() {
		super();
	}

	@Override
	public void addTable(DBDatabaseHelper db) {
		db.addTable(new EduAccount(getContext()));
		db.addTable(new EduStudent(getContext()));
		db.addTable(new EduArticleGetList(getContext()));
		db.addTable(new EduInbox(getContext()));
		db.addTable(new EduTopic(getContext()));
		db.addTable(new EduTopicImage(getContext()));
		db.addTable(new EduArticleAddComment(getContext()));
		db.addTable(new EduTopicCommentGetList(getContext()));
		db.addTable(new EduArticleGetLike(getContext()));
		db.addTable(new EduTopicGetLike(getContext()));
		db.addTable(new EduPersion(getContext()));
		db.addTable(new EduMarkTableGet(getContext()));
	}

	@Override
	public int getVersionDB() {
		return 2;
	}
}
