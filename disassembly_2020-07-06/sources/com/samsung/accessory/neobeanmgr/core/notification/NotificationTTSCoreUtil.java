package com.samsung.accessory.neobeanmgr.core.notification;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.android.SDK.routine.Constants;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationTTSCoreUtil {
    private static final String TAG = "NeoBean_NotificationTTSCoreUtil";

    static String getNotificationMessageString(NotificationMessage notificationMessage, Context context, TextToSpeech textToSpeech) {
        String str;
        String str2;
        String str3;
        Context context2 = context;
        TextToSpeech textToSpeech2 = textToSpeech;
        String[] strArr = new String[3];
        Resources resources = context.getResources();
        String str4 = null;
        if (!isAvailableTTS(context, textToSpeech)) {
            Log.d(TAG, "Not available locales for TTS");
            Toast.makeText(context2, resources.getString(R.string.no_tts_voice_data), 0).show();
            return null;
        }
        Log.v(TAG, "available locales for TTS");
        strArr[0] = notificationMessage.getAppName();
        Log.d(TAG, "VN msg.getAppName() = " + strArr[0]);
        strArr[1] = notificationMessage.getMain();
        strArr[2] = notificationMessage.getBody();
        int type = notificationMessage.getType();
        String str5 = "";
        if (type != 4869) {
            switch (type) {
                case NotificationMessage.TYPE_ALARM:
                    if (notificationMessage.getWhen() != 0) {
                        Calendar instance = Calendar.getInstance();
                        String format = DateFormat.getTimeFormat(context).format(new Date(instance.get(1), instance.get(2) + 1, instance.get(5), ((int) notificationMessage.getWhen()) / 100, ((int) notificationMessage.getWhen()) % 100));
                        if (notificationMessage.getBody().equals(str5)) {
                            str5 = " " + String.format(resources.getString(R.string.vn_alarm_string), new Object[]{format});
                        } else {
                            str5 = " " + String.format(resources.getString(R.string.vn_alarm_string2), new Object[]{notificationMessage.getBody(), format});
                        }
                    }
                    String str6 = strArr[0] + str5;
                    Log.d(TAG, "VN TYPE_ALARM.");
                    return str6;
                case NotificationMessage.TYPE_SCHEDULE:
                    if (notificationMessage.getMain() == null && notificationMessage.getWhen() == 0) {
                        Log.d(TAG, "read app name");
                        String str7 = strArr[0];
                        Log.d(TAG, "VN TYPE_SCHEDULE.");
                        return str7;
                    }
                    if (notificationMessage.getMain() == null || notificationMessage.getMain().equals(str5)) {
                        str = String.format(resources.getString(R.string.vn_schedule_string), new Object[]{getCalendarTimeString(context2, notificationMessage.getWhen(), textToSpeech2)});
                    } else {
                        str = String.format(resources.getString(R.string.vn_schedule_with_title_string), new Object[]{notificationMessage.getMain(), getCalendarTimeString(context2, notificationMessage.getWhen(), textToSpeech2)});
                    }
                    String str8 = str;
                    Log.d(TAG, "VN TYPE_SCHEDULE.");
                    return str8;
                case NotificationMessage.TYPE_CALL:
                    String str9 = strArr[1];
                    if (str9 != null) {
                        if (str9.length() == 0 || str9.equals(str5)) {
                            Log.d(TAG, "VN TYPE_CALL. number is blank");
                            str3 = resources.getString(R.string.notification_call_unknown);
                        } else if (str9.equals("PRIVATE NUMBER")) {
                            str3 = resources.getString(R.string.notification_call_privatenum);
                        } else {
                            String contactName = getContactName(context2, str9);
                            if (contactName == null || contactName.equals(str5)) {
                                Log.d(TAG, "VN TYPE_CALL. number has no contact name");
                                str3 = arrangeCaller(context2, str9);
                            } else {
                                str3 = contactName;
                            }
                        }
                        str2 = String.format(resources.getString(R.string.vn_call_string), new Object[]{str3});
                    } else {
                        str2 = strArr[0];
                    }
                    String str10 = str2;
                    Log.d(TAG, "VN TYPE_CALL.");
                    return str10;
                default:
                    if (strArr[0].equals("TTS_simpleText")) {
                        return strArr[1];
                    }
                    return str5;
            }
        } else {
            if (notificationMessage.getPkgName().equals(NotificationConstants.MISSED_CALL_PACKAGENAME)) {
                String str11 = strArr[1];
                if (NotificationUtil.getAppNotificationDetails(NotificationConstants.MISSED_CALL_PACKAGENAME).equals(NotificationConstants.NOTIFICATION_TYPE_DETAIL)) {
                    strArr[0] = null;
                    String contactName2 = getContactName(context2, str11);
                    if (contactName2 == null || contactName2.equals(str5)) {
                        Log.d(TAG, "VN TYPE_MISSEDCALL. number has no contact name");
                        contactName2 = arrangeCaller(context2, str11);
                    }
                    str4 = context2.getString(R.string.vn_missedcall_string, new Object[]{contactName2});
                } else {
                    strArr[0] = context.getResources().getString(R.string.notification_missed_call);
                }
                strArr[1] = str4;
            }
            for (int i = 0; i < 3; i++) {
                if (strArr[i] != null) {
                    str5 = str5 + ", " + strArr[i];
                }
            }
            Log.d(TAG, "VN TYPE_NORMAL");
            return str5;
        }
    }

    static boolean isAvailableTTS(Context context, TextToSpeech textToSpeech) {
        Locale tTSLanguage = getTTSLanguage(textToSpeech);
        Log.d(TAG, "getNotificationMessageString():getTTSLanguage(mTTS) = " + getTTSLanguage(textToSpeech));
        if (tTSLanguage == null) {
            tTSLanguage = context.getResources().getConfiguration().locale;
            Log.d(TAG, "if TTS locale is null, set System Language to TTS Language. locale = " + tTSLanguage);
        }
        Preferences.putString(PreferenceKey.NOTIFICATION_LANGUAGE, tTSLanguage.getLanguage());
        Log.d(TAG, "getNotificationMessageString():locale.getLanguage() = " + tTSLanguage.getLanguage());
        Log.d(TAG, "TTS engine : " + textToSpeech.getDefaultEngine() + ", set lang result = " + textToSpeech.setLanguage(tTSLanguage) + " lang available result = " + textToSpeech.isLanguageAvailable(tTSLanguage));
        boolean z = textToSpeech.setLanguage(tTSLanguage) >= 0;
        if ((!Util.isSamsungDevice() || !"com.samsung.SMT".equals(textToSpeech.getDefaultEngine())) && textToSpeech.isLanguageAvailable(tTSLanguage) < 1) {
            return false;
        }
        return z;
    }

    static Locale getTTSLanguage(TextToSpeech textToSpeech) {
        try {
            if (Build.VERSION.SDK_INT <= 21) {
                return textToSpeech.getDefaultLanguage();
            }
            if (textToSpeech.getDefaultVoice() == null) {
                return null;
            }
            return textToSpeech.getDefaultVoice().getLocale();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.lang.String, android.database.Cursor] */
    private static String getContactName(Context context, String str) {
        int columnIndex;
        ? r0 = 0;
        if (str == null) {
            return r0;
        }
        Log.d(TAG, "getContactName()");
        String[] strArr = {Constants.EXTRA_ID, "display_name"};
        String str2 = "";
        try {
            Cursor query = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), strArr, (String) null, (String[]) null, (String) null);
            if (!(query == null || !query.moveToFirst() || (columnIndex = query.getColumnIndex("display_name")) == -1)) {
                str2 = query.getString(columnIndex);
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception unused) {
            if (r0 != 0) {
                r0.close();
            }
        }
        return str2;
    }

    private static String arrangeCaller(Context context, String str) {
        Log.d(TAG, "arrangeCaller()");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) == '-' || str.charAt(i) == '+')) {
                sb.append(str.charAt(i));
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private static String getCalendarTimeString(Context context, long j, TextToSpeech textToSpeech) {
        if (textToSpeech == null || getTTSLanguage(textToSpeech) == null) {
            return "";
        }
        return DateFormat.getTimeFormat(context).format(new Date(j));
    }
}
