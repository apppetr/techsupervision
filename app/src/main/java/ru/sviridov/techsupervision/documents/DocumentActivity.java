package ru.sviridov.techsupervision.documents;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.Helper;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.defects.AddDefectActivity;
import ru.sviridov.techsupervision.docx.DocxSaver;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.pictures.PermissionController;
import ru.sviridov.techsupervision.pictures.PermissionType;
import ru.sviridov.techsupervision.pictures.PictureEditActivity;
import ru.sviridov.techsupervision.pictures.RequestPermissionCallback;
import ru.sviridov.techsupervision.utils.FreeWrapperAdapter;
import ru.sviridov.techsupervision.utils.alerts.Dialogs;
import ru.sviridov.techsupervision.widgets.RVCursorAdapter;

public class DocumentActivity extends ToolbarActivity implements LoaderCallbacks, OnClickListener, RequestPermissionCallback {
   public static final String DOCUMENT_ID = "ru.sviridov.techsupervision.DOCUMENT_ID";
   private static final int DOCUMENT_LOADER = 2;
   public static final int REQUEST_FOR_EXPORT = 23;
   public static final int REQUEST_FOR_SEND = 22;
   private static final String SORT = "ru.sviridov.techsupervision.SORT";
   private RVCursorAdapter adapter;
   private Document document;
   private View emptyView;
   private PermissionController permissionController;
   private int selectedSort = 0;

   private void checkAndRequestPermissions(int var1) {
      if (this.permissionController.isPermissionGranted(PermissionType.GOOGLE_PHOTOS) && this.permissionController.isPermissionGranted(PermissionType.WRITE_EXTERNAL_STORAGE)) {
         this.onPermissionGranted(var1);
      } else {
         this.permissionController.requestGroupOfUserGrantPermission(var1, PermissionType.GOOGLE_PHOTOS, PermissionType.WRITE_EXTERNAL_STORAGE);
      }

   }

   private RecyclerView.LayoutManager createLayoutManager() {
      Object var1;
      if (this.getResources().getBoolean(R.bool.isTablet)) {
         var1 = new GridLayoutManager(this, 3);
      } else {
         var1 = new LinearLayoutManager(this);
      }

      return (RecyclerView.LayoutManager)var1;
   }

   private void delete() {
      Uri var1 = UserDataProvider.getContentUri("Document");
      CupboardFactory.cupboard().withContext(this).delete(var1, this.document);
      this.finish();
   }

   private RecyclerView.Adapter getAdapter(RVCursorAdapter var1) {
      OnClickListener var2 = new OnClickListener() {
         public void onClick(View var1) {
            Helper.openApp(DocumentActivity.this);
         }
      };
      Object var3 = var1;
      if (Helper.isFree()) {
         var3 = new FreeWrapperAdapter(this, var1, false, var2);
      }

      return (RecyclerView.Adapter)var3;
   }

   private void showEmpty(boolean var1) {
      View var2 = this.emptyView;
      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      var2.setVisibility(var3);
   }

