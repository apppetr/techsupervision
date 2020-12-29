package ru.sviridov.techsupervision.defects;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.List;
import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.ProviderCompartment;
import ru.sviridov.techsupervision.GreatApplication;
import ru.sviridov.techsupervision.Helper;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.defects.place.PlaceDialogFragment;
import ru.sviridov.techsupervision.defects.place.object.Place;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.MaterialVariant;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.utils.Formats;
import ru.sviridov.techsupervision.utils.SelectListener;
import ru.sviridov.techsupervision.utils.alerts.Dialogs;
import ru.sviridov.techsupervision.utils.alerts.SelectDialogs;
import ru.sviridov.techsupervision.values.ValuesProvider;

public class DefectDetailsFragment extends Fragment implements ElementController.ElementListener, PlaceDialogFragment.PlaceSelectListener {
   private Spinner category;
   private Defect defect;
   private ElementController elementController;
   private TextView tvCompensation;
   private TextView tvCoord;
   private TextView tvDefect;
   private TextView tvElemMat;
   private TextView tvReason;
   private TextView tvVolume;

   private void deleteCompensations(Variant... var1) {
      String var2 = Formats.formatArray(Formats.extractIds(var1));
      ProviderCompartment var3 = CupboardFactory.cupboard().withContext(this.getActivity());
      var3.delete(ValuesProvider.uri("reasons2compensations"), "compensation_id in (" + var2 + ")", (String[])null);
      var3.delete(ValuesProvider.uri("compensations"), "_id in (" + var2 + ")", (String[])null);
   }

   private void deleteElement(Variant var1, MaterialVariant var2) {
      ProviderCompartment var3 = CupboardFactory.cupboard().withContext(this.getActivity());
      var3.delete(ValuesProvider.uri("defects2elements"), "mat_elem_id=?", String.valueOf(var2.getMatElemId()));
      var3.delete(ValuesProvider.uri("elements2materials"), "mat_elem_id=?", String.valueOf(var2.getMatElemId()));
      var3.delete(ValuesProvider.uri("elements"), "_id=?", String.valueOf(var1.getId()));
      var3.delete(ValuesProvider.uri("materials"), "_id=?", String.valueOf(var2.getId()));
   }

   private void deleteProblems(Variant[] var1) {
      String var3 = Formats.formatArray(Formats.extractIds(var1));
      ProviderCompartment var2 = CupboardFactory.cupboard().withContext(this.getActivity());
      var2.delete(ValuesProvider.uri("defects2reasons"), "defect_id in (" + var3 + ")", (String[])null);
      var2.delete(ValuesProvider.uri("defects2elements"), "defect_id in (" + var3 + ")", (String[])null);
      var2.delete(ValuesProvider.uri("defects"), "_id in (" + var3 + ")", (String[])null);
   }

   private void deleteReasons(Variant[] var1) {
      String var3 = Formats.formatArray(Formats.extractIds(var1));
      ProviderCompartment var2 = CupboardFactory.cupboard().withContext(this.getActivity());
      var2.delete(ValuesProvider.uri("defects2reasons"), "reason_id in (" + var3 + ")", (String[])null);
      var2.delete(ValuesProvider.uri("reasons2compensations"), "reason_id in (" + var3 + ")", (String[])null);
      var2.delete(ValuesProvider.uri("reasons"), "_id in (" + var3 + ")", (String[])null);
   }

   private void fillViews(Defect var1) {
      if (var1.getElement() != null) {
         this.tvElemMat.setText(this.getString(R.string.format_document_list, new Object[]{var1.getElement(), var1.getMaterial()}));
      } else {
         this.tvElemMat.setText("");
      }

      if (var1.place != null) {
         this.tvCoord.setText(var1.place.toString());
      }

      if (!TextUtils.isEmpty(var1.getCategory())) {
         this.category.setSelection(var1.getCategory().charAt(0) - 1040 + 1);
      }

      this.tvDefect.setText(var1.getNiceProblems());
      this.tvReason.setText(var1.getNiceReasons());
      this.tvCompensation.setText(var1.getNiceCompensations());
      this.tvVolume.setText(var1.getVolume());
   }

   public static DefectDetailsFragment getInstance(int var0) {
      DefectDetailsFragment var1 = new DefectDetailsFragment();
      Bundle var2 = new Bundle();
      var2.putInt("ru.sviridov.techsupervision.DEFECT_ID", var0);
      var1.setArguments(var2);
      return var1;
   }

