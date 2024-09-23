package com.example.beautyproduct.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkDetails {

    public static boolean isConnectedToInternet(Context context)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null)
            {
//                for(init, condition,i++ or i--)1<1
                for (int i = 0; i<networkInfo.length;i++)
                {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