   private void showSortDialog() {
      AlertDialog.Builder var1 = new AlertDialog.Builder(this);
      var1.setTitle(R.string.sort);
      var1.setSingleChoiceItems(R.array.sorts, this.selectedSort, new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            var1.dismiss();
            DocumentActivity.this.selectedSort = var2;
            Bundle var3 = new Bundle();
            var3.putInt("ru.sviridov.techsupervision.SORT", DocumentActivity.this.selectedSort);
            DocumentActivity.this.getLoaderManager().restartLoader(2, var3, DocumentActivity.this);
         }
      });
      var1.show();
   }

   public void onAddClick(View var1) {
      Intent var2 = new Intent(this, AddDefectActivity.class);
      var2.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id.intValue());
      this.startActivity(var2);
   }

   public void onClick(@NonNull View var1) {
      Intent var2;
      switch(var1.getId()) {
      case R.id.ivImage:
      case R.id.tvEdit:
         var2 = new Intent(this, AddDefectActivity.class);
         var2.putExtra("ru.sviridov.techsupervision.DEFECT_ID", (Integer)var1.getTag());
         var2.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id.intValue());
         byte var3;
         if (var1.getId() == R.id.tvEdit) {
            var3 = 0;
         } else {
            var3 = 1;
         }

         var2.putExtra("ru.sviridov.techsupervision.SELECTED_TAB", var3);
         this.startActivity(var2);
      case R.id.tvCategory:
      default:
         break;
      case R.id.tvAddPhoto:
         var2 = new Intent(this, PictureEditActivity.class);
         var2.putExtra("ru.sviridov.techsupervision.DEFECT_ID", (Integer)var1.getTag());
         this.startActivity(var2);
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_document);
      RecyclerView var2 = (RecyclerView)this.findViewById(R.id.lvData);
      var2.setLayoutManager(this.createLayoutManager());
      var2.setItemAnimator(new DefaultItemAnimator());
      this.adapter = new DefectsAdapter(this, (Cursor)null, this);
      var2.setAdapter(this.getAdapter(this.adapter));
      long var3;
      if (var1 == null) {
         var3 = this.getIntent().getLongExtra("ru.sviridov.techsupervision.DOCUMENT_ID", -1L);
      } else {
         var3 = var1.getLong("ru.sviridov.techsupervision.DOCUMENT_ID", -1L);
      }

      Uri var5 = ContentUris.withAppendedId(UserDataProvider.getContentUri("Document"), var3);
      this.document = (Document)CupboardFactory.cupboard().withContext(this).get(var5, Document.class);
      this.setTitle(this.document.title);
      this.findViewById(R.id.ivAdd).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            DocumentActivity.this.onAddClick(var1);
         }
      });

      this.emptyView = this.findViewById(android.R.id.empty);
      var1 = new Bundle();
      var1.putInt("ru.sviridov.techsupervision.SORT", this.selectedSort);
      this.getLoaderManager().initLoader(2, var1, this);
     // Mint.logEvent("open defects list", MintLogLevel.Info);
      this.permissionController = new PermissionController(this);
   }

   public Loader onCreateLoader(int var1, Bundle var2) {
      var1 = var2.getInt("ru.sviridov.techsupervision.SORT");
      Uri var3 = UserDataProvider.getContentUriGroupBy("defect_with_picture", "d._id");
      String var4 = "d.documentId=" + this.document._id;
      String var5;
      if (var1 == 0) {
         var5 = "d.element";
      } else {
         var5 = "d._id";
      }

      return new CursorLoader(this, var3, (String[])null, var4, (String[])null, var5);
   }

   @Override
   public void onLoadFinished(Loader loader, Object data) {

   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(R.menu.menu_document, var1);
      return super.onCreateOptionsMenu(var1);
   }

   public void onLoadFinished(Loader var1, Cursor var2) {
      this.adapter.changeCursor(var2);
      boolean var3;
      if (var2 != null && var2.getCount() != 0) {
         var3 = false;
      } else {
         var3 = true;
      }

      this.showEmpty(var3);
   }

   public void onLoaderReset(Loader var1) {
      this.adapter.changeCursor((Cursor)null);
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2;
      switch(var1.getItemId()) {
      case R.id.sort:
         this.showSortDialog();
         var2 = true;
         break;
      case R.id.send:
         this.checkAndRequestPermissions(22);
         var2 = true;
         break;
      case R.id.details:
         Intent var3 = new Intent(this, DocumentInfoActivity.class);
         var3.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id);
         this.startActivity(var3);
      default:
         var2 = super.onOptionsItemSelected(var1);
         break;
      case R.id.export:
         this.checkAndRequestPermissions(23);
         var2 = true;
         break;
      case R.id.delete:
         Dialogs.show(this, R.string.delete, R.string.want_delete_doc, R.string.yes, R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               if (var2 == -1) {
                  DocumentActivity.this.delete();
               }

            }
         });
         var2 = true;
      }

      return var2;
   }

   public void onPermissionDenied(int var1) {
      Toast.makeText(this, "Невозможно выполнить действие без разрешений", 1).show();
   }

   public void onPermissionGranted(int var1) {
      if (var1 == 23) {
         (new DocxSaver(this, this.document._id)).saveToDisk();
      } else if (var1 == 22) {
         (new DocxSaver(this, this.document._id)).send();
      }

   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);
      this.permissionController.onRequestPermissionsResult(var1, var2, var3, this);
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      var1.putLong("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id);
   }
}
