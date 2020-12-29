package ru.sviridov.techsupervision;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;


import com.android.volley.VolleyLog;
import ru.sviridov.techsupervision.service.StrongMan;
import ru.sviridov.techsupervision.service.UpdateService;
import ru.sviridov.techsupervision.R;
public class GreatApplication extends Application {
   private static Context appContext = null;

   @NonNull
   public static Context getAppContext() {
      return appContext;
   }

 //  private void initMint() {
  //    Mint.disableNetworkMonitoring();
   //   Mint.initAndStartSession(this, this.getString(R.string.mint_api_key));
  // }

   public void onCreate() {
      super.onCreate();
     // this.initMint();
      appContext = this;
      System.setProperty("org.xml.sax.driver", "ru.sviridov.techsupervision.docx.SAX2Parser");
      StrongMan.init(this);
      VolleyLog.DEBUG = false;
      UpdateService.call(this);
   }
}
