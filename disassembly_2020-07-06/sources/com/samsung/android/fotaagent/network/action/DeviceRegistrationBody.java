package com.samsung.android.fotaagent.network.action;

import android.util.Xml;
import com.accessorydm.db.file.AccessoryInfoAdapter;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;
import com.samsung.accessory.neobeanmgr.core.fmm.utils.FmmConstants;
import com.samsung.android.fotaprovider.deviceinfo.ProviderInfo;
import com.samsung.android.fotaprovider.log.Log;
import java.io.StringWriter;
import org.xmlpull.v1.XmlSerializer;

class DeviceRegistrationBody {
    private static final String TAG_ADDRESS = "devicePhysicalAddressText";
    private static final String TAG_APP_VERSION = "fotaClientVer";
    private static final String TAG_CC = "customerCode";
    private static final String TAG_FIRMWARE_VERSION = "firmwareVersion";
    private static final String TAG_FUMODEVICE = "FumoDeviceVO";
    private static final String TAG_MCC = "mobileCountryCode";
    private static final String TAG_MCC_NT = "mobileCountryCodeByTelephony";
    private static final String TAG_MNC = "mobileNetworkCode";
    private static final String TAG_MNC_NT = "mobileNetworkCodeByTelephony";
    private static final String TAG_MODELID = "deviceModelID";
    private static final String TAG_NAME = "deviceName";
    private static final String TAG_OPENNINGDATE = "initialBootingDate";
    private static final String TAG_PHONENUMBER = "phoneNumberText";
    private static final String TAG_SN = "deviceSerialNumber";
    private static final String TAG_TERMS = "terms";
    private static final String TAG_TERMS_VERSION = "termsVersion";
    private static final String TAG_TYPECODE = "deviceTypeCode";
    private static final String TAG_UN = "uniqueNumber";
    private static final String TAG_UNIQUEID = "deviceUniqueID";

    DeviceRegistrationBody() {
    }

    static String get() {
        AccessoryInfoAdapter accessoryInfoAdapter = new AccessoryInfoAdapter();
        ProviderInfo providerInfo = new ProviderInfo();
        String str = TAG_MNC_NT;
        try {
            XmlSerializer newSerializer = Xml.newSerializer();
            String str2 = TAG_MCC_NT;
            StringWriter stringWriter = new StringWriter();
            newSerializer.setOutput(stringWriter);
            StringWriter stringWriter2 = stringWriter;
            String str3 = TAG_MNC;
            newSerializer.startDocument(HttpNetworkInterface.XTP_HTTP_UTF8, true);
            newSerializer.startTag("", TAG_FUMODEVICE);
            newSerializer.startTag("", TAG_TYPECODE);
            newSerializer.text("PHONE DEVICE");
            newSerializer.endTag("", TAG_TYPECODE);
            newSerializer.startTag("", TAG_MODELID);
            newSerializer.text(accessoryInfoAdapter.getModelNumber());
            newSerializer.endTag("", TAG_MODELID);
            newSerializer.startTag("", TAG_NAME);
            newSerializer.text(accessoryInfoAdapter.getModelNumber());
            newSerializer.endTag("", TAG_NAME);
            newSerializer.startTag("", TAG_UNIQUEID);
            newSerializer.text(accessoryInfoAdapter.getDeviceId());
            newSerializer.endTag("", TAG_UNIQUEID);
            newSerializer.startTag("", TAG_ADDRESS);
            newSerializer.text(accessoryInfoAdapter.getDeviceId());
            newSerializer.endTag("", TAG_ADDRESS);
            newSerializer.startTag("", TAG_CC);
            newSerializer.text(accessoryInfoAdapter.getSalesCode());
            newSerializer.endTag("", TAG_CC);
            newSerializer.startTag("", TAG_UN);
            newSerializer.text(accessoryInfoAdapter.getUniqueNumber());
            newSerializer.endTag("", TAG_UN);
            newSerializer.startTag("", TAG_SN);
            newSerializer.text(accessoryInfoAdapter.getSerialNumber());
            newSerializer.endTag("", TAG_SN);
            newSerializer.startTag("", TAG_FIRMWARE_VERSION);
            newSerializer.text(accessoryInfoAdapter.getFirmwareVersion());
            newSerializer.endTag("", TAG_FIRMWARE_VERSION);
            newSerializer.startTag("", TAG_MCC);
            newSerializer.text(providerInfo.getSimMCC());
            newSerializer.endTag("", TAG_MCC);
            String str4 = str3;
            newSerializer.startTag("", str4);
            newSerializer.text(providerInfo.getSimMNC());
            newSerializer.endTag("", str4);
            String str5 = str2;
            newSerializer.startTag("", str5);
            newSerializer.text(providerInfo.getNetworkMCC());
            newSerializer.endTag("", str5);
            String str6 = str;
            newSerializer.startTag("", str6);
            newSerializer.text(providerInfo.getNetworkMNC());
            newSerializer.endTag("", str6);
            newSerializer.startTag("", TAG_PHONENUMBER);
            newSerializer.text("");
            newSerializer.endTag("", TAG_PHONENUMBER);
            newSerializer.startTag("", TAG_OPENNINGDATE);
            newSerializer.text("");
            newSerializer.endTag("", TAG_OPENNINGDATE);
            newSerializer.startTag("", "terms");
            newSerializer.text(FmmConstants.SUPPORT);
            newSerializer.endTag("", "terms");
            newSerializer.startTag("", TAG_TERMS_VERSION);
            newSerializer.text("");
            newSerializer.endTag("", TAG_TERMS_VERSION);
            newSerializer.startTag("", TAG_APP_VERSION);
            newSerializer.text(providerInfo.getAppVersion());
            newSerializer.endTag("", TAG_APP_VERSION);
            newSerializer.endTag("", TAG_FUMODEVICE);
            newSerializer.endDocument();
            return stringWriter2.toString();
        } catch (Exception e) {
            Log.printStackTrace(e);
            return null;
        }
    }
}
