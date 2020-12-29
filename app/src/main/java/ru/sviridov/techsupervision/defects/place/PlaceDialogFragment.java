package ru.sviridov.techsupervision.defects.place;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import ru.sviridov.techsupervision.defects.place.object.Place;
import ru.sviridov.techsupervision.defects.place.object.Point;
import ru.sviridov.techsupervision.defects.place.object.Rectangle;
import ru.sviridov.techsupervision.defects.place.object.Type;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.utils.alerts.Dialogs;

public class PlaceDialogFragment extends DialogFragment {
   public static final String PLACE = "ru.sviridov.techsupervision.PLACE";
   @Nullable
   private PlaceDialogFragment.PlaceSelectListener listener;
   private RadioGroup rgType;
   private Spinner x2spin;
   private Spinner xspin;
   private Spinner y2spin;
   private Spinner yspin;

   private Place getPlace(NumberAdapter var1, LetterAdapter var2) {
      Object var3;
      if (this.rgType.getCheckedRadioButtonId() == R.id.rbRectangular) {
         var3 = new Rectangle(new Point(var1.getItem(this.xspin.getSelectedItemPosition()), var2.getItem(this.yspin.getSelectedItemPosition())), new Point(var1.getItem(this.x2spin.getSelectedItemPosition()), var2.getItem(this.y2spin.getSelectedItemPosition())));
      } else {
         var3 = new Point(var1.getItem(this.xspin.getSelectedItemPosition()), var2.getItem(this.yspin.getSelectedItemPosition()));
      }

      return (Place)var3;
   }

   public static PlaceDialogFragment newInstance(Place var0) {
      PlaceDialogFragment var1 = new PlaceDialogFragment();
      Bundle var2 = new Bundle();
      var2.putParcelable("ru.sviridov.techsupervision.PLACE", var0);
      var1.setArguments(var2);
      return var1;
   }

   private void showPlace(@Nullable Place var1) {
      if (var1 != null) {
         if (var1.getType() == Type.POINT) {
            this.rgType.check(R.id.rbPoint);
            this.showPoint((Point)var1, this.xspin, this.yspin);
         } else if (var1.getType() == Type.RECTANGLE) {
            this.rgType.check(R.id.rbRectangular);
            this.showRect((Rectangle)var1);
         }
      }

   }

   private void showPoint(Point var1, Spinner var2, Spinner var3) {
      int var4 = 0;
      int var5 = 0;
      if (var1 != null) {
         var4 = var1.getX() - 1;
         var5 = var1.getY() - 1040;
      }

      var2.setSelection(var4);
      var3.setSelection(var5);
   }

   private void showRect(Rectangle var1) {
      this.showPoint(var1.getStart(), this.xspin, this.yspin);
      this.showPoint(var1.getFinish(), this.x2spin, this.y2spin);
   }

   public void dismiss() {
      super.dismiss();
      this.listener = null;
   }

   @NonNull
   public Dialog onCreateDialog(Bundle var1) {
      final NumberAdapter var2 = new NumberAdapter(this.getActivity(), 100);
      final LetterAdapter var3 = new LetterAdapter(this.getActivity(), 32);
      View var5 = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_coordinates, (ViewGroup)null);
      this.xspin = (Spinner)var5.findViewById(R.id.xSpinner);
      this.yspin = (Spinner)var5.findViewById(R.id.ySpinner);
      this.x2spin = (Spinner)var5.findViewById(R.id.x2Spinner);
      this.y2spin = (Spinner)var5.findViewById(R.id.y2Spinner);
      final View var4 = var5.findViewById(R.id.trSecond);
      this.rgType = (RadioGroup)var5.findViewById(R.id.rgType);
      this.rgType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(RadioGroup var1, final int var2) {
            var4.postDelayed(new Runnable() {
               public void run() {
                  View var1 = var4;
                  byte var2x;
                  if (var2 == R.id.rbRectangular) {
                     var2x = 0;
                  } else {
                     var2x = 8;
                  }

                  var1.setVisibility(var2x);
               }
            }, 300L);
         }
      });
      this.xspin.setAdapter(var2);
      this.x2spin.setAdapter(var2);
      this.yspin.setAdapter(var3);
      this.y2spin.setAdapter(var3);
      this.showPlace((Place)this.getArguments().getParcelable("ru.sviridov.techsupervision.PLACE"));
      OnClickListener var6 = new OnClickListener() {
         public void onClick(DialogInterface var1, int var2x) {
            if (var2x == -1) {
               Place var3x = PlaceDialogFragment.this.getPlace(var2, var3);
               if (PlaceDialogFragment.this.listener != null) {
                  PlaceDialogFragment.this.listener.onPlaceSelected(var3x);
               }
            }

         }
      };
      return Dialogs.showCustomView(this.getActivity(), R.string.title_coordinates, var5, R.string.apply, R.string.cancel, var6);
   }

   public void setListener(@Nullable PlaceDialogFragment.PlaceSelectListener var1) {
      this.listener = var1;
   }

   public interface PlaceSelectListener {
      void onPlaceSelected(Place var1);
   }
}
