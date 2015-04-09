package com.ut.mpc.etch;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MultiProfiler {


    private static Context appContext;
    //private static Stabilizer mStabilizer;

    private static final String TAG = MultiProfiler.class.getSimpleName();

    private static String deviceLabel;

    public MultiProfiler() {

    }

    public static void init(Eval testClass, Context testContext) {
        //mStabilizer = (Stabilizer) testClass;
        appContext =  testContext;
        Intent trepn = new Intent();
        String model= Build.MODEL;
        String serial = Build.SERIAL;
        String modelConcat = model.replaceAll("\\s","");
        String serialStrip = serial.substring(serial.length()-3);
        Log.d(TAG, "Model: "+modelConcat);
        Log.d(TAG, "Serial: "+serialStrip);
        deviceLabel = modelConcat+"_"+serialStrip;

        // Start Trepn
        trepn.setClassName("com.quicinc.trepn", "com.quicinc.trepn.TrepnService");
        appContext.startService(trepn);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startProfiling(String dbName) {
        String safeDbName = dbName.replaceAll("\\s+","");
        Intent createDatabase = new Intent("com.quicinc.trepn.start_profiling");
        String timeAndDate = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
        String dbFullName = safeDbName + "_" + deviceLabel + "_" + timeAndDate;
        Log.d(TAG, "dB full name: "+dbFullName);
        createDatabase.putExtra("com.quicinc.trepn.database_file", dbFullName);
        appContext.sendBroadcast(createDatabase);
        try {
            Thread.sleep(Setup.PROF_WARMUP_DURATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stopProfiling() {
        Intent stopProfiling = new Intent("com.quicinc.trepn.stop_profiling");
        appContext.sendBroadcast(stopProfiling);
    }

    public static int startMark(Stabilizer mStabilizer, Object data, String desc) {
        stabilizeTask(mStabilizer, data);
        Intent trepn = new Intent("com.quicinc.Trepn.UpdateAppState");
        int trepnStartCode = Math.abs(desc.hashCode() % 1000);
        trepn.putExtra("com.quicinc.Trepn.UpdateAppState.Value", trepnStartCode);
        trepn.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", desc);
        appContext.sendBroadcast(trepn);
        return trepnStartCode;
    }

    public static int endMark(String desc) {
        Intent trepn = new Intent("com.quicinc.Trepn.UpdateAppState");
        //int trepnEndCode = Math.abs(desc.hashCode() % 1000) + 1;
        int trepnEndCode = 0;
        trepn.putExtra("com.quicinc.Trepn.UpdateAppState.Value", trepnEndCode);
        trepn.putExtra("com.quicinc.Trepn.UpdateAppState.Value.Desc", desc);
        appContext.sendBroadcast(trepn);
        return trepnEndCode;
    }

    public static void loadPrefs(String prefName) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent loadPreferences = new Intent("com.quicinc.trepn.load_preferences");
        loadPreferences.putExtra("com.quicinc.trepn.load_preferences_file", prefName);
        appContext.sendBroadcast(loadPreferences);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stabilizeTask(Stabilizer mStabilizer, Object data) {
        if(Setup.STABILIZE_MODE == Setup.StabilizeMode.OFF){ return; }

        long startTime = 0;
        long endTime = 1000000;
        long execTime = 0;
        long runningSum = 0;
        long count = 0;
        long avg = 0;
        do {
            startTime = System.currentTimeMillis();
            mStabilizer.task(data);
            endTime = System.currentTimeMillis();
            execTime = endTime - startTime;
            runningSum += execTime;
            count++;
            avg = runningSum / count;
        } while(checkStabilizerCondition(count, Setup.STABILIZE_LOOP_COUNT, avg, execTime, Setup.STABILIZE_THRESH));
    }

    private static boolean checkStabilizerCondition(long count, long minCount, long avg, long execTime, long stabilizeThresh){
        if(Setup.STABILIZE_MODE == Setup.StabilizeMode.FIXED_LOOP){
            return (count < minCount);
        } else if(Setup.STABILIZE_MODE == Setup.StabilizeMode.TOLERANCE){
            return (Math.abs(avg - execTime) > stabilizeThresh);
        } else if(Setup.STABILIZE_MODE == Setup.StabilizeMode.BOTH){
            return (count < minCount) || (Math.abs(avg - execTime) > stabilizeThresh);
        } else {
            return false;
        }
    }

}

