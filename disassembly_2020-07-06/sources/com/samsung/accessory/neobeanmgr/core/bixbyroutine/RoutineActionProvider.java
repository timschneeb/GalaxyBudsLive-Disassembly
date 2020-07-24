package com.samsung.accessory.neobeanmgr.core.bixbyroutine;

import android.content.Intent;
import android.util.Log;
import com.accessorydm.interfaces.XDMInterface;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.preference.PreferenceKey;
import com.samsung.accessory.neobeanmgr.common.preference.Preferences;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.accessory.neobeanmgr.core.bigdata.SA;
import com.samsung.accessory.neobeanmgr.core.bigdata.SamsungAnalyticsUtil;
import com.samsung.accessory.neobeanmgr.core.gamemode.GameModeManager;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationConstants;
import com.samsung.accessory.neobeanmgr.core.notification.NotificationUtil;
import com.samsung.accessory.neobeanmgr.core.service.CoreService;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgID;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgLockTouchpad;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSetTouchpadOption;
import com.samsung.accessory.neobeanmgr.core.service.message.MsgSimple;
import com.samsung.android.SDK.routine.AbsRoutineActionProvider;
import com.samsung.android.sdk.mobileservice.social.buddy.provider.BuddyContract;

public class RoutineActionProvider extends AbsRoutineActionProvider {
    private static final String TAG = (Application.TAG_ + RoutineActionProvider.class.getSimpleName());

