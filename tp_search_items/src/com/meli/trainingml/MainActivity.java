package com.meli.trainingml;


import com.meli.trainingml.util.CacheManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    private final static String LOGTAG = MainActivity.class.getSimpleName();
    EditText edit;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CacheManager.getInstance().setDestinationFolder(getCacheDir().getAbsolutePath());
        initComponents();
    }

    private void initComponents() {
        Log.i(LOGTAG, "initComponents");
        edit = (EditText)findViewById(R.id.editTextProduct);
        search = (Button)findViewById(R.id.btnSearch);

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                findProduct(edit.getText().toString());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void findProduct(String query) {
        //TODO: Save last search in DB
        Bundle options = new Bundle();
        options.putString(ListItemsActivity.SEARCH_KEY, query);
        Intent intent = new Intent(this, ListItemsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(options);
        startActivity(intent);
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

}
