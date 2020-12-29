package ru.sviridov.techsupervision;


import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class ToolbarActivity extends AppCompatActivity {
   private void setupToolbar() {
      this.setSupportActionBar(this.getToolbar());
      ActionBar var1 = this.getSupportActionBar();
      if (var1 != null) {
         var1.setDisplayHomeAsUpEnabled(true);
      }

   }

   public Toolbar getToolbar() {
      return (Toolbar)this.findViewById(R.id.action_bar);
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2;
      if (var1.getItemId() == 16908332) {
         NavUtils.navigateUpFromSameTask(this);
         var2 = true;
      } else {
         var2 = super.onOptionsItemSelected(var1);
      }

      return var2;
   }

   public void setContentView(int var1) {
      super.setContentView(var1);
      this.setupToolbar();
   }

   public void setContentView(View var1) {
      super.setContentView(var1);
      this.setupToolbar();
   }

   public void setContentView(View var1, LayoutParams var2) {
      super.setContentView(var1, var2);
      this.setupToolbar();
   }
}
