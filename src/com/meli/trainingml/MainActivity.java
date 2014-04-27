package com.meli.trainingml;

import java.util.HashMap;

import com.meli.trainingml.items.FindTask;
import com.meli.trainingml.util.IObserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity implements IObserver{

	private final static String LOGTAG = MainActivity.class.getSimpleName();
	EditText edit;
	Button search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
	private void findProduct(String string) {
		FindTask findTask = new FindTask(this);
		findTask.registerObserver(this);
		HashMap<String, String> params = new HashMap<String, String>(); 
        params.put("product",  string);
		findTask.execute(params);
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
	public void update(Object data) {
		//TODO: Add validations
		String response = (String) data;
		Bundle options = new Bundle();
		options.putString("response", response);
		Intent intent = new Intent(this, ListItemsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(options);
		startActivity(intent);
		
	}

}
