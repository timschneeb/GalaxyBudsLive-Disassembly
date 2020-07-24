package com.samsung.accessory.neobeanmgr.core.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationConstants;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationMessage;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationUtil;

public class CalendarReceiver extends BroadcastReceiver {
    private static final String CALENDAR_EVENTID_EXTRA = "eventid";
    private static final String CALENDAR_ISEVENT = "isEventAlert";
    private static final String CALENDAR_TASKID_EXTRA = "_id";
    public static final Uri EVENT_CONTENT_URI = Uri.parse("content://com.android.calendar/events");
    private static final String TAG = "NeoBean_CalendarReceiver";
    public static final Uri TASK_CONTENT_URI = Uri.parse("content://com.android.calendar/syncTasks");
    private Context mContext;

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    private void getEvent(long j) {
        Log.d(TAG, "getEvent: for eventId = " + j);
        Cursor cursor = null;
        try {
            String[] strArr = {"title", "dtstart"};
            Cursor cursor2 = this.mContext.getContentResolver().query(EVENT_CONTENT_URI, strArr, " _id = " + j + " AND " + "visible" + " = 1", (String[]) null, (String) null);
            if (cursor2 != null) {
                try {
                    Log.d(TAG, "getEvents Number of vCalenders found : " + cursor2.getCount());
                    if (cursor2.moveToFirst()) {
                        int columnIndex = cursor2.getColumnIndex(strArr[0]);
                        int columnIndex2 = cursor2.getColumnIndex(strArr[1]);
                        String string = cursor2.getString(columnIndex);
                        Log.d(TAG, "title::" + string);
                        String str = NotificationUtil.getAppNotificationDetails(NotificationConstants.CALENDAR_PACKAGENAME).equals(NotificationConstants.NOTIFICATION_TYPE_SUMMARY) ? null : string;
                        Log.d(TAG, "title::" + str);
                        NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.TYPE_SCHEDULE, NotificationConstants.CALENDAR_PACKAGENAME, getApplicationLabel(this.mContext), str, (String) null, Long.parseLong(cursor2.getString(columnIndex2)));
                        notificationMessage.log();
                        Log.d(TAG, "MainService is alive");
                        Intent intent = new Intent(NotificationConstants.ACTION_UPDATE_VN_MESSAGE);
                        intent.putExtra(NotificationConstants.VN_MESSAGE, notificationMessage);
                        this.mContext.sendBroadcast(intent, "com.samsung.accessory.neobeanmgr.permission.SIGNATURE");
                    }
                } catch (Exception e) {
                    e = e;
                    cursor = cursor2;
                    try {
                        e.printStackTrace();
                        if (cursor == null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            } else {
                Log.e(TAG, "[getEvent] managedCursor is null");
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            if (cursor == null) {
                cursor.close();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    private void getTask(long j) {
        Log.d(TAG, "getTask: for eventId = " + j);
        Cursor cursor = null;
        try {
            String[] strArr = {"subject", "reminder_time"};
            String str = " _id = " + j;
            Log.d(TAG, "getTask, selection=" + str);
            Cursor cursor2 = this.mContext.getContentResolver().query(TASK_CONTENT_URI, strArr, str, (String[]) null, (String) null);
            if (cursor2 != null) {
                try {
                    Log.d(TAG, "getEvents Number of vCalenders found : " + cursor2.getCount());
                    if (cursor2.moveToFirst()) {
                        int columnIndex = cursor2.getColumnIndex(strArr[0]);
                        int columnIndex2 = cursor2.getColumnIndex(strArr[1]);
                        do {
                            String string = NotificationUtil.getAppNotificationDetails(NotificationConstants.CALENDAR_PACKAGENAME).equals(NotificationConstants.NOTIFICATION_TYPE_SUMMARY) ? null : cursor2.getString(columnIndex);
                            Log.d(TAG, "title is " + string);
                            NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.TYPE_SCHEDULE, NotificationConstants.CALENDAR_PACKAGENAME, getApplicationLabel(this.mContext), string, (String) null, Long.parseLong(cursor2.getString(columnIndex2)));
                            notificationMessage.log();
                            Log.d(TAG, "MainService is alive");
                            Intent intent = new Intent(NotificationConstants.ACTION_UPDATE_VN_MESSAGE);
                            intent.putExtra(NotificationConstants.VN_MESSAGE, notificationMessage);
                            this.mContext.sendBroadcast(intent, "com.samsung.accessory.neobeanmgr.permission.SIGNATURE");
                        } while (cursor2.moveToNext());
                        if (cursor2 != null) {
                            cursor2.close();
                            cursor2 = null;
                        }
                    }
                } catch (Exception e) {
                    e = e;
                    cursor = cursor2;
                    try {
                        e.printStackTrace();
                        if (cursor == null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor2 != null) {
                    }
                    throw th;
                }
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            if (cursor == null) {
                cursor.close();
            }
        }
    }

    private String getApplicationLabel(Context context) {
        return context.getResources().getString(R.string.notification_schedule);
    }

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        Log.d(TAG, "onReceive action: Calendar");
        if (!NotificationUtil.checkAllStatus(NotificationConstants.CALENDAR_PACKAGENAME)) {
            Log.d(TAG, "is not enable");
        } else if (NotificationUtil.semAreNotificationsEnabledForPackage(NotificationConstants.CALENDAR_PACKAGENAME, false, 0)) {
            try {
                if ((context.getPackageManager().getPackageInfo(NotificationConstants.CALENDAR_PACKAGENAME, 0).applicationInfo.flags & 1073741824) == 0) {
                    Log.d(TAG, "not suspended");
                    if (!Util.isSamsungDevice()) {
                        return;
                    }
                    if (intent.getAction().equalsIgnoreCase(NotificationConstants.CALENDAR_SEND_ALERTINFO_ACTION)) {
                        try {
                            long longExtra = intent.getLongExtra(CALENDAR_EVENTID_EXTRA, -1);
                            boolean booleanExtra = intent.getBooleanExtra(CALENDAR_ISEVENT, true);
                            Log.d(TAG, "Calendar event notification received: EventID = " + longExtra);
                            if (booleanExtra) {
                                getEvent(longExtra);
                            } else {
                                getTask(longExtra);
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    } else if (intent.getAction().equals(NotificationConstants.CALENDAR_ACTION_TASK_ALARM)) {
                        try {
                            long longExtra2 = intent.getLongExtra("_id", -1);
                            Log.d(TAG, "Calendar task notification received: EventID = " + longExtra2);
                            getTask(longExtra2);
                        } catch (RuntimeException e2) {
                            e2.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "suspended!");
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }
}
