package ru.lihachev.norm31937.utils.alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View.OnClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cab404.jsonm.impl.JSONUtils;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONException;
import org.json.JSONObject;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.utils.SelectListener;

/* renamed from: ru.lihachev.norm31937.utils.alerts.SelectDialogs */
public class SelectDialogs {
   private static final JSONObject measurementsContent;

   static {
      try {
          measurementsContent = new JSONObject("{\"площадь\": {\"мм²\":null,\"см²\":null,\"м²\":null}, \"длина\": {\"мм\":null,\"см\":null,\"м\":null}, \"глубина\": {\"мм\":null,\"см\":null,\"м\":null}}");
      } catch (JSONException e) {
         throw new RuntimeException("", e);
      }
   }

   protected static void fill(final JSONObject obj, final int offset, final SelectListener redirectLast, final LinearLayout... what) {
      final LinearLayout current = what[offset];
      current.removeAllViews();

      TextView view;
      for(Iterator<String> strIter = JSONUtils.getIterable(obj.keys()).iterator(); strIter.hasNext(); current.addView(view)) {
         final String textInput = strIter.next();
         view = (TextView)LayoutInflater.from(current.getContext()).inflate(R.layout.measure_view, current, false);
         view.setText(textInput);
         if (offset == what.length - 1) {
            view.setOnClickListener(new OnClickListener() {
               public void onClick(View offset) {
                  redirectLast.onSelected(textInput);

                  for(int i = 0; i < current.getChildCount(); ++i) {
                     current.getChildAt(i).setSelected(false);
                  }

                  offset.setSelected(true);
               }
            });
         } else {
            view.setOnClickListener(new OnClickListener() {
               public void onClick(View var1x) {
                  SelectDialogs.fill(obj.optJSONObject(textInput), offset + 1, redirectLast, what);

                  for(int i = 0; i < current.getChildCount(); ++i) {
                     current.getChildAt(i).setSelected(false);
                  }

                  var1x.setSelected(true);
               }
            });
         }
      }

   }

   public static AlertDialog showMeasumentDialog(Context ctx, SelectListener handler) {
      return showSelectDialog(ctx, measurementsContent, R.string.measurement, R.string.next, R.string.cancel, handler);
   }

   private static AlertDialog showSelectDialog(Context ctx, @NonNull JSONObject content, @StringRes int titleId, @StringRes int okId, @StringRes int noId, final SelectListener handler) {
      final View edSelDialog = LayoutInflater.from(ctx).inflate(R.layout.photo_select_measure, (ViewGroup)null);
      LinearLayout measDim = (LinearLayout)edSelDialog.findViewById(R.id.measureDimen);
      LinearLayout meas = (LinearLayout)edSelDialog.findViewById(R.id.measure);
      final AtomicReference AtRef = new AtomicReference();
      fill(content, 0, new SelectListener() {
         @Override
         public void onSelected(Object content) {
            AtRef.set(content);
         }

      }, measDim, meas);
      return Dialogs.showCustomView(ctx, titleId, edSelDialog, okId, noId, new android.content.DialogInterface.OnClickListener() {
         public void onClick(@NonNull DialogInterface content, int titleId) {
            if (titleId == -1) {
               if (AtRef.get() != null) {
                  EditText okId = (EditText)edSelDialog.findViewById(R.id.etValue);
                  String noId;
                  if (TextUtils.isEmpty(okId.getText())) {
                     noId = "0";
                  } else {
                     noId = okId.getText().toString();
                  }

                  handler.onSelected(noId + ' ' + (String)AtRef.get());
               } else {
                  handler.onSelected("");
               }
            }

         }
      });
   }
}