package com.accessorydm.interfaces;

public interface XFOTAInterface {
    public static final String XDL_DEFAULT_PKGNAME = "fota_delta_dp";
    public static final String XDL_DEFAULT_PKGVERSION = "1.0";
    public static final int XDL_FOTA_FORCE_INSTALL = 1;
    public static final int XDL_FOTA_NORMAL_INSTALL = 0;
    public static final int XDL_FOTA_UPDATE_FAILURE = -1;
    public static final int XDL_FOTA_UPDATE_SUCCESS = 200;
    public static final int XDL_FOTA_UPDATE_UNKNOWN = 0;
    public static final int XDL_MEMORY_ENCRYPTED = 3;
    public static final int XDL_MEMORY_INSUFFICIENT = 2;
    public static final int XDL_OVER_OBJECT_SIZE = 1;
    public static final int XDL_RET_CONTINUE = 1;
    public static final int XDL_RET_FAILED = -2;
    public static final int XDL_RET_OK = 0;
    public static final int XDL_RET_UNKNOWN_DD = -1;
    public static final int XDL_STATE_COPY_COMPLETE = 251;
    public static final int XDL_STATE_COPY_IN_PROGRESS = 250;
    public static final int XDL_STATE_DOWNLOAD_COMPLETE = 40;
    public static final int XDL_STATE_DOWNLOAD_DESCRIPTOR = 200;
    public static final int XDL_STATE_DOWNLOAD_FAILED = 20;
    public static final int XDL_STATE_DOWNLOAD_FAILED_REPORTING = 241;
    public static final int XDL_STATE_DOWNLOAD_IN_CANCEL = 230;
    public static final int XDL_STATE_DOWNLOAD_IN_PROGRESS = 30;
    public static final int XDL_STATE_IDLE_START = 10;
    public static final int XDL_STATE_NONE = 0;
    public static final int XDL_STATE_NO_RESPONSE_UPDATE_RESULT = 252;
    public static final int XDL_STATE_POSTPONE_TO_UPDATE = 220;
    public static final int XDL_STATE_READY_TO_UPDATE = 50;
    public static final int XDL_STATE_UPDATE_FAILED_NODATA = 80;
    public static final int XDL_STATE_UPDATE_IN_PROGRESS = 60;
    public static final int XDL_STATE_UPDATE_SUCCESSFUL_NODATA = 100;
    public static final int XDL_STATE_UPDATE_TO_REPORTING = 65;
    public static final int XDL_STATE_USER_CANCEL_REPORTING = 240;
    public static final int XDL_STATUS_ATTRIBUTE_MISMATCH = 4;
    public static final int XDL_STATUS_BLOCKED_BY_MDM_POLICY = 10;
    public static final int XDL_STATUS_DEVICE_ABORTED = 7;
    public static final int XDL_STATUS_INVALID_DDVERSIONV = 6;
    public static final int XDL_STATUS_INVALID_DESCRIPTOR = 5;
    public static final int XDL_STATUS_LOADER_ERROR = 9;
    public static final int XDL_STATUS_LOSS_SERVICE = 3;
    public static final int XDL_STATUS_MEMORY_ERROR = 1;
    public static final int XDL_STATUS_NONE = 11;
    public static final int XDL_STATUS_NON_ACCEPTABLE_CONTENT = 8;
    public static final int XDL_STATUS_SUCCESS = 0;
    public static final int XDL_STATUS_USER_CANCEL = 2;
    public static final String XFOTA_GENERIC_AUTHENTICATION_FAILURE = "406";
    public static final String XFOTA_GENERIC_BAD_URL = "411";
    public static final String XFOTA_GENERIC_BLOCKED_MDM_UPDATE_FAILED = "462";
    public static final String XFOTA_GENERIC_CLIENT_ERROR = "400";
    public static final String XFOTA_GENERIC_CORRUPTED_FW_UP = "402";
    public static final String XFOTA_GENERIC_DL_SERVER_FORBIDDEN = "510";
    public static final String XFOTA_GENERIC_DL_SERVER_REDIRECT = "511";
    public static final String XFOTA_GENERIC_DL_SERVICE_UNAVAILABLE = "512";
    public static final String XFOTA_GENERIC_DOWNLOAD_FAILED_NETWORK = "503";
    public static final String XFOTA_GENERIC_DOWNLOAD_FAILED_OUT_MEMORY = "501";
    public static final String XFOTA_GENERIC_FAILED_FW_UP_VALIDATION = "404";
    public static final String XFOTA_GENERIC_NOT_ACCEPTABLE = "405";
    public static final String XFOTA_GENERIC_NOT_IMPLEMENTED = "408";
    public static final String XFOTA_GENERIC_PACKAGE_MISMATCH = "403";
    public static final String XFOTA_GENERIC_REQUEST_TIME_OUT = "407";
    public static final String XFOTA_GENERIC_ROOTING_UPDATE_FAILED = "450";
    public static final String XFOTA_GENERIC_SAP_CONNECT_FAILED = "472";
    public static final String XFOTA_GENERIC_SAP_COPY_FAILED = "470";
    public static final String XFOTA_GENERIC_SAP_FAILED_OUT_MEMORY = "471";
    public static final String XFOTA_GENERIC_SAP_NO_RESPONSE_UPDATE_RESULT = "474";
    public static final String XFOTA_GENERIC_SAP_USER_CANCELED_UPDATE = "473";
    public static final String XFOTA_GENERIC_SERVER_ERROR = "500";
    public static final String XFOTA_GENERIC_SERVER_UNAVAILABLE = "412";
    public static final String XFOTA_GENERIC_SUCCESSFUL = "200";
    public static final String XFOTA_GENERIC_SUCCESSFUL_DOWNLOAD = "200";
    public static final String XFOTA_GENERIC_SUCCESSFUL_UPDATE = "200";
    public static final String XFOTA_GENERIC_SUCCESSFUL_VENDOR_SPECIFIED = "250";
    public static final String XFOTA_GENERIC_UNDEFINED_ERROR = "409";
    public static final String XFOTA_GENERIC_UPDATE_FAILED = "410";
    public static final String XFOTA_GENERIC_UPDATE_FAILED_OUT_MEMORY = "502";
    public static final String XFOTA_GENERIC_USER_CANCELED_DOWNLOAD = "401";
    public static final String XFOTA_GENERIC_USER_CANCELED_UPDATE = "413";
    public static final String XFOTA_GENERIC_WATCH_CHANGE = "415";
    public static final String XFOTA_GENERIC_WATCH_RESET = "414";
    public static final String XFUMO_DOWNLOADANDUPDATE_PATH = "/DownloadAndUpdate";
    public static final String XFUMO_DOWNLOADCONNTYPE_PATH = "/DownloadConnType";
    public static final String XFUMO_DOWNLOAD_PATH = "/Download";
    public static final String XFUMO_DOWNLOAD_TYPE_3G = "3G";
    public static final String XFUMO_DOWNLOAD_TYPE_WIFI = "Wifi";
    public static final String XFUMO_EXT = "/Ext";
    public static final String XFUMO_FORCE_PATH = "/ForceInstall";
    public static final String XFUMO_PATH = "./FUMO";
    public static final String XFUMO_PKGDATA_PATH = "/PkgData";
    public static final String XFUMO_PKGNAME_PATH = "/PkgName";
    public static final String XFUMO_PKGURL_PATH = "/PkgURL";
    public static final String XFUMO_PKGVERSION_PATH = "/PkgVersion";
    public static final String XFUMO_POSTPONE_PATH = "/Postpone";
    public static final String XFUMO_ROOTINGCHECK_PATH = "/DoCheckingRooting";
    public static final String XFUMO_STATE_PATH = "/State";
    public static final String XFUMO_SVCSTATE = "/SvcState";
    public static final String XFUMO_SVCSTATE_FULLY_OPEN = "260";
    public static final String XFUMO_SVCSTATE_PARTIALLY_OPEN = "261";
    public static final String XFUMO_UPDATE_PATH = "/Update";
}