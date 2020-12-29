

package ru.sviridov.techsupervision.defects;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nispok.views.SlidingTabLayout;

import nl.qbusict.cupboard.CupboardFactory;
import ru.sviridov.techsupervision.ToolbarActivity;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.service.UpdateService;


public class AddDefectActivity
        extends ToolbarActivity {
   public static final String DEFECT_ID = "ru.sviridov.techsupervision.DEFECT_ID";
   public static final String DOCUMENT_ID = "ru.sviridov.techsupervision.DOCUMENT_ID";
   public static final int LOADER_PICTURES = 1;
   public static final String SELECTED_TAB = "ru.sviridov.techsupervision.SELECTED_TAB";
   private int defectId = -1;
   private long openedTime;

   private Defect createDefect(int n) {
      Defect defect = new Defect();
      defect.documentId = n;
      defect.updated = System.currentTimeMillis();
      Uri uri = UserDataProvider.getContentUri("Defect");
      defect._id = Long.parseLong(CupboardFactory.cupboard().withContext((Context)this).put(uri, defect).getLastPathSegment());
      return defect;
   }

   @Override
   public void onCreate(Bundle object) {
      super.onCreate((Bundle)object);
      this.setContentView(R.layout.activity_add_defect);
      int n = this.getIntent().getIntExtra(DOCUMENT_ID, -1);
      this.defectId = this.getIntent().getIntExtra(DEFECT_ID, -1);
      String var6;
      if (this.defectId == -1) {
         var6 = "start new defect";
      } else {
         var6 = "edit defect";
      }
     // Mint.logEvent((String)object);
      if (this.defectId == -1) {
         this.defectId = this.createDefect((int)n)._id.intValue();
         this.setTitle(R.string.new_defect);
         ActionBar var7 = this.getSupportActionBar();
         if (var7 != null) {
            var7.setHomeAsUpIndicator(R.drawable.close);
         }
      }
      ViewPager viewPager = (ViewPager)this.findViewById(R.id.stlTabs);
      SlidingTabLayout slidingTabLayout = (SlidingTabLayout)this.findViewById(R.id.tab_indicator);
      slidingTabLayout.setCustomTabView(R.layout.tab, R.id.tvTitle);
      slidingTabLayout.setDistributeEvenly(true);
      TabsAdapter var8 = new TabsAdapter(this.getSupportFragmentManager());

      Fragment fragment = DefectDetailsFragment.getInstance(this.defectId);
      var8.addTab(this.getString(R.string.description), fragment);
      fragment = PicturesListFragment.getInstance(this.defectId);
      var8.addTab(this.getString(R.string.images), fragment);
      viewPager.setAdapter(var8);
      slidingTabLayout.setViewPager(viewPager);
      viewPager.setCurrentItem(this.getIntent().getIntExtra(SELECTED_TAB, 0));
      this.openedTime = System.currentTimeMillis();
   }

   public boolean onCreateOptionsMenu(Menu menu2) {
      if (this.getIntent().getIntExtra(DEFECT_ID, -1) != -1) return super.onCreateOptionsMenu(menu2);
      this.getMenuInflater().inflate(R.menu.document_add, menu2);
      return super.onCreateOptionsMenu(menu2);
   }

   @Override
   protected void onDestroy() {
      Object object = ContentUris.withAppendedId((Uri)UserDataProvider.getContentUri("Defect"), (long)this.defectId);
      object = (Defect)((Object)CupboardFactory.cupboard().withContext((Context)this).get((Uri)object, Defect.class));
      Uri uri = UserDataProvider.getContentUri("Picture");
      int n = CupboardFactory.cupboard().withContext((Context)this).query(uri, Picture.class).withSelection("defectId=" + this.defectId, new String[0]).list().size();
      UpdateService.call((Context)this);

      super.onDestroy();
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem menuItem) {
      switch (menuItem.getItemId()) {
         case android.R.id.home: {
            if (this.getIntent().getIntExtra(DEFECT_ID, -1) != -1) return super.onOptionsItemSelected(menuItem);
            Uri uri = UserDataProvider.getContentUri("Defect");
            CupboardFactory.cupboard().withContext((Context)this).delete(uri, "_id=?", String.valueOf(this.defectId));
            return super.onOptionsItemSelected(menuItem);
         }
         case R.id.save: {
            this.finish();
            break;
         }
      }
      return super.onOptionsItemSelected(menuItem);
   }
}

