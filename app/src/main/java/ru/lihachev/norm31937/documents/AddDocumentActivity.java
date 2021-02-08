
package ru.lihachev.norm31937.documents;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.ToolbarActivity;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Appointment;
import ru.lihachev.norm31937.objects.Document;
import ru.lihachev.norm31937.objects.Organization;
import ru.lihachev.norm31937.objects.Responsibility;
import ru.lihachev.norm31937.objects.Variant;
import ru.lihachev.norm31937.values.ValuesProvider;
import ru.lihachev.norm31937.widgets.ExpandableLayout;

public class AddDocumentActivity extends ToolbarActivity {
    private long creationTime;
    private int organizationId;
    List<Organization> organizationsList;
    protected EditText etAddress;
    protected EditText etFloors;
    protected EditText etSizes;
    protected EditText etTitle;
    protected EditText etYear;
    protected Spinner responsibility;
    protected Spinner organization;
    private Spinner sAppointment;
    public CheckedTextView tvDetailsSubTitle;
    private ExpandableLayout expandableLayout;
    private Button btnContinue;
    private  Document document;
    private UserDataHelper dbHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator((int) R.drawable.close);
        }
        this.etYear = (EditText) findViewById(R.id.etYear);
        this.etTitle = (EditText) findViewById(R.id.etTitle);
        this.etAddress = (EditText) findViewById(R.id.etAddress);
        this.etSizes = (EditText) findViewById(R.id.etSizes);
        this.etFloors = (EditText) findViewById(R.id.etFloors);
        this.responsibility = (Spinner) findViewById(R.id.etResponsibility);
        this.organization = (Spinner) findViewById(R.id.etOrganization);
        this.sAppointment = (Spinner) findViewById(R.id.sAppointment);
        this.expandableLayout = new ExpandableLayout((LinearLayout) findViewById(R.id.llDocument));
        this.tvDetailsSubTitle = (CheckedTextView) findViewById((R.id.tvDetailsSubTitle));

        this.btnContinue = (Button) findViewById((R.id.btnContinue));

        this.dbHelper = new UserDataHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor organizations = CupboardFactory.cupboard().withDatabase(db).query(Organization.class).getCursor();
        organizationsList = new ArrayList<>();
        ArrayList<String> organizationsNames = new ArrayList<>();
        while (!organizations.isAfterLast()) {
            Organization organization = CupboardFactory.cupboard().withCursor(organizations).get(Organization.class);
            organizationsList.add(organization);
            organizationsNames.add(organization.name);
            organizations.moveToNext();
        }

        final Spinner spinner = (Spinner) findViewById(R.id.etOrganization);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organizationsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        organization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organizationId = (int) (id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                organizationId = 0;
            }
        });

        findViewById(R.id.tvDetailsSubTitle).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (AddDocumentActivity.this.expandableLayout.isVisible()) {
                    AddDocumentActivity.this.expandableLayout.expand();
                } else {
                    AddDocumentActivity.this.expandableLayout.collapse();
                }
            }
        });

        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (TextUtils.isEmpty(AddDocumentActivity.this.etTitle.getText())) {
                    Toast.makeText(AddDocumentActivity.this, "Не указано название", Toast.LENGTH_SHORT).show();
                    return;
                }
                createDocument();
                finish();

            }
        });
        this.creationTime = System.currentTimeMillis();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (TextUtils.isEmpty(this.etTitle.getText())) {
                    Toast.makeText(this, "Не указано название", Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                createDocument();
                finish();
                return true;
            default:
                finish();
                return true;
        }
    }

    private void createDocument() {
        document = new Document();
        document.title = AddDocumentActivity.this.etTitle.getText().toString();
        if (AddDocumentActivity.this.responsibility.getSelectedItemPosition() > 0) {
            document.responsibility = Responsibility.values()[AddDocumentActivity.this.responsibility.getSelectedItemPosition() - 1];
        }
        if (AddDocumentActivity.this.sAppointment.getSelectedItemPosition() > 0) {
            document.appointment = Appointment.values()[AddDocumentActivity.this.sAppointment.getSelectedItemPosition() - 1];
        }
        if (!TextUtils.isEmpty(AddDocumentActivity.this.etYear.getText())) {
            document.year = Integer.valueOf(AddDocumentActivity.this.etYear.getText().toString()).intValue();
        }
        document.address = AddDocumentActivity.this.etAddress.getText().toString();
        document.sizes = AddDocumentActivity.this.etSizes.getText().toString();
        if (AddDocumentActivity.this.etFloors.length() > 0) {
            document.floors = Integer.parseInt(AddDocumentActivity.this.etFloors.getText().toString());
        }
        document.date = System.currentTimeMillis();
        document.photo = "";
        document.organization = organizationsList.get(organizationId).name;
        document.creator = organizationsList.get(organizationId).creator;
        document.inspector = organizationsList.get(organizationId).inspector;
        document.coordinator = organizationsList.get(organizationId).coordinator;
        document.orgAddres = organizationsList.get(organizationId).address;
         Uri uri = UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL);
        CupboardFactory.cupboard().withContext(this).put(uri, document);
    }
}
