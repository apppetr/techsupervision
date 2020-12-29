package ru.sviridov.techsupervision.utils.alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cab404.jsonm.impl.JSONUtils;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sviridov.techsupervision.utils.SelectListener;

public class SelectDialogs {
   private static final JSONObject measurementsContent;

   static {
      try {
         JSONObject var0 = new JSONObject("{\"площадь\": {\"мм²\":null,\"см²\":null,\"м²\":null}, \"длина\": {\"мм\":null,\"см\":null,\"м\":null}, \"глубина\": {\"мм\":null,\"см\":null,\"м\":null}}");
         measurementsContent = var0;
      } catch (JSONException var1) {
         throw new RuntimeException("", var1);
      }
   }

   protected static void fill(final JSONObject var0, final int var1, final SelectListener var2, final LinearLayout... var3) {
      final LinearLayout var4 = var3[var1];
      var4.removeAllViews();

      TextView var7;
      for(Iterator var5 = JSONUtils.getIterable(var0.keys()).iterator(); var5.hasNext(); var4.addView(var7)) {
         final String var6 = (String)var5.next();
         var7 = (TextView)LayoutInflater.from(var4.getContext()).inflate(2130903098, var4, false);
         var7.setText(var6);
         if (var1 == var3.length - 1) {
            var7.setOnClickListener(new OnClickListener() {
               public void onClick(View var1) {
                  var2.onSelected(var6);

                  for(int var2x = 0; var2x < var4.getChildCount(); ++var2x) {
                     var4.getChildAt(var2x).setSelected(false);
                  }

                  var1.setSelected(true);
               }
            });
         } else {
            var7.setOnClickListener(new OnClickListener() {
               public void onClick(View var1x) {
                  SelectDialogs.fill(var0.optJSONObject(var6), var1 + 1, var2, var3);

                  for(int var2x = 0; var2x < var4.getChildCount(); ++var2x) {
                     var4.getChildAt(var2x).setSelected(false);
                  }

                  var1x.setSelected(true);
               }
            });
         }
      }

   }

   public static AlertDialog showMeasumentDialog(Context var0, SelectListener var1) {
      return showSelectDialog(var0, measurementsContent, 2131099730, 2131099733, 2131099683, var1);
   }

   private static AlertDialog showSelectDialog(Context var0, @NonNull JSONObject var1, @StringRes int var2, @StringRes int var3, @StringRes int var4, final SelectListener var5) {
      final View var6 = LayoutInflater.from(var0).inflate(2130903108, (ViewGroup)null);
      LinearLayout var7 = (LinearLayout)var6.findViewById(2131558595);
      LinearLayout var8 = (LinearLayout)var6.findViewById(2131558597);
      final AtomicReference var9 = new AtomicReference();
      fill(var1, 0, new SelectListener() {
         @Override
         public void onSelected(Object var1) {

         }

         public void onSelected(String var1) {
            var9.set(var1);
         }
      }, var7, var8);
      return Dialogs.showCustomView(var0, var2, var6, var3, var4, new android.content.DialogInterface.OnClickListener() {
         public void onClick(@NonNull DialogInterface var1, int var2) {
            if (var2 == -1) {
               if (var9.get() != null) {
                  EditText var3 = (EditText)var6.findViewById(2131558596);
                  String var4;
                  if (TextUtils.isEmpty(var3.getText())) {
                     var4 = "0";
                  } else {
                     var4 = var3.getText().toString();
                  }

                  var5.onSelected(var4 + ' ' + (String)var9.get());
               } else {
                  var5.onSelected("");
               }
            }

         }
      });
   }
}
