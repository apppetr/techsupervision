package ru.sviridov.techsupervision.defects;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends FragmentPagerAdapter {
   private final List fragments = new ArrayList(2);
   private final List titles = new ArrayList(2);

   public TabsAdapter(FragmentManager var1) {
      super(var1);
   }

   public int addTab(String var1, Fragment var2) {
      this.titles.add(var1);
      this.fragments.add(var2);
      this.notifyDataSetChanged();
      return this.fragments.size() - 1;
   }

   public int getCount() {
      return this.fragments.size();
   }

   public Fragment getItem(int var1) {
      return (Fragment)this.fragments.get(var1);
   }

   public CharSequence getPageTitle(int var1) {
      return (CharSequence)this.titles.get(var1);
   }
}
