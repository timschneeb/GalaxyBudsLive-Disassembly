package com.accessorydm.eng.core;

public interface XDMXml {
    public static final int DDF_ID_APP = 3;
    public static final int DDF_ID_DEVDETAIL = 2;
    public static final int DDF_ID_DEVINFO = 1;
    public static final int DDF_ID_FUMO = 4;
    public static final int DDF_ID_SYNCML = 0;
    public static final int DDF_ID_TNDS = 5;
    public static final int XDM_MO_ID_DEVDETAIL = 9;
    public static final int XDM_MO_ID_DEVINFO = 8;
    public static final int XDM_MO_ID_END = 12;
    public static final int XDM_MO_ID_FUMO = 11;
    public static final int XDM_MO_ID_INBOX = 10;
    public static final int XDM_MO_ID_NONE = 0;
    public static final int XDM_MO_ID_W1 = 1;
    public static final int XDM_MO_ID_W2 = 2;
    public static final int XDM_MO_ID_W3 = 3;
    public static final int XDM_MO_ID_W4 = 4;
    public static final int XDM_MO_ID_W5 = 5;
    public static final int XDM_MO_ID_W6 = 6;
    public static final int XDM_MO_ID_W7 = 7;
    public static final int XDM_TNDS_ACCESSTYPE = 69;
    public static final int XDM_TNDS_ACL = 70;
    public static final int XDM_TNDS_ADD = 71;
    public static final int XDM_TNDS_B64_FORMAT = 72;
    public static final int XDM_TNDS_BIN_FORMAT = 73;
    public static final int XDM_TNDS_BOOL_FORMAT = 74;
    public static final int XDM_TNDS_CASESENSE = 76;
    public static final int XDM_TNDS_CHR_FORMAT = 75;
    public static final int XDM_TNDS_CIS = 77;
    public static final int XDM_TNDS_COPY = 78;
    public static final int XDM_TNDS_CS = 79;
    public static final int XDM_TNDS_DATE_FORMAT = 80;
    public static final int XDM_TNDS_DDFNAME = 81;
    public static final int XDM_TNDS_DEFAULTVALUE = 82;
    public static final int XDM_TNDS_DELETE = 83;
    public static final int XDM_TNDS_DESCRIPTION = 84;
    public static final int XDM_TNDS_DFFORMAT = 85;
    public static final int XDM_TNDS_DFPROPERTIES = 86;
    public static final int XDM_TNDS_DFTITLE = 87;
    public static final int XDM_TNDS_DFTYPE = 88;
    public static final int XDM_TNDS_DYNAMIC = 89;
    public static final int XDM_TNDS_EXEC = 90;
    public static final int XDM_TNDS_FLOAT_FORMAT = 91;
    public static final int XDM_TNDS_FORMAT = 92;
    public static final int XDM_TNDS_GET = 93;
    public static final int XDM_TNDS_INCLUDE_TAG_MAX_NUM = 30;
    public static final int XDM_TNDS_INT_FORAMT = 94;
    public static final int XDM_TNDS_MAN = 95;
    public static final int XDM_TNDS_MAX = 125;
    public static final int XDM_TNDS_MGMTTREE = 96;
    public static final int XDM_TNDS_MIME = 97;
    public static final int XDM_TNDS_MOD = 98;
    public static final int XDM_TNDS_NAME = 99;
    public static final int XDM_TNDS_NOADNAME = 102;
    public static final int XDM_TNDS_NODE = 100;
    public static final int XDM_TNDS_NODE_FORAMT = 101;
    public static final int XDM_TNDS_NULL_FORMAT = 103;
    public static final int XDM_TNDS_OCCURRENCE = 104;
    public static final int XDM_TNDS_ONE = 105;
    public static final int XDM_TNDS_ONEORMORE = 106;
    public static final int XDM_TNDS_ONEORN = 107;
    public static final int XDM_TNDS_PATH = 108;
    public static final int XDM_TNDS_PERMANENT = 109;
    public static final int XDM_TNDS_REPLACE = 110;
    public static final int XDM_TNDS_RTPROPERTIES = 111;
    public static final int XDM_TNDS_SCOPE = 112;
    public static final int XDM_TNDS_SIZE = 113;
    public static final int XDM_TNDS_SYNCML_SPECIAL_VALUE = 136;
    public static final int XDM_TNDS_TAG_BRACKET_NUM = 3;
    public static final int XDM_TNDS_TAG_NAME_MAX_LEN = 20;
    public static final int XDM_TNDS_TAG_NUM = 56;
    public static final int XDM_TNDS_TIME_FORMAT = 114;
    public static final int XDM_TNDS_TITLE = 115;
    public static final int XDM_TNDS_TSTAMP = 116;
    public static final int XDM_TNDS_TYPE = 117;
    public static final int XDM_TNDS_TYPE_DATA_MAX_LEN = 16;
    public static final int XDM_TNDS_VALUE = 118;
    public static final int XDM_TNDS_VERDTD = 119;
    public static final int XDM_TNDS_VERNO = 120;
    public static final int XDM_TNDS_XML_FORMAT = 121;
    public static final int XDM_TNDS_ZEROORMORE = 122;
    public static final int XDM_TNDS_ZEROORN = 123;
    public static final int XDM_TNDS_ZEROORONE = 124;
    public static final char XML_CLOSE_TAG = '>';
    public static final int XML_ERROR = 3000;
    public static final int XML_ERR_INVALID_PARAM = 3001;
    public static final int XML_ERR_OK = 0;
    public static final int XML_ERR_PARSING_FAIL = 3002;
    public static final char XML_LINE_FEED = '\n';
    public static final String XML_NAME_SPACE_STRING = "<SyncML xmlns='syncml:dmddf1.2'>";
    public static final char XML_OPEN_TAG = '<';
    public static final char XML_SLASH = '/';
    public static final String XML_SYNCML_END_STRING = "</SyncML>";
    public static final int XML_SYNCML_HEX_VALUE = 109;
    public static final String XML_SYNCML_OPEN_TAG = "<SyncML>";
    public static final String XML_SYNCML_STRING = "SyncML";
    public static final int XML_TAG_ACESSTYPE = 5;
    public static final int XML_TAG_ACESSTYPE_STRING_END = 31;
    public static final int XML_TAG_ACESSTYPE_STRING_START = 30;
    public static final int XML_TAG_ACL = 6;
    public static final int XML_TAG_ACL_STRING_END = 15;
    public static final int XML_TAG_ACL_STRING_START = 14;
    public static final int XML_TAG_ADD = 7;
    public static final int XML_TAG_ADD_STRING_END = 21;
    public static final int XML_TAG_ADD_STRING_START = 20;
    public static final int XML_TAG_B64 = 8;
    public static final int XML_TAG_BIN = 9;
    public static final int XML_TAG_BOOL = 10;
    public static final int XML_TAG_CASESENSE = 12;
    public static final int XML_TAG_CDATA_STRING_END = 33;
    public static final int XML_TAG_CDATA_STRING_START = 32;
    public static final int XML_TAG_CHR = 11;
    public static final int XML_TAG_CIS = 13;
    public static final int XML_TAG_COPY = 14;
    public static final int XML_TAG_CS = 15;
    public static final int XML_TAG_DATE = 16;
    public static final int XML_TAG_DDFNAME = 17;
    public static final int XML_TAG_DEFAULTVALUE = 18;
    public static final int XML_TAG_DELETE = 19;
    public static final int XML_TAG_DELETE_STRING_END = 27;
    public static final int XML_TAG_DELETE_STRING_START = 26;
    public static final int XML_TAG_DESCRIPTION = 20;
    public static final int XML_TAG_DFFORMAT = 21;
    public static final int XML_TAG_DFPROPERTIES = 22;
    public static final int XML_TAG_DFTITLE = 23;
    public static final int XML_TAG_DFTYPE = 24;
    public static final int XML_TAG_DYNAMIC = 25;
    public static final int XML_TAG_EXEC = 26;
    public static final int XML_TAG_EXEC_STRING_END = 29;
    public static final int XML_TAG_EXEC_STRING_START = 28;
    public static final int XML_TAG_FLOAT = 27;
    public static final int XML_TAG_FORMAT = 28;
    public static final int XML_TAG_FORMAT_STRING_END = 17;
    public static final int XML_TAG_FORMAT_STRING_START = 16;
    public static final int XML_TAG_GET = 29;
    public static final int XML_TAG_GET_STRING_END = 23;
    public static final int XML_TAG_GET_STRING_START = 22;
    public static final int XML_TAG_INT = 30;
    public static final int XML_TAG_Identifier_STRING_END = 39;
    public static final int XML_TAG_Identifier_STRING_START = 38;
    public static final int XML_TAG_MAN = 31;
    public static final int XML_TAG_MGMTTREE = 32;
    public static final int XML_TAG_MGMTTREE_STRING_END = 1;
    public static final int XML_TAG_MGMTTREE_STRING_START = 0;
    public static final int XML_TAG_MIME = 33;
    public static final int XML_TAG_MOD = 34;
    public static final int XML_TAG_NAME = 35;
    public static final int XML_TAG_NODE = 36;
    public static final int XML_TAG_NODENAME = 38;
    public static final int XML_TAG_NODENAME_STRING_END = 7;
    public static final int XML_TAG_NODENAME_STRING_START = 6;
    public static final int XML_TAG_NODE_STRING_END = 5;
    public static final int XML_TAG_NODE_STRING_START = 4;
    public static final int XML_TAG_NULL = 39;
    public static final int XML_TAG_OCCURRENCE = 40;
    public static final int XML_TAG_ONE = 41;
    public static final int XML_TAG_ONEORMORE = 42;
    public static final int XML_TAG_ONEORN = 43;
    public static final int XML_TAG_PATH = 44;
    public static final int XML_TAG_PATH_STRING_END = 9;
    public static final int XML_TAG_PATH_STRING_START = 8;
    public static final int XML_TAG_PERMANENT = 45;
    public static final int XML_TAG_REPLACE = 46;
    public static final int XML_TAG_REPLACE_STRING_END = 25;
    public static final int XML_TAG_REPLACE_STRING_START = 24;
    public static final int XML_TAG_RTPROPERTIES = 47;
    public static final int XML_TAG_RTPROPERTIES_STRING_END = 13;
    public static final int XML_TAG_RTPROPERTIES_STRING_START = 12;
    public static final int XML_TAG_ResultCode_STRING_END = 37;
    public static final int XML_TAG_ResultCode_STRING_START = 36;
    public static final int XML_TAG_SCOPE = 48;
    public static final int XML_TAG_SIZE = 49;
    public static final int XML_TAG_SyncML_STRING_END = 35;
    public static final int XML_TAG_SyncML_STRING_START = 34;
    public static final int XML_TAG_TIME = 50;
    public static final int XML_TAG_TITLE = 51;
    public static final int XML_TAG_TSTAMP = 52;
    public static final int XML_TAG_TYPE = 53;
    public static final int XML_TAG_TYPE_STRING_END = 19;
    public static final int XML_TAG_TYPE_STRING_START = 18;
    public static final int XML_TAG_VALUE = 54;
    public static final int XML_TAG_VALUE_STRING_END = 11;
    public static final int XML_TAG_VALUE_STRING_START = 10;
    public static final int XML_TAG_VERDTD = 55;
    public static final int XML_TAG_VERDTD_STRING_END = 3;
    public static final int XML_TAG_VERDTD_STRING_START = 2;
    public static final int XML_TAG_VERNo = 56;
    public static final int XML_TAG_XML = 57;
    public static final int XML_TAG_ZEROORMORE = 58;
    public static final int XML_TAG_ZEROORN = 59;
    public static final int XML_TAG_ZEROORONE = 60;
    public static final int XML_TAG_node = 37;
    public static final String XML_VERSION_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
}
