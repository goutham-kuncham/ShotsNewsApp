package com.me.shots;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.me.shots.Adapter.BookmarksAdapter;
import com.me.shots.Adapter.VerticalViewPager;

public class BookmarksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bookmarks);

        VerticalViewPager viewPager= (VerticalViewPager) findViewById(R.id.bookmarksViewPager);
        BookmarksAdapter adapter=new BookmarksAdapter(this);
        viewPager.setAdapter(adapter);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        return true;
    }

}
