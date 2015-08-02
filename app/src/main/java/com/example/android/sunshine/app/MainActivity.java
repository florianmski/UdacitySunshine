package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
    private final static String FRAGMENT_TAG = "forecast_fragment";
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map) {
            openMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMap() {
        String location = Utility.getPreferredLocation(this);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(formatLocation(location));

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private Uri formatLocation(String location) {
        return Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(this.location)) {
            ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (null != ff) {
                ff.onLocationChanged();
            }
            this.location = location;
        }
    }

}
