package com.samsung.accessory.neobeanmgr.core.fota.manager;

import android.util.Log;
import com.samsung.accessory.fotaprovider.controller.AccessoryUtil;
import com.samsung.accessory.neobeanmgr.Application;
import com.samsung.accessory.neobeanmgr.core.EarBudsInfo;

public final class FotaServerUtil extends AccessoryUtil {
    private static final String TAG = (Application.TAG_ + FotaServerUtil.class.getSimpleName());

    public int getMinimumBatteryLevel() {
        Log.d(TAG, "getMinimumBatteryLevel()");
        return 15;
    }

    public boolean isAvailableBatteryLevel() {
        Log.d(TAG, "isAvailableBatteryLevel()");
        EarBudsInfo earBudsInfo = Application.getCoreService().getEarBudsInfo();
        if (earBudsInfo.coupled) {
            if (earBudsInfo.batteryL < getMinimumBatteryLevel() || earBudsInfo.batteryR < getMinimumBatteryLevel()) {
                return false;
            }
        } else if (earBudsInfo.batteryL >= getMinimumBatteryLevel() || earBudsInfo.batteryR >= getMinimumBatteryLevel()) {
            return true;
        } else {
            return false;
        }
        return true;
    }

    public long getNeededFreeSpaceForDownload(long j) {
        String str = TAG;
        Log.d(str, "getNeededFreeSpaceForDownload() : " + j);
        long j2 = j * 2;
        if (j2 < 10) {
            return 10;
        }
        return j2;
    }

    public long getNeededFreeSpaceForCopy(long j) {
        String str = TAG;
        Log.d(str, "getNeededFreeSpaceForCopy() : " + j);
        long j2 = j * 2;
        if (j2 < 10) {
            return 10;
        }
        return j2;
    }

    public long getNeededFreeSpaceForInstall(long j) {
        String str = TAG;
        Log.d(str, "getNeededFreeSpaceForInstall() : " + j);
        long j2 = j * 2;
        if (j2 < 10) {
            return 10;
        }
        return j2;
    }

    public boolean isAvailableFreeSpaceForDownload(long j) {
        String str = TAG;
        Log.d(str, "isAvailableFreeSpaceForDownload() : " + j);
        return true;
    }

    public boolean isAvailableFreeSpaceForCopy(long j) {
        String str = TAG;
        Log.d(str, "isAvailableFreeSpaceForCopy() : " + j);
        return true;
    }

    public boolean isAvailableFreeSpaceForInstall(long j) {
        String str = TAG;
        Log.d(str, "isAvailableFreeSpaceForInstall() : " + j);
        return true;
    }
}
