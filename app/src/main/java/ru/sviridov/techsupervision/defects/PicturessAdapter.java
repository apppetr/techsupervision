package ru.sviridov.techsupervision.defects;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.widgets.RVCursorAdapter;

public class PicturessAdapter extends RVCursorAdapter {
   private final LayoutInflater inflater;

   public PicturessAdapter(@NonNull Context var1, @Nullable Cursor var2) {
      super(var1, var2);
      this.inflater = LayoutInflater.from(var1);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder var1, Cursor var2) {

   }

   public void onBindViewHolder(PicturessAdapter.PicturesViewHoder var1, Cursor var2) {
      Picture var3 = (Picture)CupboardFactory.cupboard().withCursor(var2).get(Picture.class);
      Picasso.get().load(var3.getImgUrl()).resizeDimen(R.dimen.width_picture_item, R.dimen.height_picture_item).centerCrop().into(var1.ivPicture);
   }

   public PicturessAdapter.PicturesViewHoder onCreateViewHolder(ViewGroup var1, int var2) {
      return new PicturessAdapter.PicturesViewHoder(this.inflater.inflate(R.layout.item_picture, var1, false));
   }

   public class PicturesViewHoder extends RVCursorAdapter.SelectableViewHolder {
      ImageView ivPicture;

      public PicturesViewHoder(View var2) {
         super(var2);
         this.ivPicture = (ImageView)var2.findViewById(R.id.ivPicture);
      }
   }
}
