package com.meli.trainingml;

import com.meli.trainingml.ListItemsFragment.ListSelectedListener;
import com.meli.trainingml.R;
import com.meli.trainingml.items.Item;
import com.meli.trainingml.util.CacheManager;
import com.meli.trainingml.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity implements ListSelectedListener{

    private final static String LOGTAG = MainActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CacheManager.getInstance().setDestinationFolder(getCacheDir().getAbsolutePath());
        // Check that the activity is using the layout version with the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            ListItemsFragment listItemsFragment = new ListItemsFragment();
            listItemsFragment.setRetainInstance(true);
            Utils.openFragment(getSupportFragmentManager(), listItemsFragment, getIntent().getExtras(), R.id.fragment_container, false, "list");
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchItemSelected(Item item) {
        
        ItemDetailFragment itemDetailFragment = (ItemDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.itemDetailFragment);
        if (itemDetailFragment != null) {
            // If article itemDetailFragment is available, we're in two-pane layout...
            // Call a method in the ItemDetailFragment to update its content
            itemDetailFragment.updateItemView(item);
        } else {
            Bundle options = new Bundle();
            options.putSerializable("item", item);
            itemDetailFragment = new ItemDetailFragment();
            Utils.openFragment(getSupportFragmentManager(), itemDetailFragment, options, R.id.fragment_container, true, "detail");
        }
    }

}
