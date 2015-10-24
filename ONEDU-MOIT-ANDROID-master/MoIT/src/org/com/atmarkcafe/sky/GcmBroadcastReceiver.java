/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.com.atmarkcafe.sky;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Handling of GCM messages.
 */
public class GcmBroadcastReceiver extends ParsePushBroadcastReceiver {
	static final String TAG = "GcmBroadcastReceiver";
	final static String KEY_USER_INFO = "userId";
	final static String KEY_CHANNEL_INFO_TEST = "privateACV2";// ["privateACV","global"]privateACV1
	final static String KEY_CHANNEL_INFO_RELEASE = "pushglobal";// global
	private Context mContext;

	static Boolean key_parse_release = true; // default === true
	private Boolean isDuplicate = false;

	@Override
	protected Notification getNotification(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String jsonData = extras.getString("com.parse.Data");
		return null;
	}

}