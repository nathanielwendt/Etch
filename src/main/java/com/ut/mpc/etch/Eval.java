package com.ut.mpc.etch;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nathanielwendt on 3/4/15.
 */
public interface Eval {
    public final String TAG = "com/mpc/etch";
    public void execute(Context ctx, JSONObject options) throws JSONException;
}
