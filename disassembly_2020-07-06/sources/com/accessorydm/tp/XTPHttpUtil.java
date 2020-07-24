package com.accessorydm.tp;

import com.accessorydm.db.file.XDBUrlInfo;
import com.accessorydm.interfaces.XTPInterface;
import com.accessorydm.tp.urlconnect.HttpNetworkInterface;

public class XTPHttpUtil implements XTPInterface {
    public static String xtpHttpParsePath(String str) {
        int indexOf = str.indexOf("://");
        return str.substring(indexOf + str.substring(indexOf + 3).indexOf(47) + 3);
    }

    public static int xtpHttpGetConnectType(String str) {
        String substring = str.substring(0, 5);
        if (substring.equals("http:")) {
            return 2;
        }
        if (substring.equals(HttpNetworkInterface.XTP_NETWORK_TYPE_HTTPS)) {
            return 1;
        }
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d A[RETURN] */
    public static int xtpHttpExchangeProtocolType(String str) {
        char c;
        int hashCode = str.hashCode();
        if (hashCode != 3213448) {
            if (hashCode == 99617003 && str.equals(HttpNetworkInterface.XTP_NETWORK_TYPE_HTTPS)) {
                c = 0;
                if (c != 0) {
                    return c != 1 ? 0 : 2;
                }
                return 1;
            }
        } else if (str.equals(HttpNetworkInterface.XTP_NETWORK_TYPE_HTTP)) {
            c = 1;
            if (c != 0) {
            }
        }
        c = 65535;
        if (c != 0) {
        }
    }

    public static XDBUrlInfo xtpURLParser(String str) {
        String str2;
        String str3;
        XDBUrlInfo xDBUrlInfo = new XDBUrlInfo();
        if (str.startsWith("https://")) {
            str3 = str.substring(8, str.length());
            str2 = HttpNetworkInterface.XTP_NETWORK_TYPE_HTTPS;
        } else if (str.startsWith("http://")) {
            str3 = str.substring(7, str.length());
            str2 = HttpNetworkInterface.XTP_NETWORK_TYPE_HTTP;
        } else {
            xDBUrlInfo.pURL = "http://";
            return xDBUrlInfo;
        }
        int indexOf = str3.indexOf(47);
        String substring = indexOf != -1 ? str3.substring(indexOf, str3.length()) : "";
        String[] split = str3.split("/");
        String str4 = split[0];
        String[] split2 = split[0].split(":");
        int i = 80;
        if (split2.length >= 2) {
            i = Integer.valueOf(split2[1]).intValue();
            str4 = split2[0];
        } else if (xtpHttpExchangeProtocolType(str2) == 1) {
            i = 443;
        }
        xDBUrlInfo.pURL = str;
        xDBUrlInfo.pAddress = str4;
        xDBUrlInfo.pPath = substring;
        xDBUrlInfo.pProtocol = str2;
        xDBUrlInfo.nPort = i;
        return xDBUrlInfo;
    }
}
