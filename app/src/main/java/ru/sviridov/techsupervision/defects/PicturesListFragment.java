package ru.sviridov.techsupervision.defects;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.GreatApplication;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.pictures.PictureEditActivity;
import ru.sviridov.techsupervision.utils.alerts.Dialogs;
import ru.sviridov.techsupervision.widgets.RVCursorAdapter;

public class PicturesListFragment extends Fragment implements LoaderManager.LoaderCallbacks, RVCursorAdapter.onItemSelectedListener {
   private PicturessAdapter adapter;

   private RVCursorAdapter.onItemSelectedListener deleteListener() {
      return new RVCursorAdapter.onItemSelectedListener() {
         public void onItemSelected(final Cursor var1) {
            OnClickListener var2 = new OnClickListener() {
               public void onClick(@NonNull DialogInterface var1x, int var2) {
                  if (var2 == -1) {
                     Picture var3 = (Picture)CupboardFactory.cupboard().withCursor(var1).get(Picture.class);
                     Uri var4 = UserDataProvider.getContentUri("Picture");
                     CupboardFactory.cupboard().withContext(GreatApplication.getAppContext()).delete(var4, "_id=?", String.valueOf(var3.getId()));
                  }

               }
            };
            Dialogs.show(PicturesListFragment.this.getActivity(), R.string.delete, R.string.want_delete_picture, R.string.yes, R.string.cancel, var2);
         }
      };
   }

   public static PicturesListFragment getInstance(int var0) {
      PicturesListFragment var1 = new PicturesListFragment();
      Bundle var2 = new Bundle();
      var2.putInt("ru.sviridov.techsupervision.DEFECT_ID", var0);
      var1.setArguments(var2);
      return var1;
   }

   public void onCreate(@Nullable Bundle var1) {
      super.onCreate(var1);
      this.adapter = new PicturessAdapter(this.getActivity(), (Cursor)null);
      this.adapter.setOnItemSelectedListener(this);
      this.adapter.setLongClickListener(this.deleteListener());
      this.getLoaderManager().initLoader(1, this.getArguments(), this);
   }

   public Loader onCreateLoader(int var1, @Nullable Bundle var2) {
      var1 = 0;
      if (var2 != null) {
         var1 = var2.getInt("ru.sviridov.techsupervision.DEFECT_ID", 0);
      }

      Uri var3 = UserDataProvider.getContentUri("Picture");
      return new CursorLoader(this.getActivity(), var3, (String[])null, "defectId=" + var1, (String[])null, (String)null);
   }

   @Override
   public void onLoadFinished(Loader var1, Object var2) {

   }

   public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {
      return var1.inflate(R.layout.fragment_pictures, var2, false);
   }

   public void onDestroy() {
      super.onDestroy();
      this.adapter.setLongClickListener((RVCursorAdapter.onItemSelectedListener)null);
      this.adapter.setOnItemSelectedListener((RVCursorAdapter.onItemSelectedListener)null);
      this.adapter = null;
      this.getLoaderManager().destroyLoader(1);
   }

   public void onItemSelected(Cursor var1) {
      Picture var3 = (Picture)CupboardFactory.cupboard().withCursor(var1).get(Picture.class);
      Intent var2 = new Intent(this.getActivity(), PictureEditActivity.class);
      var2.putExtra("ru.sviridov.techsupervision.pictures.PICTURE_ID", var3.getId().intValue());
      this.startActivity(var2);
   }

   public void onLoadFinished(Loader var1, Cursor var2) {
      this.adapter.changeCursor(var2);
   }

   public void onLoaderReset(Loader var1) {
      this.adapter.changeCursor((Cursor)null);
   }

   public void onViewCreated(View var1, @Nullable Bundle var2) {
      super.onViewCreated(var1, var2);
      var1.findViewById(R.id.ivAdd).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(@NonNull View var1) {
           // Mint.logEvent("presses add picture");
            Intent var2 = new Intent(PicturesListFragment.this.getActivity(), PictureEditActivity.class);
            var2.putExtras(PicturesListFragment.this.getArguments());
            PicturesListFragment.this.startActivity(var2);
         }
      });
      RecyclerView var3 = (RecyclerView)var1.findViewById(16908298);
      var3.setLayoutManager(new LinearLayoutManager(this.getActivity()));
      var3.setAdapter(this.adapter);
   }
}
