package ru.lihachev.norm31937.defects;

import android.content.DialogInterface;
import android.content.Intent;
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
import ru.lihachev.norm31937.GreatApplication;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.pictures.PictureEditActivity;
import ru.lihachev.norm31937.utils.alerts.Dialogs;
import ru.lihachev.norm31937.widgets.RVCursorAdapter;

public class PicturesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RVCursorAdapter.onItemSelectedListener {
   private PicturessAdapter adapter;

   public static PicturesListFragment getInstance(int defectId) {
      PicturesListFragment fragment = new PicturesListFragment();
      Bundle args = new Bundle();
      args.putInt(AddDefectActivity.DEFECT_ID, defectId);
      fragment.setArguments(args);
      return fragment;
   }

   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.adapter = new PicturessAdapter(getActivity(), (Cursor) null);
      this.adapter.setOnItemSelectedListener(this);
      this.adapter.setLongClickListener(deleteListener());
      getLoaderManager().initLoader(1, getArguments(), this);
   }

   private RVCursorAdapter.onItemSelectedListener deleteListener() {
      return new RVCursorAdapter.onItemSelectedListener() {
         public void onItemSelected(final Cursor item) {
            Dialogs.show(PicturesListFragment.this.getActivity(), R.string.delete, R.string.want_delete_picture, R.string.yes, R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(@NonNull DialogInterface dialog, int which) {
                  if (which == -1) {
                     Uri uri = UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL);
                     CupboardFactory.cupboard().withContext(GreatApplication.getAppContext()).delete(uri, "_id=?", String.valueOf(((Picture) CupboardFactory.cupboard().withCursor(item).get(Picture.class)).getId()));
                  }
               }
            });
         }
      };
   }

   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_pictures, container, false);
   }

   public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      view.findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
         public void onClick(@NonNull View v) {
         //   Mint.logEvent(Metrics.PRESS_ADD_PICTURE);
            Intent intent = new Intent(PicturesListFragment.this.getActivity(), PictureEditActivity.class);
            intent.putExtras(PicturesListFragment.this.getArguments());
            PicturesListFragment.this.startActivity(intent);
         }
      });

      RecyclerView photos = (RecyclerView) view.findViewById(android.R.id.list);
      photos.setLayoutManager(new LinearLayoutManager(getActivity()));
      photos.setAdapter(this.adapter);
   }

   public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
      int defectId = 0;
      if (args != null) {
         defectId = args.getInt(AddDefectActivity.DEFECT_ID, 0);
      }
      return new CursorLoader(getActivity(), UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), (String[]) null, "defectId=" + defectId, (String[]) null, (String) null);
   }

   public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      this.adapter.changeCursor(cursor);
   }

   public void onLoaderReset(Loader<Cursor> loader) {
      this.adapter.changeCursor((Cursor) null);
   }

   public void onItemSelected(Cursor item) {
      Intent editIntent = new Intent(getActivity(), PictureEditActivity.class);
      editIntent.putExtra(PictureEditActivity.PICTURE_ID, ((Picture) CupboardFactory.cupboard().withCursor(item).get(Picture.class)).getId().intValue());
      startActivity(editIntent);
   }

   public void onDestroy() {
      super.onDestroy();
      this.adapter.setLongClickListener((RVCursorAdapter.onItemSelectedListener) null);
      this.adapter.setOnItemSelectedListener((RVCursorAdapter.onItemSelectedListener) null);
      this.adapter = null;
      getLoaderManager().destroyLoader(1);
   }
}
