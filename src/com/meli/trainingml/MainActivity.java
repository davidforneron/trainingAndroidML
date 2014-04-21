package com.meli.trainingml;

import java.util.HashMap;
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


public class MainActivity extends Activity {

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

	
	private class FindTask extends AsyncTask<HashMap<String, String>, Void, String> {
	    private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

	    private Context context;
	    private Activity activity;
	    
	    public FindTask(Activity activity) {
	    	this.context = activity.getApplicationContext();
	    	this.activity = activity;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	    	dialog.setMessage("Searching products...");
	    	dialog.show();
	    }

	    @Override
	    protected String doInBackground(HashMap<String, String>... params) {
	        //Task for doing something 
	    	String result = MeliService.findProducts(params[0]);
	        return result;
	    }

	    @Override
	    protected void onPostExecute(String result)
	    {
	    	dialog.dismiss();
	        if(result != null){
	        	showList(result);
	        	Log.i(MainActivity.LOGTAG, result);
	        }
	    }
	    
	    private void showList(String response) {
			Bundle options = new Bundle();
			options.putString("response", response);
			Intent intent = new Intent(context, ListItemsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtras(options);
			context.startActivity(intent);
	    }
	}

}
