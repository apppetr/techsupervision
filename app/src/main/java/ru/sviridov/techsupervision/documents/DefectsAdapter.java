package ru.sviridov.techsupervision.documents;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupMenu.OnMenuItemClickListener;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.widgets.RVCursorAdapter;

public class DefectsAdapter extends RVCursorAdapter {
   private OnClickListener clickListener;
   private SimpleDateFormat format = new SimpleDateFormat("d MMMM y", Locale.getDefault());
   private int imgIndx = -1;
   private final LayoutInflater inflater;

   public DefectsAdapter(@NonNull Context var1, @Nullable Cursor var2, OnClickListener var3) {
      super(var1, var2);
      this.clickListener = var3;
      this.inflater = LayoutInflater.from(var1);
   }

   private void setImage(Context var1, ImageView var2, String var3) {
      if (var3 == null) {
         var2.setScaleType(ScaleType.CENTER);
         var2.setImageResource(R.drawable.no_photo);
      } else {
         var2.setScaleType(ScaleType.CENTER_CROP);

         Picasso.get().load(var3).resizeDimen(R.dimen.width_picture_item, R.dimen.height_picture_defect).centerCrop().into(var2);
      }

   }

   public void delete(View var1, final int var2) {
      this.noNotifyOncePlease();
      Uri var3 = UserDataProvider.getContentUri("Defect");
      CupboardFactory.cupboard().withContext(this.context).delete(var3, "_id=?", String.valueOf(this.getItemId(var2)));
      var1.postDelayed(new Runnable() {
         public void run() {
            DefectsAdapter.this.notifyItemRemoved(var2);
         }
      }, 100L);
   }

   protected void initIndexes(@NonNull Cursor var1) {
      this.imgIndx = var1.getColumnIndex("imgUrl");
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder var1, Cursor var2) {

   }

   public void onBindViewHolder(DefectsAdapter.DefectHolder var1, Cursor var2) {
      this.setImage(this.context, var1.ivImage, var2.getString(this.imgIndx));
      Defect var4 = (Defect)CupboardFactory.cupboard().withCursor(var2).get(Defect.class);

      if (var4.place != null) {
         var1.tvTitle.setText(this.context.getString(   R.string.format_defect_title, new Object[]{var4.getElement(), var4.getMaterial(), var4.place}));
      } else if (var4.getElement() != null) {
         var1.tvTitle.setText(this.context.getString(R.string.format_defect_title_short, new Object[]{var4.getElement(), var4.getMaterial()}));
      } else {
         var1.tvTitle.setText(R.string.new_defect);
      }

      var1.tvDate.setText(this.format.format(new Date(var4.updated)));
      var1.tvCategory.setText(this.context.getString(R.string.danger_category_format, new Object[]{var4.getCategory()}));
      var1.tvDesc.setText(var4.getNiceProblems());
      int var3 = var4._id.intValue();
      var1.tvEdit.setTag(var3);
      var1.tvAddPhoto.setTag(var3);
      var1.ivImage.setTag(var3);
   }

   public DefectsAdapter.DefectHolder onCreateViewHolder(ViewGroup var1, int var2) {
      DefectsAdapter.DefectHolder var3 = new DefectsAdapter.DefectHolder(this.inflater.inflate(R.layout.item_defect, var1, false));
      var3.tvEdit.setOnClickListener(this.clickListener);
      var3.tvAddPhoto.setOnClickListener(this.clickListener);
      var3.ivImage.setOnClickListener(this.clickListener);
      return var3;
   }

   public class DefectHolder extends RecyclerView.ViewHolder implements OnClickListener, OnMenuItemClickListener {
      private final ImageView ivImage;
      private final View ivMore;
      private final View tvAddPhoto;
      private final TextView tvCategory;
      private final TextView tvDate;
      private final TextView tvDesc;
      private final View tvEdit;
      private final TextView tvTitle;

      public DefectHolder(View var2) {
         super(var2);
         this.tvTitle = (TextView)var2.findViewById(R.id.tvTitle);
         this.tvDate = (TextView)var2.findViewById(R.id.tvDate);
         this.tvCategory = (TextView)var2.findViewById(R.id.tvCategory);
         this.ivImage = (ImageView)var2.findViewById(R.id.ivImage);
         this.tvDesc = (TextView)var2.findViewById(R.id.tvDesc);
         this.ivMore = var2.findViewById(R.id.ivMore);
         this.tvEdit = var2.findViewById(R.id.tvEdit);
         this.tvAddPhoto = var2.findViewById(R.id.tvAddPhoto);
         this.ivMore.setOnClickListener(this);
      }

      public void onClick(@NonNull View var1) {
         PopupMenu var2 = new PopupMenu(DefectsAdapter.this.context, var1);
         var2.setOnMenuItemClickListener(this);
         var2.getMenuInflater().inflate(R.menu.popup_menu_defects, var2.getMenu());
         var2.show();
      }

      public boolean onMenuItemClick(MenuItem var1) {
         boolean var2 = true;
         switch(var1.getItemId()) {
         case R.id.menu_popup_edit:
            DefectsAdapter.this.clickListener.onClick(this.tvEdit);
            break;
         case R.id.menu_popup_delete:
            DefectsAdapter.this.delete(this.itemView, this.getAdapterPosition());
            break;
         default:
            var2 = false;
         }

         return var2;
      }
   }
}
