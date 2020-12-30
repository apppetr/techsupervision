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

public class ToolbarActivity
        extends AppCompatActivity {
   private void setupToolbar() {
      this.setSupportActionBar(this.getToolbar());
      ActionBar actionBar = this.getSupportActionBar();
      if (actionBar == null) return;
      actionBar.setDisplayHomeAsUpEnabled(true);
   }

   public Toolbar getToolbar() {
      return (Toolbar) this.findViewById(R.id.action_bar);
   }

   public boolean onOptionsItemSelected(MenuItem menuItem) {
      if (menuItem.getItemId() != android.R.id.home) return super.onOptionsItemSelected(menuItem);
      NavUtils.navigateUpFromSameTask(this);
      return true;
   }

   @Override
   public void setContentView(int n) {
      super.setContentView(n);
      this.setupToolbar();
   }

   @Override
   public void setContentView(View view) {
      super.setContentView(view);
      this.setupToolbar();
   }

   @Override
   public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
      super.setContentView(view, layoutParams);
      this.setupToolbar();
   }
}

