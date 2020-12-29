
package ru.sviridov.techsupervision.documents;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.ProviderCompartment;
import ru.sviridov.techsupervision.Metrics;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Appointment;
import ru.sviridov.techsupervision.objects.Document;
import ru.sviridov.techsupervision.objects.Responsibility;

public class AddDocumentActivity
        extends ToolbarActivity {
   private long creationTime;
   protected EditText etAddress;
   protected EditText etFloors;
   protected EditText etSizes;
   protected EditText etTitle;
   protected EditText etYear;
   protected Spinner responsibility;
   private Spinner sAppointment;

   @Override
   protected void onCreate(Bundle object) {
      super.onCreate((Bundle)object);
      this.setContentView(R.layout.activity_add_document);
      ActionBar obj = this.getSupportActionBar();
      if (obj != null) {
         (obj).setHomeAsUpIndicator(R.drawable.close);
      }
      this.etYear = (EditText)this.findViewById(R.id.etYear);
      this.etTitle = (EditText)this.findViewById(R.id.etTitle);
      this.etAddress = (EditText)this.findViewById(R.id.etAddress);
      this.etSizes = (EditText)this.findViewById(R.id.etSizes);
      this.etFloors = (EditText)this.findViewById(R.id.etFloors);
      this.responsibility = (Spinner)this.findViewById(R.id.etResponsibility);
      this.sAppointment = (Spinner)this.findViewById(R.id.sAppointment);
      //Mint.logEvent("start new document", MintLogLevel.Info);
      this.creationTime = System.currentTimeMillis();
   }

   public boolean onCreateOptionsMenu(Menu menu2) {
      getMenuInflater().inflate(R.menu.document_add, menu2);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem object) {
      boolean bl = true;
      switch (object.getItemId()) {
         default: {
            this.finish();
            return bl;
         }
         case R.id.save:
      }
      if (TextUtils.isEmpty((CharSequence)this.etTitle.getText())) {
         Toast.makeText(this, "Не указано название", Toast.LENGTH_SHORT).show();
         return super.onOptionsItemSelected((MenuItem)object);
      }
      Document objectDocument = new Document();
      objectDocument.title = this.etTitle.getText().toString();
      if (this.responsibility.getSelectedItemPosition() > 0) {
         objectDocument.responsibility = Responsibility.values()[this.responsibility.getSelectedItemPosition() - 1];
      }
      if (this.sAppointment.getSelectedItemPosition() > 0) {
         objectDocument.appointment = Appointment.values()[this.sAppointment.getSelectedItemPosition() - 1];
      }
      if (!TextUtils.isEmpty((CharSequence)this.etYear.getText())) {
         objectDocument.year = Integer.valueOf(this.etYear.getText().toString());
      }
      objectDocument.address = this.etAddress.getText().toString();
      objectDocument.sizes = this.etSizes.getText().toString();
      if (this.etFloors.length() > 0) {
         objectDocument.floors = Integer.parseInt(this.etFloors.getText().toString());
      }
      objectDocument.date = System.currentTimeMillis();
      objectDocument.photo = "";
      CupboardFactory.cupboard().withContext((Context)this).put(UserDataProvider.getContentUri("Document"), object);
      // Mint.logEvent("created new document", MintLogLevel.Info, Metrics.toMetrics((Document)object, this.creationTime));
      this.finish();
      return bl;
   }
}

