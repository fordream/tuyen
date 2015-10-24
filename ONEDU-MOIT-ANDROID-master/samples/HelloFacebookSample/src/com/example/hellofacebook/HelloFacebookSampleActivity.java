/**
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.hellofacebook;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.avnp.BaseFacebookActivity;
import com.facebook.avnp.Facebook415Utils;
import com.facebook.avnp.Facebook415Utils.IFacebookListener;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;

public class HelloFacebookSampleActivity extends BaseFacebookActivity {

	private final String PENDING_ACTION_BUNDLE_KEY = "com.example.hellofacebook:PendingAction";

	public IFacebookListener createIFacebookListener() {
		return listener;
	}

	IFacebookListener listener = new IFacebookListener() {

		@Override
		public void onLoginSuccess(LoginResult loginResult) {
			updateUI();
		}

		@Override
		public void onLoginCancel() {
			updateUI();
		}

		@Override
		public void onLoginError(FacebookException exception) {
			updateUI();
		}

		private void showAlert() {
			new AlertDialog.Builder(HelloFacebookSampleActivity.this).setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted).setPositiveButton(R.string.ok, null).show();
		}

		@Override
		public void onCurrentProfileChangedOfProfileTracker(Profile oldProfile, Profile currentProfile) {
			updateUI();
		}

		public void onShareCancel() {
			Log.d("HelloFacebook", "Canceled");
		}

		public void onShareError(FacebookException error) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
			String title = getString(R.string.error);
			String alertMessage = error.getMessage();
			showResult(title, alertMessage);
		}

		public void onShareSuccess(Sharer.Result result) {
			Log.d("HelloFacebook", "Success!");
			if (result.getPostId() != null) {
				String title = getString(R.string.success);
				String id = result.getPostId();
				String alertMessage = getString(R.string.successfully_posted_post, id);
				showResult(title, alertMessage);
			}
		}

		private void showResult(String title, String alertMessage) {
			new AlertDialog.Builder(HelloFacebookSampleActivity.this).setTitle(title).setMessage(alertMessage).setPositiveButton(R.string.ok, null).show();
		}
	};
	private Button postStatusUpdateButton;
	private Button postPhotoButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
		postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Facebook415Utils.getInstance().onClickPostStatusUpdate();
			}
		});

		postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
		postPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Facebook415Utils.getInstance().onClickPostPhoto(BitmapFactory.decodeResource(getResources(), R.drawable.icon));
			}
		});

		findViewById(R.id.postLogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Facebook415Utils.getInstance().isLogin()) {
					Facebook415Utils.getInstance().logout();
				} else
					Facebook415Utils.getInstance().login();
			}
		});
	}

	private void updateUI() {
		boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
		postStatusUpdateButton.setEnabled(enableButtons || Facebook415Utils.getInstance().isCanPresentShareDialog());
		postPhotoButton.setEnabled(enableButtons || Facebook415Utils.getInstance().isCanPresentShareDialogWithPhotos());
	}

}
