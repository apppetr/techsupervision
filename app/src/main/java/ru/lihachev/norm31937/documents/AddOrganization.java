package ru.lihachev.norm31937.documents;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.ToolbarActivity;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Appointment;
import ru.lihachev.norm31937.objects.Document;
import ru.lihachev.norm31937.objects.Organization;
import ru.lihachev.norm31937.objects.Responsibility;
import ru.lihachev.norm31937.pictures.RequestPermissionCallback;
import ru.lihachev.norm31937.widgets.ExpandableLayout;

public class AddOrganization extends ToolbarActivity  {
    private long creationTime;
    protected EditText etOrganizationAddress;
    protected EditText etOrganizationYear;
    protected EditText etOrganizationTitle;
    protected EditText etOrganizationCreator;
    protected EditText etOrganizationInspector;
    protected EditText etOrganizationCoordinator;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_organization);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator((int) R.drawable.close);
        }
        this.etOrganizationYear = (EditText) findViewById(R.id.etOrganizationYear);
        this.etOrganizationTitle = (EditText) findViewById(R.id.etOrganizationTitle);
        this.etOrganizationAddress = (EditText) findViewById(R.id.etOrganizationAddress);
        this.etOrganizationCreator = (EditText) findViewById(R.id.etOrganizationCreator);
        this.etOrganizationInspector = (EditText) findViewById(R.id.etOrganizationInspector);
        this.etOrganizationCoordinator = (EditText) findViewById(R.id.etOrganizationCoordinator);
        this.btnContinue = (Button) findViewById((R.id.btnContinue));

        this.btnContinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(AddOrganization.this.etOrganizationTitle.getText())) {
                    Toast.makeText(AddOrganization.this, "Не указано название", Toast.LENGTH_SHORT).show();
                    return;
                }
                createOrganization();
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
            case R.id.save :
                if (TextUtils.isEmpty(this.etOrganizationTitle.getText())) {
                    Toast.makeText(this, "Не указано название", Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                createOrganization();
                //Mint.logEvent(Metrics.CREATED_NEW_DOCUMENT, MintLogLevel.Info, Metrics.toMetrics(document, this.creationTime));
                finish();
                return true;
            default:
                finish();
                return true;
        }
    }

    private void createOrganization() {
        Organization organization = new Organization();
        organization.name = this.etOrganizationTitle.getText().toString();
        if (!TextUtils.isEmpty(this.etOrganizationYear.getText())) {
            organization.year = Integer.valueOf(this.etOrganizationYear.getText().toString()).intValue();
        }
        organization.address = this.etOrganizationAddress.getText().toString();
        organization.creator = AddOrganization.this.etOrganizationCreator.getText().toString();
        organization.inspector = AddOrganization.this.etOrganizationInspector.getText().toString();
        organization.coordinator = AddOrganization.this.etOrganizationCoordinator.getText().toString();
        organization.date = System.currentTimeMillis();

        CupboardFactory.cupboard().withContext(this).put(UserDataProvider.getContentUri(UserDataHelper.ORGANIZATION_URL), organization);
    }

}