   private void initLongClickListeners(View var1, View var2, View var3, View var4) {
      var1.setOnLongClickListener(new OnLongClickListener() {
         public boolean onLongClick(View var1) {
            boolean var2;
            if (DefectDetailsFragment.this.defect.getElement() != null && DefectDetailsFragment.this.defect.getMaterial() != null) {
               var2 = DefectDetailsFragment.this.showDeleteElementDialog();
            } else {
               var2 = false;
            }

            return var2;
         }
      });
      var2.setOnLongClickListener(new OnLongClickListener() {
         public boolean onLongClick(View var1) {
            return DefectDetailsFragment.this.showDeleteProblemsDialog();
         }
      });
      var3.setOnLongClickListener(new OnLongClickListener() {
         public boolean onLongClick(View var1) {
            return DefectDetailsFragment.this.showDeleteReasonDialog();
         }
      });
      var4.setOnLongClickListener(new OnLongClickListener() {
         public boolean onLongClick(View var1) {
            return DefectDetailsFragment.this.showDeleteCompensationDialog();
         }
      });
   }

   private void initViews(View var1) {
      this.tvElemMat = (TextView)var1.findViewById(R.id.tvElemMat);
      this.tvCoord = (TextView)var1.findViewById(R.id.tvCoord);
      this.category = (Spinner)var1.findViewById(R.id.sCategory);
      this.tvDefect = (TextView)var1.findViewById(R.id.tvDefect);
      this.tvReason = (TextView)var1.findViewById(R.id.tvReasons);
      this.tvCompensation = (TextView)var1.findViewById(R.id.tvCompensations);
      this.tvVolume = (TextView)var1.findViewById(R.id.tvVolume);
   }

   private void saveDefect() {
      this.defect.updated = System.currentTimeMillis();
      ContentValues var1 = CupboardFactory.cupboard().withEntity(Defect.class).toContentValues(this.defect);
      CupboardFactory.cupboard().withContext(this.getActivity()).update(UserDataProvider.getContentUri("Defect"), var1);
   }

