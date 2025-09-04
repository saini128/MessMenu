package com.singhropar.messmenu.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.singhropar.messmenu.R
import com.singhropar.messmenu.data.MessMenuItem
import com.singhropar.messmenu.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class MessMenuWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_mess_menu)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val prefs = UserPreferences(context)
                    val hostel = prefs.getSelectedHostel().first() ?: "No Hostel"
                    val json = prefs.getCachedMenu().first()

                    val today = LocalDate.now().dayOfWeek
                        .getDisplayName(TextStyle.FULL, Locale.getDefault())

                    views.setTextViewText(R.id.widgetDay, today)
                    views.setTextViewText(R.id.widgetHostel, hostel)

                    if (!json.isNullOrEmpty()) {
                        try {
                            val type = object : TypeToken<List<MessMenuItem>>() {}.type
                            val menu = Gson().fromJson<List<MessMenuItem>>(json, type)

                            val hostelMenu = menu.find { it.hostel.equals(hostel, ignoreCase = true) }
                            val todayMeals = hostelMenu?.menu?.get(today.lowercase())

                            if (todayMeals != null) {
                                views.setTextViewText(
                                    R.id.widgetBreakfast,
                                    todayMeals.breakfast.joinToString().ifBlank { "—" }
                                )
                                views.setTextViewText(
                                    R.id.widgetLunch,
                                    todayMeals.lunch.joinToString().ifBlank { "—" }
                                )
                                views.setTextViewText(
                                    R.id.widgetDinner,
                                    todayMeals.dinner.joinToString().ifBlank { "—" }
                                )
                            } else {
                                // No menu for today
                                views.setTextViewText(R.id.widgetBreakfast, "—")
                                views.setTextViewText(R.id.widgetLunch, "—")
                                views.setTextViewText(R.id.widgetDinner, "—")
                            }
                        } catch (e: JsonSyntaxException) {
                            Log.e("MessMenuWidget", "Invalid JSON: ${e.message}")
                            views.setTextViewText(R.id.widgetBreakfast, "Error")
                            views.setTextViewText(R.id.widgetLunch, "Error")
                            views.setTextViewText(R.id.widgetDinner, "Error")
                        }
                    } else {
                        // No cached menu available
                        views.setTextViewText(R.id.widgetBreakfast, "No data")
                        views.setTextViewText(R.id.widgetLunch, "No data")
                        views.setTextViewText(R.id.widgetDinner, "No data")
                    }

                    // Open app when widget tapped
                    val intent =
                        context.packageManager.getLaunchIntentForPackage(context.packageName)
                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent)

                } catch (e: Exception) {
                    Log.e("MessMenuWidget", "Error updating widget: ${e.message}")
                    views.setTextViewText(R.id.widgetDay, "Error")
                    views.setTextViewText(R.id.widgetHostel, "—")
                    views.setTextViewText(R.id.widgetBreakfast, "—")
                    views.setTextViewText(R.id.widgetLunch, "—")
                    views.setTextViewText(R.id.widgetDinner, "—")
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        fun refreshAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, MessMenuWidgetProvider::class.java)
            )
            for (id in ids) {
                updateAppWidget(context, manager, id)
            }
        }
    }
}
