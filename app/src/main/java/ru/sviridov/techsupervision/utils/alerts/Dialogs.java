package ru.sviridov.techsupervision.utils.alerts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

/* renamed from: ru.sviridov.techsupervision.utils.alerts.Dialogs */
public class Dialogs {
   public static AlertDialog show(@NonNull Context context, @StringRes int titleId, @StringRes int messageId, @StringRes int okId, @StringRes int noId, @Nullable DialogInterface.OnClickListener listener) {
      return new AlertDialog.Builder(context).setTitle(titleId).setMessage(messageId).setPositiveButton(okId, listener).setNegativeButton(noId, listener).show();
   }

   public static AlertDialog showCustomView(@NonNull Context context, @StringRes int titleId, @NonNull View view, @StringRes int okId, @StringRes int noId, @Nullable DialogInterface.OnClickListener listener) {
      return new AlertDialog.Builder(context).setTitle(titleId).setView(view).setPositiveButton(okId, listener).setNegativeButton(noId, listener).show();
   }
}
