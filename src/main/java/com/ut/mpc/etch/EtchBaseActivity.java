package com.ut.mpc.etch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EtchBaseActivity extends Activity {
    private static final String EVAL_NAME_TAG = "eval";
    private static final String TAG = EtchBaseActivity.class.getSimpleName();

    private Map<Integer, Map<String, String>> evalPlan = new HashMap<Integer, Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_run);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
            Log.d(TAG, extras.toString());
            String rawPlan = extras.getString("plan");

            String planStr = "";
            try {
                planStr = java.net.URLDecoder.decode(rawPlan, "UTF-8");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            try {
                JSONArray plan = new JSONArray(planStr);
                for (int i = 0; i < plan.length(); i++) {
                    JSONObject entry = plan.getJSONObject(i);
                    runEval(entry);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "*** ETCH EVALS COMPLETE ***");
            Intent myIntent = new Intent(EtchBaseActivity.this, EtchCompleteActivity.class);
            EtchBaseActivity.this.startActivity(myIntent);
        }
    }

    private void runEval(JSONObject options) throws JSONException {
        String evalClassName = options.getString(EVAL_NAME_TAG);
        Log.d(TAG, "--------------------------------");
        Log.d(TAG, "Test name: " + evalClassName);
        for(int i = 0; i< options.names().length(); i++){
            Log.v(TAG, "key = " + options.names().getString(i) + " value = " + options.get(options.names().getString(i)));
        }

        try {
            Object newObject = Class.forName(Setup.EVAL_DIR + "." + evalClassName).newInstance();
            ((Eval) newObject).execute(this, options);
        } catch (Exception e){
            //TODO: better catching of exceptions here for json exceptions
            e.printStackTrace();
        }
    }
}
