package ru.sviridov.techsupervision.documents;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.docx.DocxSaver;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.pictures.PermissionController;
import ru.sviridov.techsupervision.pictures.PermissionType;
import ru.sviridov.techsupervision.pictures.RequestPermissionCallback;
import ru.sviridov.techsupervision.utils.alerts.Dialogs;

public class DocumentsActivity extends ToolbarActivity implements LoaderCallbacks, OnItemClickListener, OnItemClicked, RequestPermissionCallback {
   private static final int DOCUMENTS_LOADER = 1;
   public static final int REQUEST_FOR_EXPORT = 23;
   public static final int REQUEST_FOR_SEND = 22;
   private CursorAdapter adapter;
   @Deprecated
   private long docId = -1L;
   private PermissionController permissionController;

   private void checkAndRequestPermissions(int var1) {
      if (this.permissionController.isPermissionGranted(PermissionType.GOOGLE_PHOTOS) && this.permissionController.isPermissionGranted(PermissionType.WRITE_EXTERNAL_STORAGE)) {
         this.onPermissionGranted(var1);
      } else {
         this.permissionController.requestGroupOfUserGrantPermission(var1, PermissionType.GOOGLE_PHOTOS, PermissionType.WRITE_EXTERNAL_STORAGE);
      }

   }

   public void onClicked(final long var1, @MenuRes int var3) {
      this.docId = var1;
      switch(var3) {
      case R.id.menu_popup_delete:
         Dialogs.show(this, R.string.delete, R.string.want_delete_doc, R.string.yes, R.string.cancel, new OnClickListener() {
            public void onClick(DialogInterface var1x, int var2) {
               if (var2 == -1) {
                  CupboardFactory.cupboard().withContext(DocumentsActivity.this).delete(UserDataProvider.getContentUri("Document"), "_id=?", String.valueOf(var1));
               }

            }
         });
         break;
      case R.id.menu_popup_send:
         this.checkAndRequestPermissions(22);
         break;
      case R.id.menu_popup_export:
         this.checkAndRequestPermissions(23);
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_documents);
      ActionBar var3 = this.getSupportActionBar();
      if (var3 != null) {
         var3.setDisplayHomeAsUpEnabled(false);
      }

      LayoutInflater var2 = LayoutInflater.from(this);
      ListView var4 = (ListView)this.findViewById(android.R.id.list);
      var4.setEmptyView(this.findViewById(android.R.id.empty));
      var4.addHeaderView(var2.inflate(R.layout.header_documents, var4, false), (Object)null, false);
      var4.addFooterView(var2.inflate(R.layout.separator, var4, false), (Object)null, false);
      this.adapter = new DocumentsAdapter(this, (Cursor)null, this);
      var4.setAdapter(this.adapter);
      var4.setOnItemClickListener(this);
      this.getLoaderManager().initLoader(1, (Bundle)null, this);
      this.findViewById(R.id.fabAdd).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            DocumentsActivity.this.startActivity(new Intent(DocumentsActivity.this, AddDocumentActivity.class));
         }
      });
      this.permissionController = new PermissionController(this);
   }

   public Loader onCreateLoader(int var1, Bundle var2) {
      return new CursorLoader(this, UserDataProvider.getContentUri("document_with_defects"), (String[])null, (String)null, (String[])null, "date DESC");
   }

   @Override
   public void onLoadFinished(Loader loader, Object data) {

   }

   public void onItemClick(@NonNull AdapterView var1, @NonNull View var2, int var3, long var4) {
      Intent var6 = new Intent(this, DocumentActivity.class);
      var6.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", var4);
      this.startActivity(var6);
   }

   public void onLoadFinished(Loader var1, Cursor var2) {
      if (this.adapter != null) {
         Cursor var3 = this.adapter.getCursor();
         if (var3 != null && var3 != var2 && (var2 == null || var2.getCount() < var3.getCount())) {
            Snackbar.make(this.findViewById(R.id.fabAdd), R.string.deleted_document, 0).show();
         }

         this.adapter.changeCursor(var2);
      }

   }

   public void onLoaderReset(Loader var1) {
      if (this.adapter != null) {
         this.adapter.changeCursor((Cursor)null);
      }

   }

   public void onPermissionDenied(int var1) {
      Toast.makeText(this, "Невозможно выполнить действие без разрешений", 1).show();
   }

   public void onPermissionGranted(int var1) {
      if (var1 == 23) {
         (new DocxSaver(this, this.docId)).saveToDisk();
      } else if (var1 == 22) {
         (new DocxSaver(this, this.docId)).send();
      }

   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);
      this.permissionController.onRequestPermissionsResult(var1, var2, var3, this);
   }
}
