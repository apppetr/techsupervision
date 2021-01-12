package ru.lihachev.norm31937.documents;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Document;

public class DocumentsAdapter extends ResourceCursorAdapter implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
   private final Context context;
   private int defCountIndx = -1;
   private Long docId = -1L;
   private SimpleDateFormat format = new SimpleDateFormat("d MMMM y", Locale.getDefault());
   private final OnItemClicked itemClickListener;

   public DocumentsAdapter(@NonNull Context context2, @Nullable Cursor c, @Nullable OnItemClicked itemClickListener2) {
      super(context2, R.layout.item_document, c, false);
      this.context = context2;
      initIndexes(c);
      this.itemClickListener = itemClickListener2;
   }

   private void initIndexes(@Nullable Cursor c) {
      if (c != null && this.defCountIndx == -1) {
         this.defCountIndx = c.getColumnIndex(UserDataHelper.DOCUMENT_WITH_DEFECTS.DEFECT_COUNT);
      }
   }

   public Cursor swapCursor(Cursor newCursor) {
      initIndexes(newCursor);
      return super.swapCursor(newCursor);
   }

   @NonNull
   public View newView(Context context2, Cursor cursor, ViewGroup parent) {
      View v = super.newView(context2, cursor, parent);
      DocumentsHolder h = new DocumentsHolder();
      h.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
      h.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
      h.ivMore = v.findViewById(R.id.ivMore);
      h.ivMore.setOnClickListener(this);
      v.setTag(h);
      return v;
   }

   public void bindView(@NonNull View view, Context context2, @NonNull Cursor cursor) {
      String defCount = formatDefectCount(cursor.getInt(this.defCountIndx));
      Document document = (Document) CupboardFactory.cupboard().withCursor(cursor).get(Document.class);
      DocumentsHolder h = (DocumentsHolder) view.getTag();
      h.tvTitle.setText(document.title);
      String date = this.format.format(new Date(document.date));
      h.tvDesc.setText(context2.getString(R.string.format_document_list, new Object[]{defCount, date}));
      h.ivMore.setTag(document._id);
   }

   private String formatDefectCount(int count) {
      if (count > 10 && count < 20) {
         return count + " дефектов";
      }
      switch (count % 10) {
         case 1:
            return count + " дефект";
         case 2:
         case 3:
         case 4:
            return count + " дефекта";
         default:
            return count + " дефектов";
      }
   }

   public void onClick(@NonNull View v) {
      this.docId = (Long) v.getTag();
      PopupMenu popup = new PopupMenu(v.getContext(), v);
      popup.setOnMenuItemClickListener(this);
      popup.getMenuInflater().inflate(R.menu.popup_menu_documents, popup.getMenu());
      popup.show();
   }

   public boolean onMenuItemClick(MenuItem item) {
      if (this.itemClickListener == null) {
         return true;
      }
      this.itemClickListener.onClicked(this.docId.longValue(), item.getItemId());
      return true;
   }

   /* renamed from: ru.lihachev.norm31937.documents.DocumentsAdapter$DocumentsHolder */
   private class DocumentsHolder {
      View ivMore;
      TextView tvDesc;
      TextView tvTitle;

      private DocumentsHolder() {
      }
   }
}
