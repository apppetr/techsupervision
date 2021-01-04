package ru.sviridov.techsupervision.documents;

import android.app.AlertDialog;
import android.app.LoaderManager;
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
import ru.sviridov.techsupervision.db.UserDataHelper;
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

public class DocumentActivity extends ToolbarActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, RequestPermissionCallback {
   public static final String DOCUMENT_ID = "ru.sviridov.techsupervision.DOCUMENT_ID";
   private static final int DOCUMENT_LOADER = 2;
   public static final int REQUEST_FOR_EXPORT = 23;
   public static final int REQUEST_FOR_SEND = 22;
   private static final String SORT = "ru.sviridov.techsupervision.SORT";
   private RVCursorAdapter adapter;
   private Document document;
   private View emptyView;
   private PermissionController permissionController;
   /* access modifiers changed from: private */
   public int selectedSort = 0;

   /* access modifiers changed from: protected */
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView((int) R.layout.activity_document);
      RecyclerView rv = (RecyclerView) findViewById(R.id.lvData);
      rv.setLayoutManager(createLayoutManager());
      rv.setItemAnimator(new DefaultItemAnimator());
      this.adapter = new DefectsAdapter(this, (Cursor) null, this);
      rv.setAdapter(getAdapter(this.adapter));
      this.document = (Document) CupboardFactory.cupboard().withContext(this).get(ContentUris.withAppendedId(UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL), savedInstanceState == null ? getIntent().getLongExtra("ru.sviridov.techsupervision.DOCUMENT_ID", -1) : savedInstanceState.getLong("ru.sviridov.techsupervision.DOCUMENT_ID", -1)), Document.class);
      setTitle(this.document.title);
      findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            DocumentActivity.this.onAddClick(v);
         }
      });

      this.emptyView = findViewById(android.R.id.empty);
      Bundle bundle = new Bundle();
      bundle.putInt(SORT, this.selectedSort);
      getLoaderManager().initLoader(2, bundle, this);
      //Mint.logEvent(Metrics.OPEN_DEFECTS_LIST, MintLogLevel.Info);
      this.permissionController = new PermissionController(this);
   }

   private RecyclerView.Adapter getAdapter(RVCursorAdapter defectsAdapter) {
      return Helper.isFree() ? new FreeWrapperAdapter(this, defectsAdapter, false, new View.OnClickListener() {
         public void onClick(View v) {
            Helper.openApp(DocumentActivity.this);
         }
      }) : defectsAdapter;
   }

   private RecyclerView.LayoutManager createLayoutManager() {
      return getResources().getBoolean(R.bool.isTablet) ? new GridLayoutManager(this, 3) : new LinearLayoutManager(this);
   }

   public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putLong("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id.longValue());
   }

   public void onAddClick(View view) {
      Intent intent = new Intent(this, AddDefectActivity.class);
      intent.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id.intValue());
      startActivity(intent);
   }

   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      return new CursorLoader(this, UserDataProvider.getContentUriGroupBy(UserDataHelper.DEFECT_WITH_PICTURE.URI, "d._id"), (String[]) null, "d.documentId=" + this.document._id, (String[]) null, args.getInt(SORT) == 0 ? "d.element" : "d._id");
   }

   public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      this.adapter.changeCursor(cursor);
      showEmpty(cursor == null || cursor.getCount() == 0);
   }

   private void showEmpty(boolean isShow) {
      this.emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
   }

   public void onLoaderReset(Loader<Cursor> loader) {
      this.adapter.changeCursor((Cursor) null);
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.sort /*2131558600*/:
            showSortDialog();
            return true;
         case R.id.send /*2131558601*/:
            checkAndRequestPermissions(22);
            return true;
         case R.id.details /*2131558602*/:
            Intent intent = new Intent(this, DocumentInfoActivity.class);
            intent.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id.longValue());
            startActivity(intent);
            break;
         case R.id.export /*2131558603*/:
            checkAndRequestPermissions(23);
            return true;
         case R.id.delete /*2131558604*/:
            Dialogs.show(this, R.string.delete, R.string.want_delete_doc, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                  if (which == -1) {
                     DocumentActivity.this.delete();
                  }
               }
            });
            return true;
      }
      return super.onOptionsItemSelected(item);
   }

   private void checkAndRequestPermissions(int requestCode) {
      if (!this.permissionController.isPermissionGranted(PermissionType.GOOGLE_PHOTOS) || !this.permissionController.isPermissionGranted(PermissionType.WRITE_EXTERNAL_STORAGE)) {
         this.permissionController.requestGroupOfUserGrantPermission(requestCode, PermissionType.GOOGLE_PHOTOS, PermissionType.WRITE_EXTERNAL_STORAGE);
         return;
      }
      onPermissionGranted(requestCode);
   }

   public void onPermissionGranted(int requestCode) {
      if (requestCode == 23) {
         new DocxSaver(this, this.document._id.longValue()).saveToDisk();
      } else if (requestCode == 22) {
         new DocxSaver(this, this.document._id.longValue()).send();
      }
   }

   public void onPermissionDenied(int requestCode) {
      Toast.makeText(this, "Невозможно выполнить действие без разрешений", Toast.LENGTH_LONG).show();
   }

   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      this.permissionController.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
   }

   /* access modifiers changed from: private */
   public void delete() {
      CupboardFactory.cupboard().withContext(this).delete(UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL), this.document);
      finish();
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_document, menu);
      return super.onCreateOptionsMenu(menu);
   }

   public void onClick(@NonNull View view) {
      switch (view.getId()) {
         case R.id.ivImage /*2131558573*/:
         case R.id.tvEdit /*2131558575*/:
            Intent intentEdit = new Intent(this, AddDefectActivity.class);
            intentEdit.putExtra(AddDefectActivity.DEFECT_ID, ((Integer) view.getTag()).intValue());
            intentEdit.putExtra("ru.sviridov.techsupervision.DOCUMENT_ID", this.document._id.intValue());
            intentEdit.putExtra(AddDefectActivity.SELECTED_TAB, view.getId() == R.id.tvEdit ? 0 : 1);
            startActivity(intentEdit);
            return;
         case R.id.tvAddPhoto /*2131558576*/:
            Intent intentAddPhoto = new Intent(this, PictureEditActivity.class);
            intentAddPhoto.putExtra(AddDefectActivity.DEFECT_ID, ((Integer) view.getTag()).intValue());
            startActivity(intentAddPhoto);
            return;
         default:
            return;
      }
   }

   private void showSortDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle((int) R.string.sort);
      builder.setSingleChoiceItems((int) R.array.sorts, this.selectedSort, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            int unused = DocumentActivity.this.selectedSort = which;
            Bundle bundle = new Bundle();
            bundle.putInt(DocumentActivity.SORT, DocumentActivity.this.selectedSort);
            DocumentActivity.this.getLoaderManager().restartLoader(2, bundle, DocumentActivity.this);
         }
      });
      builder.show();
   }
}
