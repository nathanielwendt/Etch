package com.ut.mpc.etch;

/**
 * Created by nathanielwendt on 3/28/15.
 */
public class Setup {
    //package application id
    public static final String BASE_DIR = "com.example.nathanielwendt.etchtest";

    //directory which contains your 'eval' classes
    public static final String EVAL_DIR = "com.example.nathanielwendt.etchtest.evals";

    //number of ms to wait after trepn has been called before running tests
    //this time is typically used to generate a baseline for energy measurements
    public static final int PROF_WARMUP_DURATION = 5000;

    //Mode used by the stabilizer function
    // Fixed Loop - run the stabilizer a fixed number of times (3)
    // Tolerance - run the stabilizer until the execution time of the result is within STABILIZE_THRESH of the previous iteration
    // Both - Both fixed loop and tolerance
    // Off - No stabilizer will be used
    public enum StabilizeMode{FIXED_LOOP,TOLERANCE,BOTH,OFF};
    public static final StabilizeMode STABILIZE_MODE = StabilizeMode.FIXED_LOOP;

    public static final int STABILIZE_THRESH = 10; // in ms
    public static final int STABILIZE_LOOP_COUNT = 3; // in iterations
}
