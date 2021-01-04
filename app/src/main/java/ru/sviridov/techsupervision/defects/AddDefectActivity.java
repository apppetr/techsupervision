

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
import ru.sviridov.techsupervision.db.UserDataHelper;
import ru.sviridov.techsupervision.db.UserDataProvider;
import ru.sviridov.techsupervision.free.R;
import ru.sviridov.techsupervision.objects.Defect;
import ru.sviridov.techsupervision.objects.Picture;
import ru.sviridov.techsupervision.service.UpdateService;


public class AddDefectActivity extends ToolbarActivity {
   public static final String DEFECT_ID = "ru.sviridov.techsupervision.DEFECT_ID";
   public static final String DOCUMENT_ID = "ru.sviridov.techsupervision.DOCUMENT_ID";
   public static final int LOADER_PICTURES = 1;
   public static final String SELECTED_TAB = "ru.sviridov.techsupervision.SELECTED_TAB";
   private int defectId = -1;
   private long openedTime;

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView((int) R.layout.activity_add_defect);
      int documentId = getIntent().getIntExtra("ru.sviridov.techsupervision.DOCUMENT_ID", -1);
      this.defectId = getIntent().getIntExtra(DEFECT_ID, -1);
     // Mint.logEvent(this.defectId == -1 ? Metrics.START_NEW_DEFECT : Metrics.EDIT_DEFECT);
      if (this.defectId == -1) {
         this.defectId = createDefect(documentId)._id.intValue();
         setTitle(R.string.new_defect);
         ActionBar actionbar = getSupportActionBar();
         if (actionbar != null) {
            actionbar.setHomeAsUpIndicator((int) R.drawable.close);
         }
      }
      ViewPager viewPager = (ViewPager) findViewById(R.id.stlTabs);
      SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tab_indicator);
      tabLayout.setCustomTabView(R.layout.tab, R.id.tvTitle);
      tabLayout.setDistributeEvenly(true);
      TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
      tabsAdapter.addTab(getString(R.string.description), DefectDetailsFragment.getInstance(this.defectId));
      tabsAdapter.addTab(getString(R.string.images), PicturesListFragment.getInstance(this.defectId));
      viewPager.setAdapter(tabsAdapter);
      tabLayout.setViewPager(viewPager);
      viewPager.setCurrentItem(getIntent().getIntExtra(SELECTED_TAB, 0));
      this.openedTime = System.currentTimeMillis();
   }

   private Defect createDefect(int documentId) {
      Defect defect = new Defect();
      defect.documentId = documentId;
      defect.updated = System.currentTimeMillis();
      defect._id = Long.valueOf(Long.parseLong(CupboardFactory.cupboard().withContext(this).put(UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL), defect).getLastPathSegment()));
      return defect;
   }

   public boolean onCreateOptionsMenu(Menu menu) {
      if (getIntent().getIntExtra(DEFECT_ID, -1) == -1) {
         getMenuInflater().inflate(R.menu.document_add, menu);
      }
      return super.onCreateOptionsMenu(menu);
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case 16908332:
            if (getIntent().getIntExtra(DEFECT_ID, -1) == -1) {
               Uri uri = UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL);
               CupboardFactory.cupboard().withContext(this).delete(uri, "_id=?", String.valueOf(this.defectId));
               break;
            }
            break;
         case R.id.save /*2131558598*/:
            finish();
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   /* access modifiers changed from: protected */
   public void onDestroy() {
      Defect defect = (Defect) CupboardFactory.cupboard().withContext(this).get(ContentUris.withAppendedId(UserDataProvider.getContentUri(UserDataHelper.DEFECT_URL), (long) this.defectId), Defect.class);
      int pictureCount = CupboardFactory.cupboard().withContext(this).query(UserDataProvider.getContentUri(UserDataHelper.PICTURE_URL), Picture.class).withSelection("defectId=" + this.defectId, new String[0]).list().size();
      UpdateService.call(this);
      if (defect != null) {
       //  Mint.logEvent(Metrics.CHANGED_DEFECT, MintLogLevel.Info, Metrics.toMetrics(defect, this.openedTime, pictureCount));
      }
      super.onDestroy();
   }
}