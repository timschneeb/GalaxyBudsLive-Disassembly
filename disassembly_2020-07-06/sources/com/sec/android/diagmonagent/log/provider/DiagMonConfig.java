package com.sec.android.diagmonagent.log.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants;
import com.sec.android.diagmonagent.log.provider.utils.DiagMonUtil;
import java.util.ArrayList;
import java.util.List;

public class DiagMonConfig {
    public boolean globalNetworkMode;
    private boolean isCustomConfig = false;
    private String mAgreeAsString = "";
    private boolean mAgreement;
    private Context mContext;
    private String mDeviceId = "";
    public boolean mIsDefaultNetwork;
    private String mServiceId = "";
    private String mServiceVer = "";
    private String mTrackingId = "";
    private oldDiagMonConfig oldConf;

    public DiagMonConfig(Context context) {
        this.mContext = context;
        this.mAgreement = false;
        this.globalNetworkMode = true;
        this.mIsDefaultNetwork = false;
        try {
            this.mServiceVer = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldConf = new oldDiagMonConfig(context);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: protected */
    public void setCustomConfigStatus(boolean z) {
        if (z) {
            this.isCustomConfig = true;
        } else {
            this.isCustomConfig = false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCustomConfiguration() {
        return this.isCustomConfig;
    }

    public oldDiagMonConfig getOldConfig() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldConf;
        }
        return null;
    }

    public DiagMonConfig setServiceId(String str) {
        this.mServiceId = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldConf.setServiceId(str);
            this.oldConf.setAuthorityList(str);
        }
        return this;
    }

    public String getServiceId() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldConf.getServiceId();
        }
        return this.mServiceId;
    }

    public String getServiceVer() {
        return this.mServiceVer;
    }

    public DiagMonConfig setAgree(String str) {
        this.mAgreeAsString = str;
        if (this.mAgreeAsString == null) {
            Log.e(DiagMonUtil.TAG, "You can't use agreement as null");
            return this;
        }
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldConf.setAgree(this.mAgreeAsString);
        } else if (this.mAgreeAsString.equals("S")) {
            this.mAgreement = true;
        } else if (this.mAgreeAsString.equals("D")) {
            this.mAgreement = true;
        } else {
            this.mAgreement = false;
        }
        return this;
    }

    public String getAgreeAsString() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldConf.getAgreeAsString();
        }
        return this.mAgreeAsString;
    }

    public boolean getAgree() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldConf.getAgree();
        }
        return this.mAgreement;
    }

    public DiagMonConfig setDeviceId(String str) {
        this.mDeviceId = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldConf.setDeviceId(str);
        }
        return this;
    }

    public String getDeviceId() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldConf.getDeviceId();
        }
        return this.mDeviceId;
    }

    public DiagMonConfig setTrackingId(String str) {
        this.mTrackingId = str;
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            this.oldConf.setTrackingId(str);
        }
        return this;
    }

    public String getTrackingId() {
        if (DiagMonUtil.checkDMA(this.mContext) == 1) {
            return this.oldConf.getTrackingId();
        }
        return this.mTrackingId;
    }

    public DiagMonConfig setDefaultNetwork(boolean z) {
        this.mIsDefaultNetwork = true;
        this.globalNetworkMode = z;
        String str = DiagMonUtil.TAG;
        Log.i(str, "DefaultNetwork : " + this.mIsDefaultNetwork);
        String str2 = DiagMonUtil.TAG;
        Log.i(str2, "globalNetworkMode : " + this.globalNetworkMode);
        return this;
    }

    public boolean isEnabledDefaultNetwork() {
        return this.mIsDefaultNetwork;
    }

    public boolean getDefaultNetworkMode() {
        return this.globalNetworkMode;
    }

    class oldDiagMonConfig {
        private String mAgreeAsString = "";
        private boolean mAgreement = false;
        private List<String> mAuthorityList;
        private Context mContext;
        private String mDeviceId = "";
        private List<String> mLogList;
        private String mServiceId = "";
        private String mServiceName = "Samsung Software";
        private String mTrackingId = "";

        public oldDiagMonConfig(Context context) {
            this.mContext = context;
            this.mAuthorityList = new ArrayList();
            this.mLogList = new ArrayList();
        }

        public void setAgree(String str) {
            this.mAgreeAsString = str;
            if (this.mAgreeAsString.equals("S")) {
                this.mAgreeAsString = FmmConstants.SUPPORT;
            }
            if (this.mAgreeAsString.isEmpty()) {
                Log.w(DiagMonUtil.TAG, "Empty agreement");
                this.mAgreement = false;
            } else if (this.mAgreeAsString.equals(FmmConstants.SUPPORT) || this.mAgreeAsString.equals("D")) {
                this.mAgreement = true;
            } else {
                String str2 = DiagMonUtil.TAG;
                Log.w(str2, "Wrong agreement : " + str);
                this.mAgreement = false;
            }
        }

        public boolean getAgree() {
            return this.mAgreement;
        }

        public String getAgreeAsString() {
            return this.mAgreeAsString;
        }

        public void setServiceId(String str) {
            this.mServiceId = str;
            setAuthorityList(str);
        }

        public String getServiceId() {
            return this.mServiceId;
        }

        public void setLogList(List<String> list) {
            this.mLogList = list;
        }

        public List<String> getLogList() {
            return this.mLogList;
        }

        public void setDeviceId(String str) {
            this.mDeviceId = str;
        }

        public String getDeviceId() {
            return this.mDeviceId;
        }

        public void setServiceName(String str) {
            this.mServiceName = str;
        }

        public String getServiceName() {
            return this.mServiceName;
        }

        public void setAuthorityList(String str) {
            List<String> list = this.mAuthorityList;
            list.add("com.sec.android.log." + str);
        }

        public List<String> getAuthorityList() {
            return this.mAuthorityList;
        }

        public void setTrackingId(String str) {
            this.mTrackingId = str;
        }

        public String getTrackingId() {
            return this.mTrackingId;
        }
    }
}
