package com.example.test;

import android.util.Log;


//Wildly stolen from http://stackoverflow.com/questions/2752472/android-how-can-i-print-a-variable-on-eclipse-console
public final class Debug{
    private Debug (){}

    public static void out (Object msg){
        Log.e ("error", "error " + msg.toString ());
    }
}