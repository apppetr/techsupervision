/*
 * Decompiled with CFR <Could not determine version>.
 *
 * Could not load the following classes:
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package ru.sviridov.techsupervision;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ToolbarActivity extends AppCompatActivity {
   private void setupToolbar() {
      setSupportActionBar(getToolbar());
      ActionBar aBar = getSupportActionBar();
      if (aBar != null) {
         aBar.setDisplayHomeAsUpEnabled(true);
      }
   }

   public Toolbar getToolbar() {
      return (Toolbar) findViewById(R.id.action_bar);
   }

   public boolean onOptionsItemSelected(MenuItem item) {

      if (item.getItemId() != android.R.id.home) {
         return super.onOptionsItemSelected(item);
      }
      NavUtils.navigateUpFromSameTask(this);
      return true;
   }

   public void setContentView(int layoutResID) {
      super.setContentView(layoutResID);
      setupToolbar();
   }

   public void setContentView(View view) {
      super.setContentView(view);
      setupToolbar();
   }

   public void setContentView(View view, ViewGroup.LayoutParams params) {
      super.setContentView(view, params);
      setupToolbar();
   }
}

