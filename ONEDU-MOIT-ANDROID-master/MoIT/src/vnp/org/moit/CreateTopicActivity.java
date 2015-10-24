package vnp.org.moit;

import vnp.org.moit.view.CreateTopicView;
import one.edu.vn.sms.R;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.vnp.core.activity.BaseActivity;
import com.vnp.core.common.CommonAndroid;
import com.vnp.core.common.GalleryCameraChooser;

public class CreateTopicActivity extends BaseActivity {
	private GalleryCameraChooser galleryCameraChooser = new GalleryCameraChooser() {
		@Override
		public void onGallery(Bitmap bitmap) {
			imageForCreateTopPic(bitmap);
		}

		@Override
		public void onGalleryError() {

		}

		@Override
		public void onCamera(Bitmap bitmap) {
			imageForCreateTopPic(bitmap);
		}
	};
	CreateTopicView createTopicView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createTopicView = new CreateTopicView(this);
		setContentView(createTopicView);
		createTopicView.setActivity(this);
		createTopicView.reset(getIntent().getStringExtra("schoolid"));
	}

	protected void imageForCreateTopPic(Bitmap bitmap) {
		try {
			bitmap = CommonAndroid.scaleBitmap(bitmap, 500);
		} catch (Exception ex) {

		} catch (Error e) {

		}
		createTopicView.imageForCreateTopPic(bitmap);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		galleryCameraChooser.onActivityResult(this, requestCode, resultCode, data);
	}

	public void openChooserImageForTopic() {
		Builder builder = new Builder(this);
		builder.setItems(new String[] { "Gallery", "Camera" }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					galleryCameraChooser.startGalleryChooser(CreateTopicActivity.this);
				} else {
					galleryCameraChooser.startCameraChooser(CreateTopicActivity.this);
				}
			}
		});
		builder.setNegativeButton(R.string.boqua, null);
		builder.show();
	}
}
