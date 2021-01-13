package ru.lihachev.norm31937;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ru.lihachev.norm31937.documents.DocumentsActivity;
import ru.lihachev.norm31937.free.R;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        TextView modelTextview = (TextView)findViewById(R.id.showDocs);
        modelTextview.setOnClickListener(v -> {
                    Intent i = new Intent(ScrollingActivity.this, DocumentsActivity.class);
                    startActivity(i);
                    finish();
        });
    }
}