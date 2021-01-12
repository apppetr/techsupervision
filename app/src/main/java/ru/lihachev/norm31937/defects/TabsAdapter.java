package ru.lihachev.norm31937.defects;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
public class TabsAdapter extends FragmentPagerAdapter {
   private final List<Fragment> fragments = new ArrayList(2);
   private final List<CharSequence> titles = new ArrayList(2);

   public TabsAdapter(FragmentManager fm) {
      super(fm);
   }

   public int addTab(String tabTitle, Fragment newFragment) {
      this.titles.add(tabTitle);
      this.fragments.add(newFragment);
      notifyDataSetChanged();
      return this.fragments.size() - 1;
   }

   public CharSequence getPageTitle(int position) {
      return this.titles.get(position);
   }

   public int getCount() {
      return this.fragments.size();
   }

   public Fragment getItem(int position) {
      return this.fragments.get(position);
   }
}
