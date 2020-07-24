package com.samsung.android.fotaagent.network.action;

import com.samsung.android.fotaagent.network.rest.RestRequest;
import com.samsung.android.fotaagent.network.rest.RestResponse;
import com.samsung.android.fotaprovider.util.OperatorUtil;

public class DeviceRegistrationAction extends NetworkAction {
    private static final String URL_FUMO_DEVICE = "/device/fumo/device/";
    private static final String URL_OSP_SERVER = ".ospserver.net";

    public RestRequest.HttpMethod getMethod() {
        return RestRequest.HttpMethod.POST;
    }

    public String getURI() {
        return "https://" + OperatorUtil.getUrlPrefix() + URL_OSP_SERVER + URL_FUMO_DEVICE;
    }

    public String getBody() {
        return DeviceRegistrationBody.get();
    }

    public NetworkResult getResult(RestResponse restResponse) {
        return new DeviceRegistrationResult(restResponse);
    }
}
