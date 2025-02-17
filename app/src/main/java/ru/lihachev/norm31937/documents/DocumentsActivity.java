package ru.lihachev.norm31937.documents;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
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

import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.ToolbarActivity;

import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.docx.DocxSaver;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.pictures.PermissionController;
import ru.lihachev.norm31937.pictures.PermissionType;
import ru.lihachev.norm31937.pictures.RequestPermissionCallback;
import ru.lihachev.norm31937.utils.alerts.Dialogs;

public class DocumentsActivity extends ToolbarActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, OnItemClicked, RequestPermissionCallback {
    private static final int DOCUMENTS_LOADER = 1;
    public static final int REQUEST_FOR_EXPORT = 23;
    public static final int REQUEST_FOR_SEND = 22;
    private CursorAdapter adapter;
    @Deprecated
    private long docId = -1;
    private PermissionController permissionController;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_documents);

        ActionBar aBar = getSupportActionBar();
        if (aBar != null) {
            aBar.setDisplayHomeAsUpEnabled(false);
            aBar.setTitle(R.string.app_name); // hide the title bar
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));
        list.addHeaderView(inflater.inflate(R.layout.header_documents, list, false), (Object) null, false);
        list.addFooterView(inflater.inflate(R.layout.separator, list, false), (Object) null, false);
        this.adapter = new DocumentsAdapter(this, (Cursor) null, this);
        list.setAdapter(this.adapter);
        list.setOnItemClickListener(this);
        getLoaderManager().initLoader(1, (Bundle) null, this);
        findViewById(R.id.imageAddDefect).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DocumentsActivity.this.startActivity(new Intent(DocumentsActivity.this, AddDocumentActivity.class));
            }
        });

        findViewById(R.id.TextAddOrganization).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DocumentsActivity.this.startActivity(new Intent(DocumentsActivity.this, AddOrganization.class));
            }
        });
        this.permissionController = new PermissionController(this);
    }

    public void onItemClick(@NonNull AdapterView<?> adapterView, @NonNull View view, int position, long id) {
        Intent intent = new Intent(this, DocumentActivity.class);
        intent.putExtra("ru.lihachev.norm31937.DOCUMENT_ID", id);
        startActivity(intent);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_WITH_DEFECTS.URI), (String[]) null, (String) null, (String[]) null, "date DESC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (this.adapter != null) {
            Cursor oldCursor = this.adapter.getCursor();
            if (!(oldCursor == null || oldCursor == cursor || (cursor != null && cursor.getCount() >= oldCursor.getCount()))) {
                Snackbar.make(findViewById(R.id.imageAddDefect), (int) R.string.deleted_document, Snackbar.LENGTH_LONG).show();
            }
            this.adapter.changeCursor(cursor);
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        if (this.adapter != null) {
            this.adapter.changeCursor((Cursor) null);
        }
    }

    public void onClicked(final long docId2, @MenuRes int menuId) {
        this.docId = docId2;
        switch (menuId) {
            case R.id.menu_popup_delete /*2131558607*/:
                Dialogs.show(this, R.string.delete, R.string.want_delete_doc, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == -1) {
                            CupboardFactory.cupboard().withContext(DocumentsActivity.this).delete(UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL), "_id=?", String.valueOf(docId2));
                        }
                    }
                });
                return;
            case R.id.menu_popup_send /*2131558608*/:
                checkAndRequestPermissions(22);
                return;
            case R.id.menu_popup_export /*2131558609*/:
                checkAndRequestPermissions(23);
                return;
            default:
                return;
        }
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
            new DocxSaver(this, this.docId).saveToDisk();
        } else if (requestCode == 22) {
            new DocxSaver(this, this.docId).send();
        }
    }

    public void onPermissionDenied(int requestCode) {
        Toast.makeText(this, "Невозможно выполнить действие без разрешений", Toast.LENGTH_LONG).show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionController.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
