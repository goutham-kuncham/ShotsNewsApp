package com.me.shots;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.me.shots.Adapter.BookmarksAdapter;
import com.me.shots.Adapter.NotificationsAdapter;
import com.me.shots.Adapter.VerticalViewPager;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        VerticalViewPager viewPager= (VerticalViewPager) findViewById(R.id.notificationsViewPager);
        NotificationsAdapter adapter=new NotificationsAdapter(this);
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
