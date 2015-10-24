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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.PushService;

/**
 * Handling of GCM messages.
 */
public class ParseBroadcastReceiverCustom extends BroadcastReceiver {
	private static final String TAG = "com.parse.ParseBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			Log.i(TAG, "startServiceIfRequired done");
			PushService.startServiceIfRequired(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.e(TAG, "startServiceIfRequired err");
		}
	}
}