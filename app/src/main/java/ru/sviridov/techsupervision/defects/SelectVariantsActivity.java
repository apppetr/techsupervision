package ru.sviridov.techsupervision.defects;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import ru.sviridov.techsupervision.Helper;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Variant;
import ru.sviridov.techsupervision.utils.Formats;
import ru.sviridov.techsupervision.utils.FreeWrapperAdapter;
import ru.sviridov.techsupervision.values.ValuesProvider;

public class SelectVariantsActivity extends ToolbarActivity implements LoaderCallbacks {
   public static final int MODE_COMPENSATIONS = 2;
   public static final int MODE_DEFECTS = 0;
   public static final int MODE_REASONS = 1;
   public static final String ROOTS = "ru.sviridov.techsupervision.ROOTS";
   public static final String ROOTS_STRING = "ru.sviridov.techsupervision.ROOTS_STRING";
   public static final String SELECTED_VALUES = "ru.sviridov.techsupervision.SELECTED_VALUES";
   private static final String TEXT = "ru.sviridov.techsupervision.TEXT";
   public static final String URI = "ru.sviridov.techsupervision.URI";
   private static final int VARIANT_LOADER = 3;
   private VariantsAdapter adapter;
   private View addVariantLayout;
   private SearchView searchView;

   private void addVariant(String var1) {
      Bundle var2 = this.getIntent().getExtras();
      ContentResolver var3 = this.getContentResolver();
      ContentValues var4 = new ContentValues();
      var4.put("name", var1);
      var4.put("manually_added", true);
      var4.put("uploaded", false);
      String var5 = var2.getString("ru.sviridov.techsupervision.URI");
      long var6 = Long.parseLong(var3.insert(ValuesProvider.uri(var5), var4).getLastPathSegment());
      int[] var8 = var2.getIntArray("ru.sviridov.techsupervision.ROOTS");
      ContentValues[] var9 = new ContentValues[var8.length];
      String[] var12 = getFeildsForInsert(var5);

      for(int var10 = 0; var10 < var8.length; ++var10) {
         var9[var10] = new ContentValues();
         var9[var10].put(var12[1], var8[var10]);
         var9[var10].put(var12[2], var6);
         var9[var10].put("manually_added", true);
         var9[var10].put("uploaded", false);
      }

      var3.bulkInsert(ValuesProvider.uri(var12[0]), var9);
      HashMap var11 = new HashMap();
      var11.put("type", var5);
      var11.put("name", var1);
      var11.put("roots", var2.getString("ru.sviridov.techsupervision.ROOTS_STRING"));
    //  Mint.logEvent("added variant", MintLogLevel.Info, var11);
      this.getLoaderManager().restartLoader(3, var2, this);
   }