    public int onAct(String str, String str2, boolean z) {
        return 0;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public String getCurrentParam(String str) {
        char c;
        String str2 = TAG;
        Log.d(str2, "getCurrentParam : " + str);
        switch (str.hashCode()) {
            case -1535835213:
                if (str.equals("gaming_mode")) {
                    c = 3;
                    break;
                }
            case 643020320:
                if (str.equals("touchpad_option")) {
                    c = 4;
                    break;
                }
            case 843529938:
                if (str.equals("equalizer")) {
                    c = 0;
                    break;
                }
            case 1272354024:
                if (str.equals("notifications")) {
                    c = 1;
                    break;
                }
            case 2029855112:
                if (str.equals("lock_touchpad")) {
                    c = 2;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            return String.valueOf(Application.getCoreService().getEarBudsInfo().equalizerType);
        }
        if (c == 1) {
            return String.valueOf(Preferences.getBoolean(PreferenceKey.NOTIFICATION_ENABLE, true));
        }
        if (c == 2) {
            return String.valueOf(Application.getCoreService().getEarBudsInfo().touchpadLocked);
        }
        if (c == 3) {
            return String.valueOf(Application.getCoreService().getEarBudsInfo().adjustSoundSync);
        }
        if (c != 4) {
            return null;
        }
        return "" + Application.getCoreService().getEarBudsInfo().touchpadOptionLeft + Application.getCoreService().getEarBudsInfo().touchpadOptionRight;
    }

    public int onAct(String str, String str2, boolean z, boolean z2) {
        String str3 = TAG;
        Log.d(str3, "onAct tag : " + str + ", param : " + str2 + ", isNegative : " + z + ", isRecovery : " + z2);
        if (str2 == null) {
            Log.d(TAG, "Action fail :: ACT_ERR_NONE_PARAM");
            return -102;
        } else if (!Application.getCoreService().isConnected()) {
            Log.d(TAG, "Action fail :: ACT_ERR_SPP_CONNECTION_FAIL");
            return -101;
        } else if (!Preferences.getBoolean(PreferenceKey.SETUP_WIZARD_DONE, false)) {
            Log.d(TAG, "Action fail :: ACT_ERR_NOT_OOBE_COMPLETED");
            return -100;
        } else {
            char c = 65535;
            switch (str.hashCode()) {
                case -1535835213:
                    if (str.equals("gaming_mode")) {
                        c = 3;
                        break;
                    }
                    break;
                case 643020320:
                    if (str.equals("touchpad_option")) {
                        c = 4;
                        break;
                    }
                    break;
                case 843529938:
                    if (str.equals("equalizer")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1272354024:
                    if (str.equals("notifications")) {
                        c = 1;
                        break;
                    }
                    break;
                case 2029855112:
                    if (str.equals("lock_touchpad")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                int intValue = Integer.valueOf(str2).intValue();
                Application.getCoreService().getEarBudsInfo().equalizerType = intValue;
                SamsungAnalyticsUtil.setStatusString(SA.Status.EQUALIZER_STATUS, SamsungAnalyticsUtil.equalizerTypeToDetail(intValue));
                Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.EQUALIZER, (byte) intValue));
                Util.sendPermissionBroadcast(Application.getContext(), new Intent(CoreService.ACTION_MSG_ID_EQUALIZER_TYPE_UPDATED));
            } else if (c != 1) {
                if (c == 2) {
                    boolean booleanValue = Boolean.valueOf(str2).booleanValue();
                    Application.getCoreService().getEarBudsInfo().touchpadLocked = booleanValue;
                    Application.getCoreService().sendSppMessage(new MsgLockTouchpad(booleanValue));
                } else if (c != 3) {
                    if (c == 4) {
                        int numericValue = Character.getNumericValue(str2.charAt(0));
                        int numericValue2 = Character.getNumericValue(str2.charAt(1));
                        Application.getCoreService().getEarBudsInfo().touchpadOptionLeft = numericValue;
                        Application.getCoreService().getEarBudsInfo().touchpadOptionRight = numericValue2;
                        Application.getCoreService().sendSppMessage(new MsgSetTouchpadOption((byte) numericValue, (byte) numericValue2));
                    }
                } else if (!GameModeManager.isSupportDevice()) {
                    Log.d(TAG, "Action fail :: ACT_ERR_NOT_SUPPORTED_GAMING_MODE");
                    return -104;
                } else {
                    boolean booleanValue2 = Boolean.valueOf(str2).booleanValue();
                    Application.getCoreService().getEarBudsInfo().adjustSoundSync = booleanValue2;
                    SamsungAnalyticsUtil.setStatusString(SA.Status.GAME_MODE, Application.getCoreService().getEarBudsInfo().adjustSoundSync ? "1" : "0");
                    Application.getCoreService().sendSppMessage(new MsgSimple(MsgID.ADJUST_SOUND_SYNC, booleanValue2 ? (byte) 1 : 0));
                }
            } else if (!NotificationUtil.isAccessibilityON()) {
                Log.d(TAG, "Action fail :: ACT_ERR_PERMISSION_DENIED");
                return -105;
            } else {
                Preferences.putBoolean(PreferenceKey.NOTIFICATION_ENABLE, Boolean.valueOf(Boolean.valueOf(str2).booleanValue()));
                Util.sendPermissionBroadcast(Application.getContext(), new Intent(NotificationConstants.ACTION_NOTIFICATION_SETTING_UPDATE));
            }
            return 0;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public String getLabelParam(String str, String str2, boolean z) {
        char c;
        String str3 = TAG;
        Log.d(str3, "getLabelParam tag : " + str + ", param : " + str2 + ", isNegative : " + z);
        switch (str.hashCode()) {
            case -1535835213:
                if (str.equals("gaming_mode")) {
                    c = 1;
                    break;
                }
            case 643020320:
                if (str.equals("touchpad_option")) {
                    c = 2;
                    break;
                }
            case 843529938:
                if (str.equals("equalizer")) {
                    c = 4;
                    break;
                }
            case 1272354024:
                if (str.equals("notifications")) {
                    c = 3;
                    break;
                }
            case 2029855112:
                if (str.equals("lock_touchpad")) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        if (c == 0 || c == 1 || c == 2 || c == 3) {
            String str4 = TAG;
            Log.d(str4, "makeOnOffLabel : " + makeOnOffLabel(str2));
            return makeOnOffLabel(str2);
        } else if (c != 4) {
            return null;
        } else {
            String str5 = TAG;
            Log.d(str5, "makeEqualizerLabel : " + makeEqualizerLabel(str2));
            return makeEqualizerLabel(str2);
        }
    }

    private static String makeOnOffLabel(String str) {
        if (XDMInterface.XDM_DEVDETAIL_DEFAULT_LRGOBJ_SUPPORT.equals(str)) {
            return Application.getContext().getString(R.string.routine_off);
        }
        return Application.getContext().getString(R.string.routine_on);
    }

    private static String makeEqualizerLabel(String str) {
        if (str == null) {
            return Application.getContext().getString(R.string.eq_preset_normal);
        }
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 2;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c = 3;
                    break;
                }
                break;
            case 52:
                if (str.equals(BuddyContract.Email.Type.MOBILE)) {
                    c = 4;
                    break;
                }
                break;
            case 53:
                if (str.equals("5")) {
                    c = 5;
                    break;
                }
                break;
        }
        if (c == 0) {
            return Application.getContext().getString(R.string.eq_preset_normal);
        }
        if (c == 1) {
            return Application.getContext().getString(R.string.eq_preset_bass_boost);
        }
        if (c == 2) {
            return Application.getContext().getString(R.string.eq_preset_soft);
        }
        if (c == 3) {
            return Application.getContext().getString(R.string.eq_preset_dynamic);
        }
        if (c == 4) {
            return Application.getContext().getString(R.string.eq_preset_clear);
        }
        if (c != 5) {
            return Application.getContext().getString(R.string.eq_preset_normal);
        }
        return Application.getContext().getString(R.string.eq_preset_treble_boost);
    }
}
