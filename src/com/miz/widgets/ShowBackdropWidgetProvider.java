/*
 * Copyright (C) 2014 Michell Bak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miz.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.miz.functions.MizLib;
import com.miz.mizuu.R;
import com.miz.mizuu.TvShowDetails;

@SuppressWarnings("deprecation")
public class ShowBackdropWidgetProvider extends AppWidgetProvider {

	public static final String SHOW_BACKDROP_WIDGET = "showBackdropWidget";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (SHOW_BACKDROP_WIDGET.equals(action)) {
			Intent openShow = new Intent();
			openShow.putExtra("showId", intent.getStringExtra("showId"));
			openShow.putExtra("isFromWidget", true);
			openShow.setClass(context, TvShowDetails.class);
			openShow.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(openShow);
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; ++i) {
			Intent intent = new Intent(context, ShowBackdropWidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.movie_backdrop_widget);

			if (MizLib.hasICS())
				rv.setRemoteAdapter(R.id.widget_grid, intent);
			else
				rv.setRemoteAdapter(appWidgetIds[i], R.id.widget_grid, intent);

			Intent toastIntent = new Intent(context, ShowBackdropWidgetProvider.class);
			toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			rv.setPendingIntentTemplate(R.id.widget_grid, PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT));

			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}