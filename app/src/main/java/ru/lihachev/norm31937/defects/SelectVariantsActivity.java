package ru.lihachev.norm31937.defects;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.opensagres.xdocreport.document.docx.DocxConstants;
import ru.lihachev.norm31937.Helper;
import ru.lihachev.norm31937.ToolbarActivity;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.utils.Formats;
import ru.lihachev.norm31937.utils.FreeWrapperAdapter;
import ru.lihachev.norm31937.values.Values;
import ru.lihachev.norm31937.values.ValuesProvider;

public class SelectVariantsActivity extends ToolbarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int MODE_COMPENSATIONS = 2;
    public static final int MODE_DEFECTS = 0;
    public static final int MODE_REASONS = 1;
    public static final String ROOTS = "ru.lihachev.norm31937.ROOTS";
    public static final String ROOTS_STRING = "ru.lihachev.norm31937.ROOTS_STRING";
    public static final String SELECTED_VALUES = "ru.lihachev.norm31937.SELECTED_VALUES";
    public static final String VALUES_INFO = "ru.lihachev.norm31937.VALUES_INFO";
    private static final String TEXT = "ru.lihachev.norm31937.TEXT";
    public static final String URI = "ru.lihachev.norm31937.URI";
    public static String selectedUrl;
    private static final int VARIANT_LOADER = 3;
    private VariantsAdapter adapter;
    private View addVariantLayout;
    public SearchView searchView;

    private static String[] getFeildsForInsert(String uri) {
        char c = 65535;
        switch (uri.hashCode()) {
            case 1080866479:
                if (uri.equals("reasons")) {
                    c = 1;
                    break;
                }
                break;
            case 1544906018:
                if (uri.equals("defects")) {
                    c = 0;
                    break;
                }
                break;
            case 1769711449:
                if (uri.equals("compensations")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new String[]{"defects2elements", "mat_elem_id", "defect_id"};
            case 1:
                return new String[]{"defects2reasons", "defect_id", "reason_id"};
            case 2:
                return new String[]{"reasons2compensations", "reason_id", Values.R2C.COMPENSATION_ID};
            case 3:
                return new String[]{"defects2compensations", "defect_id", Values.D2C.COMPENSATION_ID};
            default:
                return null;
        }
    }

    private static String getLinker(String uriPath) {
        char c = 65535;
        switch (uriPath.hashCode()) {
            case 1080866479:
                if (uriPath.equals("reasons")) {
                    c = 1;
                    break;
                }
                break;
            case 1544906018:
                if (uriPath.equals("defects")) {
                    c = 0;
                    break;
                }
                break;
            case 1769711449:
                if (uriPath.equals("compensations")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "mat_elem_id";   //передача параметра для запроса к базе в переменную select
            case 1:
                return "defect_id";  //передача параметра для запроса к базе в переменную select
            case 2:
                return "reason_id";  //передача параметра для запроса к базе в переменную select
            case 3:
                return "defect_id";  //передача параметра для запроса к базе в переменную select
            default:
                return "mat_elem_id";
        }
    }

    private static String getUriForSelection(String uriPath) {
        char c = 65535;
        selectedUrl = uriPath;
        switch (uriPath.hashCode()) {
            case 1080866479:
                if (uriPath.equals("reasons")) {

                    c = 1;
                    break;
                }
                break;
            case 1544906018:
                if (uriPath.equals("defects")) {
                    c = 0;
                    break;
                }
                break;
            case 1769711449:
                if (uriPath.equals("compensations")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return Values.Defects.URI_FOR_SELECTION;  //url для ValuesProvider строка 105
            case 1:
                return Values.Reasons.URI_FOR_SELECTION;  //url для ValuesProvider строка 108
            case 2:
                return Values.Compensations.URI_FOR_SELECTION;  //url для ValuesProvider строка 113
            case 3:
                return Values.Compensations.URI_FOR_SELECTION;  //url для ValuesProvider строка 113
            default:
                return Values.Defects.URI_FOR_SELECTION;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView((int) R.layout.activity_select_variants);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator((int) R.drawable.close);
        }
        Bundle args = getIntent().getExtras();
        String uriPath = args.getString(URI, "defects");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(selectTitle(uriPath));
        }
        int[] selectedValues = args.getIntArray(SELECTED_VALUES);
     //   Object[] selectedValuesr = args.getStringArray(SELECTED_VALUES);
        Parcelable[] values = args.getParcelableArray(VALUES_INFO);
    //   Parcelable[] values = savedInstanceState.getParcelableArray(VALUES_INFO);

        this.adapter = new VariantsAdapter(this, (Cursor) null);
        this.adapter.setSelectedIds(selectedValues);
        this.adapter.setUserData(values);
        RecyclerView recyclerView = (RecyclerView) findViewById(android.R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter(this.adapter));
        this.searchView = (SearchView) findViewById(R.id.svQuery);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                Bundle args = SelectVariantsActivity.this.getIntent().getExtras();
                if (newText.length() > 2) {
                    args.putString(SelectVariantsActivity.TEXT, newText);
                }
                SelectVariantsActivity.this.getLoaderManager().restartLoader(3, args, SelectVariantsActivity.this);
                return true;
            }
        });
        this.addVariantLayout = findViewById(R.id.llAddVariant);
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Helper.isFree()) {
                    Helper.openApp(SelectVariantsActivity.this);
                } else {
                    SelectVariantsActivity.this.addVariant(SelectVariantsActivity.this.searchView.getQuery().toString());
                }
            }
        });
        ((TextView) findViewById(R.id.tvAddVariant)).setText(getAddVariantText(uriPath));
        this.searchView.post(new Runnable() {
            public void run() {
                SelectVariantsActivity.this.searchView.clearFocus();
            }
        });
        getLoaderManager().initLoader(3, args, this);
        //Mint.logEvent(Metrics.OPEN_SELECT_VARIANTS, MintLogLevel.Info, DocxConstants.TYPE_ATTR, String.valueOf(uriPath));
    }

    private RecyclerView.Adapter<VariantsAdapter.VariantViewHolder> getAdapter(VariantsAdapter variantsAdapter) {
        if (Helper.isFree()) {
            return new FreeWrapperAdapter(this, variantsAdapter, true, new View.OnClickListener() {
                public void onClick(View v) {
                    Helper.openApp(SelectVariantsActivity.this);
                }
            });
        }
        return variantsAdapter;
    }

    @StringRes
    private int getAddVariantText(String uriPath) {
        char c = 65535;
        switch (uriPath.hashCode()) {
            case 1080866479:
                if (uriPath.equals("reasons")) {
                    c = 1;
                    break;
                }
                break;
            case 1544906018:
                if (uriPath.equals("defects")) {
                    c = 0;
                    break;
                }
                break;
            case 1769711449:
                if (uriPath.equals("compensations")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return R.string.ask_add_defect;
            case 1:
                return R.string.ask_add_reason;
            case 2:
                return R.string.ask_add_compensation;
            default:
                return R.string.ask_add_variant;
        }
    }

    public void addVariant(String name) {
        Bundle args = getIntent().getExtras();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("manually_added", true);
        values.put("uploaded", false);
        String uri = args.getString(URI);
        long newId = Long.parseLong(cr.insert(ValuesProvider.uri(uri), values).getLastPathSegment());
        int[] roots = args.getIntArray(ROOTS);
        ContentValues[] cvs = new ContentValues[roots.length];
        String[] fields = getFeildsForInsert(uri);
        for (int i = 0; i < roots.length; i++) {
            cvs[i] = new ContentValues();
            cvs[i].put(fields[1], Integer.valueOf(roots[i]));
            cvs[i].put(fields[2], Long.valueOf(newId));
            cvs[i].put("manually_added", true);
            cvs[i].put("uploaded", false);
        }
        cr.bulkInsert(ValuesProvider.uri(fields[0]), cvs);
        HashMap<String, Object> attrs = new HashMap<>();
        attrs.put(DocxConstants.TYPE_ATTR, uri);
        attrs.put("name", name);
        attrs.put("roots", args.getString(ROOTS_STRING));
        //Mint.logEvent(Metrics.ADDED_VARIANT, MintLogLevel.Info, attrs);
        getLoaderManager().restartLoader(3, args, this);
    }

    private int selectTitle(String uriPath) {
        char c = 65535;
        switch (uriPath.hashCode()) {
            case 1080866479:
                if (uriPath.equals("reasons")) {
                    c = 1;
                    break;
                }
                break;
            case 1544906018:
                if (uriPath.equals("defects")) {
                    c = 0;
                    break;
                }
                break;
            case 1769711449:
                if (uriPath.equals("compensations")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 1:
                return R.string.defect_reasons;
            case 2:
                return R.string.defect_compentations;
            default:
                return R.string.defect_desc;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marks_management, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ready) {
            selectVariants();
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectVariants() {
        List<Variant> selectedVariants = this.adapter.getSelected();
        Parcelable[] values = this.adapter.getSelected().toArray(new Parcelable[selectedVariants.size()]);
        getIntent().putExtra(SELECTED_VALUES, values);

     setResult(-1, getIntent());
        finish();
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String uriPath = args.getString(URI);
        String selection = null;
        if (args != null) {
            int[] roots = args.getIntArray(ROOTS);
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(String.format("%1$s in (%2$s)", getLinker(uriPath), Formats.formatArray(roots)));
            if (args.containsKey(TEXT)) {
                sBuilder.append("AND name LIKE '%");
                sBuilder.append(args.getString(TEXT));
                sBuilder.append("%' ");
            }
            selection = sBuilder.toString();
        }
        return new CursorLoader(this, ValuesProvider.uri(getUriForSelection(uriPath)), (String[]) null, selection, (String[]) null, (String) null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.adapter.swapCursor(cursor);
        this.addVariantLayout.setVisibility(canAddValues(cursor.getCount()) ? View.VISIBLE : View.GONE);
    }

    private boolean canAddValues(int itemsCount) {
        return itemsCount == 0 && this.searchView.getQuery().length() != 0;
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        this.adapter.swapCursor((Cursor) null);
    }
}
