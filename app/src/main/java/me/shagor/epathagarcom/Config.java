package me.shagor.epathagarcom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
  Created by shagor on 9/19/2017.
 */

public class Config {

   public String url_link = "http://www.epathagar.com/app/get_post_bycategory.php?access_key=ctnews_sec2017&secret_key=ctnewssec2017_secure";
    public String single_url = "http://epathagar.com/app/get_single_post.php?access_key=ctnews_sec2017&secret_key=ctnewssec2017_secure";

    // Internet Connection
    public  boolean isNetworkAvailable(Context activity) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}

