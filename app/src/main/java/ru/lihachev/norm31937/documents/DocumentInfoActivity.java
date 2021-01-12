package ru.lihachev.norm31937.documents;

import android.content.ContentUris;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.ToolbarActivity;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Document;

public class DocumentInfoActivity extends ToolbarActivity {
   /* access modifiers changed from: protected */
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView((int) R.layout.activity_document_info);
      Document document = (Document) CupboardFactory.cupboard().withContext(this).get(ContentUris.withAppendedId(UserDataProvider.getContentUri(UserDataHelper.DOCUMENT_URL), getIntent().getLongExtra("ru.lihachev.norm31937.DOCUMENT_ID", 0)), Document.class);
      setTitle(document.title);
      fillViews(document);
     // Mint.logEvent(Metrics.OPEN_DOCUMENT_INFO, MintLogLevel.Info);
   }

   private void fillViews(@Nullable Document document) {
      if (document != null) {
         ((TextView) findViewById(R.id.tvAddress)).setText(document.address);
         ((TextView) findViewById(R.id.tvYear)).setText(String.valueOf(document.year));
         ((TextView) findViewById(R.id.tvResponsibility)).setText(document.responsibility.getName());
         ((TextView) findViewById(R.id.tvSizes)).setText(document.sizes);
         ((TextView) findViewById(R.id.tvFloors)).setText(String.valueOf(document.floors));
         ((TextView) findViewById(R.id.tvAppointment)).setText(String.valueOf(document.appointment.getName()));
      }
   }
}
