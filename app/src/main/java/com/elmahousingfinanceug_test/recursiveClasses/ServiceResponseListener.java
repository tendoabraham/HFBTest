package com.elmahousingfinanceug_test.recursiveClasses;

import org.json.JSONObject;

public interface ServiceResponseListener {
    void onError(String message);

    void onResponse(String response);

    void onResponseJson(JSONObject response);
}
