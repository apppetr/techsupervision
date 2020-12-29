package ru.sviridov.techsupervision.documents;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Document;

public class DocumentsAdapter extends ResourceCursorAdapter implements OnClickListener, PopupMenu.OnMenuItemClickListener {
   private final Context context;
   private int defCountIndx = -1;
   private Long docId = -1L;
   private SimpleDateFormat format = new SimpleDateFormat("d MMMM y", Locale.getDefault());
   private final OnItemClicked itemClickListener;

   public DocumentsAdapter(@NonNull Context var1, @Nullable Cursor var2, @Nullable OnItemClicked var3) {
      super(var1, R.layout.item_document, var2, false);
      this.context = var1;
      this.initIndexes(var2);
      this.itemClickListener = var3;
   }

   private String formatDefectCount(int var1) {
      String var2;
      if (var1 > 10 && var1 < 20) {
         var2 = var1 + " дефектов";
      } else {
         switch(var1 % 10) {
         case 1:
            var2 = var1 + " дефект";
            break;
         case 2:
         case 3:
         case 4:
            var2 = var1 + " дефекта";
            break;
         default:
            var2 = var1 + " дефектов";
         }
      }

      return var2;
   }

   private void initIndexes(@Nullable Cursor var1) {
      if (var1 != null && this.defCountIndx == -1) {
         this.defCountIndx = var1.getColumnIndex("defect_count");
      }

   }

   public void bindView(@NonNull View var1, Context var2, @NonNull Cursor var3) {
      String var4 = this.formatDefectCount(var3.getInt(this.defCountIndx));
      Document var7 = (Document)CupboardFactory.cupboard().withCursor(var3).get(Document.class);
      DocumentsAdapter.DocumentsHolder var6 = (DocumentsAdapter.DocumentsHolder)var1.getTag();
      var6.tvTitle.setText(var7.title);
      String var5 = this.format.format(new Date(var7.date));
      var6.tvDesc.setText(var2.getString(R.string.format_document_list, new Object[]{var4, var5}));
      var6.ivMore.setTag(var7._id);
   }

   @NonNull
   public View newView(Context var1, Cursor var2, ViewGroup var3) {
      View var4 = super.newView(var1, var2, var3);
      DocumentsAdapter.DocumentsHolder var5 = new DocumentsAdapter.DocumentsHolder();
      var5.tvTitle = (TextView)var4.findViewById(R.id.tvTitle);
      var5.tvDesc = (TextView)var4.findViewById(R.id.tvDesc);
      var5.ivMore = var4.findViewById(R.id.ivMore);
      var5.ivMore.setOnClickListener(this);
      var4.setTag(var5);
      return var4;
   }

   public void onClick(@NonNull View var1) {
      this.docId = (Long)var1.getTag();
      PopupMenu var2 = new PopupMenu(var1.getContext(), var1);
      var2.setOnMenuItemClickListener(this);
      var2.getMenuInflater().inflate(R.menu.popup_menu_documents, var2.getMenu());
      var2.show();
   }

   public boolean onMenuItemClick(MenuItem var1) {
      if (this.itemClickListener != null) {
         this.itemClickListener.onClicked(this.docId, var1.getItemId());
      }

      return true;
   }

   public Cursor swapCursor(Cursor var1) {
      this.initIndexes(var1);
      return super.swapCursor(var1);
   }

   private class DocumentsHolder {
      View ivMore;
      TextView tvDesc;
      TextView tvTitle;

      private DocumentsHolder() {
      }

      // $FF: synthetic method
      DocumentsHolder(Object var2) {
         this();
      }
   }
}
