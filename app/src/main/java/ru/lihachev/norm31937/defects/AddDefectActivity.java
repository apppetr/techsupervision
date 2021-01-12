package ru.lihachev.norm31937.defects;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import ru.lihachev.norm31937.ToolbarActivity;
import ru.lihachev.norm31937.db.UserDataHelper;
import ru.lihachev.norm31937.db.UserDataProvider;
import ru.lihachev.norm31937.free.R;
import ru.lihachev.norm31937.objects.Defect;
import ru.lihachev.norm31937.objects.Picture;
import ru.lihachev.norm31937.service.UpdateService;

public class AddDefectActivity extends ToolbarActivity {
   public static final String DEFECT_ID = "ru.lihachev.norm31937.DEFECT_ID";
   public static final String DOCUMENT_ID = "ru.lihachev.norm31937.DOCUMENT_ID";
   public static final int LOADER_PICTURES = 1;
   public static final String SELECTED_TAB = "ru.lihachev.norm31937.SELECTED_TAB";
   private int defectId = -1;
   private long openedTime;
   private TabLayout tabLayout;
   private ViewPager viewPager;
   private MyViewPagerAdapter adapter;

  // SlidingTabLayout tabLayout;
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_add_defect);
      int documentId = getIntent().getIntExtra("ru.lihachev.norm31937.DOCUMENT_ID", -1);
      this.defectId = getIntent().getIntExtra(DEFECT_ID, -1);
     // Mint.logEvent(this.defectId == -1 ? Metrics.START_NEW_DEFECT : Metrics.EDIT_DEFECT);
      if (this.defectId == -1) {
         this.defectId = createDefect(documentId)._id.intValue();
         setTitle(R.string.new_defect);

         ActionBar actionbar = getSupportActionBar();
         if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.close);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Add Defect"); // hide the title bar
         }
      }
      tabLayout = (TabLayout) findViewById(R.id.tab_layout);
      viewPager = (ViewPager) findViewById(R.id.pager);

      if (viewPager != null) {
         setupViewPager(viewPager);
      }

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
         case android.R.id.home:
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

   public class MyViewPagerAdapter extends FragmentStatePagerAdapter {


      private final List<Fragment> myFragments = new ArrayList<>();
      private final List<String> myFragmentTitles = new ArrayList<>();
      private Context context;

      public MyViewPagerAdapter(FragmentManager fm, Context context) {
         super(fm);
         this.context = context;
      }

      public void addFragment(Fragment fragment, String title) {
         myFragments.add(fragment);
         myFragmentTitles.add(title);
      }

      @Override
      public Fragment getItem(int position) {
         return myFragments.get(position);
      }

      @Override
      public int getCount() {
         return myFragments.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {
         return myFragmentTitles.get(position);
      }
   }

   private void setupViewPager(ViewPager viewPager) {
      adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
      adapter.addFragment(DefectDetailsFragment.getInstance(this.defectId), getString(R.string.description));
      adapter.addFragment(PicturesListFragment.getInstance(this.defectId), getString(R.string.images));
      viewPager.setAdapter(adapter);
      tabLayout.setupWithViewPager(viewPager);
   }

}