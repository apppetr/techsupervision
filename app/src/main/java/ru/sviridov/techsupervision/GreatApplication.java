package ru.sviridov.techsupervision;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;


import com.android.volley.VolleyLog;
import ru.sviridov.techsupervision.service.StrongMan;
import ru.sviridov.techsupervision.service.UpdateService;

public class GreatApplication extends Application {
   private static Context appContext = null;
   //TODO CORRECT public class PermissionController isPermissionGranted
   //TODO reasons2compensations нет компенсирующих мероприятий
   //TODO не выставляется обьем и не сохраняются отметки на фото дефекта
   //TODO при добавлении дефекта впервые не сохраняет фото error urifromfile
   @NonNull
   public static Context getAppContext() {
      return appContext;
   }

   public void onCreate() {
      super.onCreate();
     // initMint();
      appContext = this;
      System.setProperty("org.xml.sax.driver", "ru.sviridov.techsupervision.docx.SAX2Parser");
      StrongMan.init(this);
      VolleyLog.DEBUG = false;
      UpdateService.call(this);
   }

  // private void initMint() {
    //  Mint.disableNetworkMonitoring();
    //  Mint.initAndStartSession(this, getString(R.string.mint_api_key));
  // }
}
