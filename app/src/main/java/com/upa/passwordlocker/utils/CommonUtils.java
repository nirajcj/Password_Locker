package com.upa.passwordlocker.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

public class CommonUtils {

    public static boolean hasWhiteSpace(String data) {

        if(data == null) {
            return false;
        }

        return data.contains(" ");
    }

    public static boolean startActivity(Context context, Intent intent) {
        try {
            context.startActivity(intent);
            return true;
        }
        catch (ActivityNotFoundException e) {
            return false;
        }
    }

}
