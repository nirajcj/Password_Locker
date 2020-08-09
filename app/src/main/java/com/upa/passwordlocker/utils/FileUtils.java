package com.upa.passwordlocker.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileUtils {

    private static final String TAG = FileUtils.class.getName();

    public static void writeToFile(Context mContext, String data, String filename) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "Error writing file to disc", e.getCause());
        }
    }

    public static String readFromFile(Context mContext, String filename) {

        String ret = "";

        try {

            InputStream inputStream = mContext.openFileInput(filename);

            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Error reading file from disc", e.getCause());
        }
        return ret;
    }

}
