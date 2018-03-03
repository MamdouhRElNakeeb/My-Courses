package com.mycoursesapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.mycoursesapp.activity.QRScanner;

import java.util.regex.Pattern;

/**
 * Created by mamdouhelnakeeb on 11/28/17.
 */

public class Utils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isValidEmaill(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {

        /**
         * at least:
         * 1 Number (?=.*[0-9])
         * 1 small character (?=.*[a-z])
         * 6 characters minimum
         * */

        return Pattern.compile("^(?=\\S+$).{6,}$").matcher(password).matches();

    }

    public static boolean isValidName(String name){

        return Pattern.compile("^(?=.*[a-z])(?=\\S+$).{6,}$").matcher(name).matches();
    }

    public static double kgToLbs(double kgVal){

        return kgVal * 2.20462;
    }

    public static double cmToInch(double cmVal){
        return cmVal * 0.393701;
    }


    public static void askForPermission(Activity context, String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, QRScanner.class);
            context.startActivity(intent);
        }
    }
}