   private boolean showDeleteCompensationDialog() {
      Dialogs.show(this.getActivity(), R.string.delete_compensation, R.string.delete_compensation_message, R.string.yes, R.string.cancel, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
            if (var2 == -1) {
               DefectDetailsFragment.this.deleteCompensations(DefectDetailsFragment.this.defect.compensations);
               DefectDetailsFragment.this.defect.compensations = null;
               DefectDetailsFragment.this.saveDefect();
               DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
            }

         }
      });
      return true;
   }

   private boolean showDeleteElementDialog() {
      Dialogs.show(this.getActivity(), R.string.delete_element, R.string.delete_element_message, R.string.yes, R.string.cancel, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
            if (var2 == -1) {
               DefectDetailsFragment.this.deleteElement(DefectDetailsFragment.this.defect.getElement(), DefectDetailsFragment.this.defect.getMaterial());
               DefectDetailsFragment.this.defect.setElement((Variant)null);
               DefectDetailsFragment.this.defect.setMaterial((MaterialVariant)null);
               DefectDetailsFragment.this.defect.compensations = null;
               DefectDetailsFragment.this.defect.problems = null;
               DefectDetailsFragment.this.defect.reasons = null;
               DefectDetailsFragment.this.saveDefect();
               DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
            }

         }
      });
      return true;
   }

   private boolean showDeleteProblemsDialog() {
      Dialogs.show(this.getActivity(), R.string.delete_problem, R.string.delete_problem_message, R.string.yes, R.string.cancel, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
            if (var2 == -1) {
               DefectDetailsFragment.this.deleteProblems(DefectDetailsFragment.this.defect.problems);
               DefectDetailsFragment.this.defect.problems = null;
               DefectDetailsFragment.this.defect.reasons = null;
               DefectDetailsFragment.this.defect.compensations = null;
               DefectDetailsFragment.this.saveDefect();
               DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
            }

         }
      });
      return true;
   }

   private boolean showDeleteReasonDialog() {
      Dialogs.show(this.getActivity(), R.string.delete_reason, R.string.delete_reason_message, R.string.yes, R.string.cancel, new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
            if (var2 == -1) {
               DefectDetailsFragment.this.deleteReasons(DefectDetailsFragment.this.defect.reasons);
               DefectDetailsFragment.this.defect.reasons = null;
               DefectDetailsFragment.this.defect.compensations = null;
               DefectDetailsFragment.this.saveDefect();
               DefectDetailsFragment.this.fillViews(DefectDetailsFragment.this.defect);
            }

         }
      });
      return true;
   }

   @SuppressLint("ResourceType")
   public SpinnerAdapter createAdapter(@StringRes int var1, @NonNull String[] var2) {
      String var3 = this.getString(var1);
      String[] var4 = new String[var2.length + 1];
      var4[0] = var3;

      for(var1 = 0; var1 < var2.length; ++var1) {
         var4[var1 + 1] = var2[var1];
      }

      return new ArrayAdapter(this.getActivity(), 2130903114, var4) {
         public boolean areAllItemsEnabled() {
            return false;
         }

         public boolean isEnabled(int var1) {
            boolean var2;
            if (var1 != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      };
   }

   public void elementChanged(Variant var1, MaterialVariant var2) {
      this.tvElemMat.setText(this.getString(R.string.format_document_list, new Object[]{var1, var2}));
      this.tvDefect.setText("");
      this.tvReason.setText("");
      this.tvCompensation.setText("");
      this.defect.setElement(var1);
      this.defect.setMaterial(var2);
      this.defect.problems = null;
      this.defect.reasons = null;
      this.defect.compensations = null;
      this.saveDefect();
   }

   public SpinnerAdapter get(String var1, List var2) {
      int var3 = var2.size();
      byte var4;
      if (var1 == null) {
         var4 = 0;
      } else {
         var4 = 1;
      }

      String[] var5 = new String[var4 + var3];

      for(int var6 = 0; var6 < var2.size(); ++var6) {
         var5[var6 + 1] = ((Variant)var2.get(var6)).getName();
      }

      var5[0] = var1;
      return new ArrayAdapter(this.getActivity(), 2130903114, var5) {
         public boolean areAllItemsEnabled() {
            return false;
         }

         public boolean isEnabled(int var1) {
            boolean var2;
            if (var1 != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      };
   }

   public void onActivityResult(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      if (var2 == -1) {
         Parcelable[] var4 = var3.getParcelableArrayExtra("ru.sviridov.techsupervision.SELECTED_VALUES");
         switch(var1) {
         case 0:
            this.defect.problems = Formats.migrateArray(var4);
            this.defect.reasons = null;
            this.defect.compensations = null;
            this.tvDefect.setText(Formats.formatArray((Object[])var4));
            this.tvReason.setText("");
            this.tvCompensation.setText("");
            break;
         case 1:
            this.defect.reasons = Formats.migrateArray(var4);
            this.defect.compensations = null;
            this.tvReason.setText(Formats.formatArray((Object[])var4));
            this.tvCompensation.setText("");
            break;
         case 2:
            this.defect.compensations = Formats.migrateArray(var4);
            this.tvCompensation.setText(Formats.formatArray((Object[])var4));
         }

         this.saveDefect();
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setHasOptionsMenu(true);
      this.defect = new Defect();
      var1 = this.getArguments();
      if (var1 != null) {
         int var2 = var1.getInt("ru.sviridov.techsupervision.DEFECT_ID", -1);
         if (var2 != -1) {
            Uri var3 = ContentUris.withAppendedId(UserDataProvider.getContentUri("Defect"), (long)var2);
            this.defect = (Defect)CupboardFactory.cupboard().withContext(this.getActivity()).get(var3, Defect.class);
         }
      }

   }

   public View onCreateView(LayoutInflater var1, @NonNull ViewGroup var2, Bundle var3) {
      return var1.inflate(R.layout.fragment_defect_details, var2, false);
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      switch(var1.getItemId()) {
      case R.id.export:
         this.saveDefect();
      default:
         return super.onOptionsItemSelected(var1);
      }
   }

   public void onPlaceSelected(Place var1) {
      this.defect.place = var1;
      this.saveDefect();
      this.tvCoord.setText(var1.toString());
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onCreate(var2);
      this.initViews(var1);
      this.elementController = new ElementController(this.getActivity());
      View var3 = var1.findViewById(R.id.llElemMat);
      var3.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            DefectDetailsFragment.this.elementController.chooseElement();
         }
      });
      this.elementController.addListener(this);
      var1.findViewById(R.id.llCoords).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            DefectDetailsFragment.this.showCoordinatesChooser();
         }
      });
      this.category.setAdapter(this.createAdapter(R.string.danger_category, this.getResources().getStringArray(R.array.danger_categories)));
      this.category.setOnItemSelectedListener(new OnItemSelectedListener() {
         public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
            if (var3 != 0) {
               DefectDetailsFragment.this.defect.setCategory((char)(var3 + 1040 - 1) + "");
               DefectDetailsFragment.this.saveDefect();
            }

         }

         public void onNothingSelected(AdapterView var1) {
            DefectDetailsFragment.this.defect.setCategory("");
         }
      });
      View var4 = var1.findViewById(R.id.llDefect);
      var4.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            if (DefectDetailsFragment.this.defect.getElement() == null) {
               Toast.makeText(GreatApplication.getAppContext(), R.string.choose_element, Toast.LENGTH_SHORT).show();
            } else {
               Intent var2 = new Intent(var1.getContext(), SelectVariantsActivity.class);
               var2.putExtra("ru.sviridov.techsupervision.ROOTS", new int[]{DefectDetailsFragment.this.defect.getMaterial().getMatElemId()});
               var2.putExtra("ru.sviridov.techsupervision.ROOTS_STRING", DefectDetailsFragment.this.defect.getElement() + " " + DefectDetailsFragment.this.defect.getMaterial());
               var2.putExtra("ru.sviridov.techsupervision.SELECTED_VALUES", Formats.extractIds(DefectDetailsFragment.this.defect.problems));
               var2.putExtra("ru.sviridov.techsupervision.URI", "defects");
               DefectDetailsFragment.this.startActivityForResult(var2, 0);
            }

         }
      });
      View var5 = var1.findViewById(R.id.llReason);
      var5.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            if (DefectDetailsFragment.this.defect.problems != null && DefectDetailsFragment.this.defect.problems.length != 0) {
               Intent var2 = new Intent(var1.getContext(), SelectVariantsActivity.class);
               var2.putExtra("ru.sviridov.techsupervision.ROOTS", Formats.extractIds(DefectDetailsFragment.this.defect.problems));
               var2.putExtra("ru.sviridov.techsupervision.ROOTS_STRING", DefectDetailsFragment.this.defect.getNiceProblems());
               var2.putExtra("ru.sviridov.techsupervision.SELECTED_VALUES", Formats.extractIds(DefectDetailsFragment.this.defect.reasons));
               var2.putExtra("ru.sviridov.techsupervision.URI", "reasons");
               DefectDetailsFragment.this.startActivityForResult(var2, 1);
            } else {
               Toast.makeText(GreatApplication.getAppContext(), R.string.choose_problem, Toast.LENGTH_SHORT).show();
            }

         }
      });
      View var6 = var1.findViewById(R.id.llCompensations);
      var6.setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            if (DefectDetailsFragment.this.defect.reasons != null && DefectDetailsFragment.this.defect.reasons.length != 0) {
               Intent var2 = new Intent(var1.getContext(), SelectVariantsActivity.class);
               var2.putExtra("ru.sviridov.techsupervision.ROOTS", Formats.extractIds(DefectDetailsFragment.this.defect.reasons));
               var2.putExtra("ru.sviridov.techsupervision.ROOTS_STRING", DefectDetailsFragment.this.defect.getNiceReasons());
               var2.putExtra("ru.sviridov.techsupervision.SELECTED_VALUES", Formats.extractIds(DefectDetailsFragment.this.defect.compensations));
               var2.putExtra("ru.sviridov.techsupervision.URI", "compensations");
               DefectDetailsFragment.this.startActivityForResult(var2, 2);
            } else {
               Toast.makeText(GreatApplication.getAppContext(), R.string.choose_reasons, Toast.LENGTH_SHORT).show();
            }

         }
      });
      if (!Helper.isFree()) {
         this.initLongClickListeners(var3, var4, var5, var6);
      }

      var1.findViewById(R.id.llVolume).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            SelectDialogs.showMeasumentDialog(DefectDetailsFragment.this.getActivity(), new SelectListener() {
               @Override
               public void onSelected(Object var1) {

               }

               public void onSelected(String var1) {
                  if (var1 != null) {
                     DefectDetailsFragment.this.defect.setVolume(var1);
                     DefectDetailsFragment.this.tvVolume.setText(var1);
                     DefectDetailsFragment.this.saveDefect();
                  }

               }
            }).show();
         }
      });
      this.fillViews(this.defect);
   }

   protected void showCoordinatesChooser() {
      PlaceDialogFragment var1 = PlaceDialogFragment.newInstance(this.defect.place);
      var1.setListener(this);
      var1.show(this.getChildFragmentManager(), "Place");
   }
}
