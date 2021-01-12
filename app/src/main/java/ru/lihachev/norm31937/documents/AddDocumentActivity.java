
package ru.lihachev.norm31937.documents;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.ToolbarActivity;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Appointment;
import ru.lihachev.norm31937.objects.Document;
import ru.lihachev.norm31937.objects.Responsibility;

public class AddDocumentActivity extends ToolbarActivity {
   private long creationTime;
   protected EditText etAddress;
   protected EditText etFloors;
   protected EditText etSizes;
   protected EditText etTitle;
   protected EditText etYear;
   protected Spinner responsibility;
   private Spinner sAppointment;

   /* access modifiers changed from: protected */
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
      this.sAppointment = (Spinner) findViewById(R.id.sAppointment);
  //    Mint.logEvent(Metrics.START_NEW_DOCUMENT, MintLogLevel.Info);
      this.creationTime = System.currentTimeMillis();
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.document_add, menu);
      return super.onCreateOptionsMenu(menu);
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.save /*2131558598*/:
            if (TextUtils.isEmpty(this.etTitle.getText())) {
               Toast.makeText(this, "Не указано название", Toast.LENGTH_SHORT).show();
               return super.onOptionsItemSelected(item);
            }
            Document document = new Document();
            document.title = this.etTitle.getText().toString();
            if (this.responsibility.getSelectedItemPosition() > 0) {
               document.responsibility = Responsibility.values()[this.responsibility.getSelectedItemPosition() - 1];
            }
            if (this.sAppointment.getSelectedItemPosition() > 0) {
               document.appointment = Appointment.values()[this.sAppointment.getSelectedItemPosition() - 1];
            }
            if (!TextUtils.isEmpty(this.etYear.getText())) {
               document.year = Integer.valueOf(this.etYear.getText().toString()).intValue();
            }
            document.address = this.etAddress.getText().toString();
            document.sizes = this.etSizes.getText().toString();
            if (this.etFloors.length() > 0) {
               document.floors = Integer.parseInt(this.etFloors.getText().toString());
            }
            document.date = System.currentTimeMillis();
            document.photo = "";
            CupboardFactory.cupboard().withContext(this).put(UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL), document);
            //Mint.logEvent(Metrics.CREATED_NEW_DOCUMENT, MintLogLevel.Info, Metrics.toMetrics(document, this.creationTime));
            finish();
            return true;
         default:
            finish();
            return true;
      }
   }
}
