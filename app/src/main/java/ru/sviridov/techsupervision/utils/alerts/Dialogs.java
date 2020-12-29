package ru.sviridov.techsupervision.utils.alerts;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class Dialogs {
   public static AlertDialog show(@NonNull Context var0, @StringRes int var1, @StringRes int var2, @StringRes int var3, @StringRes int var4, @Nullable OnClickListener var5) {
      return (new AlertDialog.Builder(var0)).setTitle(var1).setMessage(var2).setPositiveButton(var3, var5).setNegativeButton(var4, var5).show();
   }

   public static AlertDialog showCustomView(@NonNull Context var0, @StringRes int var1, @NonNull View var2, @StringRes int var3, @StringRes int var4, @Nullable OnClickListener var5) {
      return (new AlertDialog.Builder(var0)).setTitle(var1).setView(var2).setPositiveButton(var3, var5).setNegativeButton(var4, var5).show();
   }
}
