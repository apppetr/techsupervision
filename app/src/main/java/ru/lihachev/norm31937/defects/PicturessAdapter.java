package ru.lihachev.norm31937.defects;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.widgets.RVCursorAdapter;
public class PicturessAdapter extends RVCursorAdapter<PicturessAdapter.PicturesViewHoder> {
   private final LayoutInflater inflater;

   public PicturessAdapter(@NonNull Context context, @Nullable Cursor cursor) {
      super(context, cursor);
      this.inflater = LayoutInflater.from(context);
   }

   public void onBindViewHolder(PicturesViewHoder h, Cursor cursor) {
      Picasso.get().load(((Picture) CupboardFactory.cupboard().withCursor(cursor).get(Picture.class)).getImgUrl()).resizeDimen(R.dimen.width_picture_item, R.dimen.height_picture_item).centerCrop().into(h.ivPicture);
   }

   public PicturesViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new PicturesViewHoder(this.inflater.inflate(R.layout.item_picture, parent, false));
   }

   /* renamed from: ru.lihachev.norm31937.defects.PicturessAdapter$PicturesViewHoder */
   public class PicturesViewHoder extends RVCursorAdapter.SelectableViewHolder {
      ImageView ivPicture;

      public PicturesViewHoder(View itemView) {
         super(itemView);
         this.ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
      }
   }
}
