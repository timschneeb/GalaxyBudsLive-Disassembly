package com.samsung.android.fotaagent.network.action;

import com.accessorydm.db.file.AccessoryInfoAdapter;
import com.accessorydm.db.file.XDBPollingAdp;
import com.samsung.android.fotaagent.network.rest.RestRequest;
import com.samsung.android.fotaagent.network.rest.RestResponse;
import com.samsung.android.fotaagent.polling.PollingInfo;
import com.samsung.android.fotaprovider.log.Log;

public class PollingAction extends NetworkAction {
    public String getBody() {
        return null;
    }

    public RestRequest.HttpMethod getMethod() {
        return RestRequest.HttpMethod.GET;
    }

    public String getURI() {
        PollingInfo xdbGetPollingInfo = XDBPollingAdp.xdbGetPollingInfo();
        AccessoryInfoAdapter accessoryInfoAdapter = new AccessoryInfoAdapter();
        String str = xdbGetPollingInfo.getPreUrl() + accessoryInfoAdapter.getSalesCode() + "/" + accessoryInfoAdapter.getModelNumber() + "/" + xdbGetPollingInfo.getVersionFileName();
        Log.H("PollingInfo URL: " + str);
        return str;
    }

    public NetworkResult getResult(RestResponse restResponse) {
        return new PollingResult(restResponse);
    }
}
