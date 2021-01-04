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
   /* access modifiers changed from: private */
   @Nullable
   public PlaceSelectListener listener;
   private RadioGroup rgType;
   private Spinner x2spin;
   private Spinner xspin;
   private Spinner y2spin;
   private Spinner yspin;

   /* renamed from: ru.sviridov.techsupervision.defects.place.PlaceDialogFragment$PlaceSelectListener */
   public interface PlaceSelectListener {
      void onPlaceSelected(Place place);
   }

   public static PlaceDialogFragment newInstance(Place place) {
      PlaceDialogFragment frgmt = new PlaceDialogFragment();
      Bundle args = new Bundle();
      args.putParcelable(PLACE, place);
      frgmt.setArguments(args);
      return frgmt;
   }

   public void setListener(@Nullable PlaceSelectListener listener2) {
      this.listener = listener2;
   }

   public void dismiss() {
      super.dismiss();
      this.listener = null;
   }

   @NonNull
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      final NumberAdapter xAdapter = new NumberAdapter(getActivity(), 100);
      final LetterAdapter yAdapter = new LetterAdapter(getActivity(), 32);
      View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_coordinates, (ViewGroup) null);
      this.xspin = (Spinner) view.findViewById(R.id.xSpinner);
      this.yspin = (Spinner) view.findViewById(R.id.ySpinner);
      this.x2spin = (Spinner) view.findViewById(R.id.x2Spinner);
      this.y2spin = (Spinner) view.findViewById(R.id.y2Spinner);
      final View secondPoint = view.findViewById(R.id.trSecond);
      this.rgType = (RadioGroup) view.findViewById(R.id.rgType);
      this.rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         public void onCheckedChanged(RadioGroup group, final int checkedId) {
            secondPoint.postDelayed(new Runnable() {
               public void run() {
                  secondPoint.setVisibility(checkedId == R.id.rbRectangular ? View.VISIBLE : View.GONE);
               }
            }, 300);
         }
      });
      this.xspin.setAdapter(xAdapter);
      this.x2spin.setAdapter(xAdapter);
      this.yspin.setAdapter(yAdapter);
      this.y2spin.setAdapter(yAdapter);
      showPlace((Place) getArguments().getParcelable(PLACE));
      return Dialogs.showCustomView(getActivity(), R.string.title_coordinates, view, R.string.apply, R.string.cancel, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialogInterface, int which) {
            if (which == -1) {
               Place place = PlaceDialogFragment.this.getPlace(xAdapter, yAdapter);
               if (PlaceDialogFragment.this.listener != null) {
                  PlaceDialogFragment.this.listener.onPlaceSelected(place);
               }
            }
         }
      });
   }

   /* access modifiers changed from: private */
   public Place getPlace(NumberAdapter xAdapter, LetterAdapter yAdapter) {
      if (this.rgType.getCheckedRadioButtonId() == R.id.rbRectangular) {
         return new Rectangle(new Point(xAdapter.getItem(this.xspin.getSelectedItemPosition()).intValue(), yAdapter.getItem(this.yspin.getSelectedItemPosition()).charValue()), new Point(xAdapter.getItem(this.x2spin.getSelectedItemPosition()).intValue(), yAdapter.getItem(this.y2spin.getSelectedItemPosition()).charValue()));
      }
      return new Point(xAdapter.getItem(this.xspin.getSelectedItemPosition()).intValue(), yAdapter.getItem(this.yspin.getSelectedItemPosition()).charValue());
   }

   private void showPlace(@Nullable Place place) {
      if (place != null) {
         if (place.getType() == Type.POINT) {
            this.rgType.check(R.id.rbPoint);
            showPoint((Point) place, this.xspin, this.yspin);
         } else if (place.getType() == Type.RECTANGLE) {
            this.rgType.check(R.id.rbRectangular);
            showRect((Rectangle) place);
         }
      }
   }

   private void showRect(Rectangle place) {
      showPoint(place.getStart(), this.xspin, this.yspin);
      showPoint(place.getFinish(), this.x2spin, this.y2spin);
   }

   private void showPoint(Point point, Spinner xspin2, Spinner yspin2) {
      int selectedX = 0;
      int selectedY = 0;
      if (point != null) {
         selectedX = point.getX() - 1;
         selectedY = point.getY() - 1040;
      }
      xspin2.setSelection(selectedX);
      yspin2.setSelection(selectedY);
   }
}