   private boolean canAddValues(int var1) {
      boolean var2;
      if (var1 == 0 && this.searchView.getQuery().length() != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private RecyclerView.Adapter getAdapter(VariantsAdapter var1) {
      Object var2 = var1;
      if (Helper.isFree()) {
         var2 = new FreeWrapperAdapter(this, var1, true, new OnClickListener() {
            public void onClick(View var1) {
               Helper.openApp(SelectVariantsActivity.this);
            }
         });
      }

      return (RecyclerView.Adapter)var2;
   }

   @StringRes
   private int getAddVariantText(String var1) {
      byte var2 = -1;
      switch(var1.hashCode()) {
      case R.id.ivPicture:
         if (var1.equals("reasons")) {
            var2 = 1;
         }
         break;

      case 1544906018:
         if (var1.equals("defects")) {
            var2 = 0;
         }
         break;
      case 1769711449:
         if (var1.equals("compensations")) {
            var2 = 2;
         }
      }

      int var3;
      switch(var2) {
      case 0:
         var3 = R.string.ask_add_defect;
         break;
      case 1:
         var3 = R.string.ask_add_reason;
         break;
      case 2:
         var3 = R.string.ask_add_compensation;
         break;
      default:
         var3 = R.string.ask_add_variant;
      }

      return var3;
   }

   private static String[] getFeildsForInsert(String var0) {
      byte var1 = -1;
      switch(var0.hashCode()) {
      case 1080866479:
         if (var0.equals("reasons")) {
            var1 = 1;
         }
         break;
      case 1544906018:
         if (var0.equals("defects")) {
            var1 = 0;
         }
         break;
      case 1769711449:
         if (var0.equals("compensations")) {
            var1 = 2;
         }
      }

      String[] var2;
      switch(var1) {
      case 0:
         var2 = new String[]{"defects2elements", "mat_elem_id", "defect_id"};
         break;
      case 1:
         var2 = new String[]{"defects2reasons", "defect_id", "reason_id"};
         break;
      case 2:
         var2 = new String[]{"reasons2compensations", "reason_id", "compensation_id"};
         break;
      default:
         var2 = null;
      }

      return var2;
   }

   private static String getLinker(String var0) {
      byte var1 = -1;
      switch(var0.hashCode()) {
      case 1080866479:
         if (var0.equals("reasons")) {
            var1 = 1;
         }
         break;
      case 1544906018:
         if (var0.equals("defects")) {
            var1 = 0;
         }
         break;
      case 1769711449:
         if (var0.equals("compensations")) {
            var1 = 2;
         }
      }

      switch(var1) {
      case 0:
         var0 = "mat_elem_id";
         break;
      case 1:
         var0 = "defect_id";
         break;
      case 2:
         var0 = "reason_id";
         break;
      default:
         var0 = "mat_elem_id";
      }

      return var0;
   }

   private static String getUriForSelection(String var0) {
      byte var1 = -1;
      switch(var0.hashCode()) {
      case 1080866479:
         if (var0.equals("reasons")) {
            var1 = 1;
         }
         break;
      case 1544906018:
         if (var0.equals("defects")) {
            var1 = 0;
         }
         break;
      case 1769711449:
         if (var0.equals("compensations")) {
            var1 = 2;
         }
      }

      switch(var1) {
      case 0:
         var0 = "select_defects";
         break;
      case 1:
         var0 = "select_reasons";
         break;
      case 2:
         var0 = "select_compensations";
         break;
      default:
         var0 = "select_defects";
      }

      return var0;
   }

   private int selectTitle(String var1) {
      int var2 = R.string.defect_desc;
      byte var3 = -1;
      switch(var1.hashCode()) {
      case 1080866479:
         if (var1.equals("reasons")) {
            var3 = 1;
         }
         break;
      case 1544906018:
         if (var1.equals("defects")) {
            var3 = 0;
         }
         break;
      case 1769711449:
         if (var1.equals("compensations")) {
            var3 = 2;
         }
      }

      int var4 = var2;
      switch(var3) {
      case 0:
         break;
      case 1:
         var4 = R.string.defect_reasons;
         break;
      case 2:
         var4 = R.string.defect_compentations;
         break;
      default:
         var4 = var2;
      }

      return var4;
   }

   private void selectVariants() {
      List var1 = this.adapter.getSelected();
      this.getIntent().putExtra("ru.sviridov.techsupervision.SELECTED_VALUES", (Parcelable[])this.adapter.getSelected().toArray(new Variant[var1.size()]));
      this.setResult(-1, this.getIntent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_select_variants);
      ActionBar var4 = this.getSupportActionBar();
      if (var4 != null) {
         var4.setHomeAsUpIndicator(R.drawable.close);
      }

      var1 = this.getIntent().getExtras();
      String var2 = var1.getString("ru.sviridov.techsupervision.URI", "defects");
      if (this.getSupportActionBar() != null) {
         this.getSupportActionBar().setTitle(this.selectTitle(var2));
      }

      int[] var3 = var1.getIntArray("ru.sviridov.techsupervision.SELECTED_VALUES");
      this.adapter = new VariantsAdapter(this, (Cursor)null);
      this.adapter.setSelectedIds(var3);
      RecyclerView var5 = (RecyclerView)this.findViewById(android.R.id.list);
      var5.setLayoutManager(new LinearLayoutManager(this));
      var5.setAdapter(this.getAdapter(this.adapter));
      this.searchView = (SearchView)this.findViewById(R.id.svQuery);
      this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         public boolean onQueryTextChange(String var1) {
            Bundle var2 = SelectVariantsActivity.this.getIntent().getExtras();
            if (var1.length() > 2) {
               var2.putString("ru.sviridov.techsupervision.TEXT", var1);
            }

            SelectVariantsActivity.this.getLoaderManager().restartLoader(3, var2, SelectVariantsActivity.this);
            return true;
         }

         public boolean onQueryTextSubmit(String var1) {
            return false;
         }
      });
      this.addVariantLayout = this.findViewById(R.id.llAddVariant);
      this.findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (Helper.isFree()) {
               Helper.openApp(SelectVariantsActivity.this);
            } else {
               SelectVariantsActivity.this.addVariant(SelectVariantsActivity.this.searchView.getQuery().toString());
            }

         }
      });
      ((TextView)this.findViewById(R.id.tvAddVariant)).setText(this.getAddVariantText(var2));
      this.searchView.post(new Runnable() {
         public void run() {
            SelectVariantsActivity.this.searchView.clearFocus();
         }
      });
      this.getLoaderManager().initLoader(3, var1, this);
     // Mint.logEvent("open select variants", MintLogLevel.Info, "type", String.valueOf(var2));
   }

   public Loader onCreateLoader(int var1, Bundle var2) {
      String var3 = var2.getString("ru.sviridov.techsupervision.URI");
      String var4 = null;
      if (var2 != null) {
         int[] var5 = var2.getIntArray("ru.sviridov.techsupervision.ROOTS");
         StringBuilder var6 = new StringBuilder();
         var6.append(String.format("%1$s in (%2$s)", getLinker(var3), Formats.formatArray(var5)));
         if (var2.containsKey("ru.sviridov.techsupervision.TEXT")) {
            var6.append("AND name LIKE '%");
            var6.append(var2.getString("ru.sviridov.techsupervision.TEXT"));
            var6.append("%' ");
         }

         var4 = var6.toString();
      }

      return new CursorLoader(this, ValuesProvider.uri(getUriForSelection(var3)), (String[])null, var4, (String[])null, (String)null);
   }

   @Override
   public void onLoadFinished(Loader loader, Object data) {

   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(R.menu.marks_management, var1);
      return super.onCreateOptionsMenu(var1);
   }

   public void onLoadFinished(Loader var1, Cursor var2) {
      this.adapter.swapCursor(var2);
      View var4 = this.addVariantLayout;
      byte var3;
      if (this.canAddValues(var2.getCount())) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      var4.setVisibility(var3);
   }

   public void onLoaderReset(Loader var1) {
      this.adapter.swapCursor((Cursor)null);
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      int var2 = var1.getItemId();
      if (var2 == R.id.ready) {
         this.selectVariants();
      } else if (var2 == android.R.id.home) {
         this.finish();
      }

      return super.onOptionsItemSelected(var1);
   }
}
