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
import ru.sviridov.techsupervision.db.UserDataHelper;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.widgets.RVCursorAdapter;

public class DefectsAdapter extends RVCursorAdapter<DefectsAdapter.DefectHolder> {
   /* access modifiers changed from: private */
   public View.OnClickListener clickListener;
   private SimpleDateFormat format = new SimpleDateFormat("d MMMM y", Locale.getDefault());
   private int imgIndx = -1;
   private final LayoutInflater inflater;

   public DefectsAdapter(@NonNull Context context, @Nullable Cursor cursor, View.OnClickListener clickListener2) {
      super(context, cursor);
      this.clickListener = clickListener2;
      this.inflater = LayoutInflater.from(context);
   }

   public DefectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      DefectHolder dh = new DefectHolder(this.inflater.inflate(R.layout.item_defect, parent, false));
      dh.tvEdit.setOnClickListener(this.clickListener);
      dh.tvAddPhoto.setOnClickListener(this.clickListener);
      dh.ivImage.setOnClickListener(this.clickListener);
      return dh;
   }

   public void onBindViewHolder(DefectHolder h, Cursor cursor) {
      setImage(this.context, h.ivImage, cursor.getString(this.imgIndx));
      Defect defect = (Defect) CupboardFactory.cupboard().withCursor(cursor).get(Defect.class);
      if (defect.place != null) {
         h.tvTitle.setText(this.context.getString(R.string.format_defect_title, defect.getElement(), defect.getMaterial(), defect.place));
      } else if (defect.getElement() != null) {
         h.tvTitle.setText(this.context.getString(R.string.format_defect_title_short, defect.getElement(), defect.getMaterial()));
      } else {
         h.tvTitle.setText(R.string.new_defect);
      }
      h.tvDate.setText(this.format.format(new Date(defect.updated)));
      h.tvCategory.setText(this.context.getString(R.string.danger_category_format, defect.getCategory()));
      h.tvDesc.setText(defect.getNiceProblems());
      int id = defect._id.intValue();
      h.tvEdit.setTag(id);
      h.tvAddPhoto.setTag(id);
      h.ivImage.setTag(id);
   }

   private void setImage(Context context, ImageView ivImage, String imageUrl) {
      if (imageUrl == null) {
         ivImage.setScaleType(ImageView.ScaleType.CENTER);
         ivImage.setImageResource(R.drawable.no_photo);
         return;
      }
      ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
      Picasso.get().load(imageUrl).resizeDimen(R.dimen.width_picture_item, R.dimen.height_picture_defect).centerCrop().into(ivImage);
   }

   /* access modifiers changed from: protected */
   public void initIndexes(@NonNull Cursor cursor) {
      this.imgIndx = cursor.getColumnIndex(UserDataHelper.DEFECT_WITH_PICTURE.IMG_URL);
   }

   public void delete(View view, final int position) {
      noNotifyOncePlease();
      Uri uri = UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL);
      CupboardFactory.cupboard().withContext(this.context).delete(uri, "_id=?", String.valueOf(getItemId(position)));
      view.postDelayed(new Runnable() {
         public void run() {
            DefectsAdapter.this.notifyItemRemoved(position);
         }
      }, 100);
   }

   /* renamed from: ru.sviridov.techsupervision.documents.DefectsAdapter$DefectHolder */
   public class DefectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
      /* access modifiers changed from: private */
      public final ImageView ivImage;
      private final View ivMore;
      /* access modifiers changed from: private */
      public final View tvAddPhoto;
      /* access modifiers changed from: private */
      public final TextView tvCategory;
      /* access modifiers changed from: private */
      public final TextView tvDate;
      /* access modifiers changed from: private */
      public final TextView tvDesc;
      /* access modifiers changed from: private */
      public final View tvEdit;
      /* access modifiers changed from: private */
      public final TextView tvTitle;

      public DefectHolder(View v) {
         super(v);
         this.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
         this.tvDate = (TextView) v.findViewById(R.id.tvDate);
         this.tvCategory = (TextView) v.findViewById(R.id.tvCategory);
         this.ivImage = (ImageView) v.findViewById(R.id.ivImage);
         this.tvDesc = (TextView) v.findViewById(R.id.tvDesc);
         this.ivMore = v.findViewById(R.id.ivMore);
         this.tvEdit = v.findViewById(R.id.tvEdit);
         this.tvAddPhoto = v.findViewById(R.id.tvAddPhoto);
         this.ivMore.setOnClickListener(this);
      }

      public void onClick(@NonNull View view) {
         PopupMenu popup = new PopupMenu(DefectsAdapter.this.context, view);
         popup.setOnMenuItemClickListener(this);
         popup.getMenuInflater().inflate(R.menu.popup_menu_defects, popup.getMenu());
         popup.show();
      }

      public boolean onMenuItemClick(MenuItem item) {
         switch (item.getItemId()) {
            case R.id.menu_popup_edit /*2131558606*/:
               DefectsAdapter.this.clickListener.onClick(this.tvEdit);
               return true;
            case R.id.menu_popup_delete /*2131558607*/:
               DefectsAdapter.this.delete(this.itemView, getAdapterPosition());
               return true;
            default:
               return false;
         }
      }
   }
}
