package ru.sviridov.techsupervision.documents;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Document;

public class DocumentInfoActivity extends ToolbarActivity {
   private void fillViews(@Nullable Document var1) {
      if (var1 != null) {
         ((TextView)this.findViewById(R.id.tvAddress)).setText(var1.address);
         ((TextView)this.findViewById(R.id.tvYear)).setText(String.valueOf(var1.year));
         ((TextView)this.findViewById(R.id.tvResponsibility)).setText(var1.responsibility.getName());
         ((TextView)this.findViewById(R.id.tvSizes)).setText(var1.sizes);
         ((TextView)this.findViewById(R.id.tvFloors)).setText(String.valueOf(var1.floors));
         ((TextView)this.findViewById(R.id.tvAppointment)).setText(String.valueOf(var1.appointment.getName()));
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_document_info);
      long var2 = this.getIntent().getLongExtra("ru.sviridov.techsupervision.DOCUMENT_ID", 0L);
      Uri var4 = ContentUris.withAppendedId(UserDataProvider.getContentUri("Document"), var2);
      Document var5 = (Document)CupboardFactory.cupboard().withContext(this).get(var4, Document.class);
      this.setTitle(var5.title);
      this.fillViews(var5);
     // Mint.logEvent("open document info", MintLogLevel.Info);
   }
}
