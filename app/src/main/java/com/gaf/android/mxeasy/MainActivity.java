package com.gaf.android.mxeasy;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gaf.android.mxeasy.maximoobjects.PO;
import com.gaf.android.mxeasy.utilities.MxObjectAdapter;
import com.gaf.android.mxeasy.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private EditText mSearchBoxEditorText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResults;

    private TextView mErrorMessageDisplay;

    private ProgressBar mProgressBar;

    private ListView mListView;

    private MxObjectAdapter mxObjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(LOG_TAG, "On Create() Called");

        mSearchBoxEditorText = (EditText) findViewById(R.id.mx_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.mx_url_display);

        //mSearchResults = (TextView) findViewById(R.id.mxSearchResultsJson);

        mErrorMessageDisplay = (TextView) findViewById(R.id.mxErrorMsgDisplay);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mListView = (ListView) findViewById(R.id.maximoList);

        // Create a new adapter that takes an empty list of earthquakes as input
        mxObjectAdapter = new MxObjectAdapter(this, new ArrayList<PO>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mListView.setAdapter(mxObjectAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected == R.id.action_search){
            Context context = MainActivity.this;
            String message = "Search Clicked";
            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            makeMaximoSearchQuery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeMaximoSearchQuery(){
        String poNumber = mSearchBoxEditorText.getText().toString();
        URL maximoSearchUrl = NetworkUtils.buildUrl(poNumber);
        mUrlDisplayTextView.setText(maximoSearchUrl.toString());
        new MaximoQueryTask().execute(maximoSearchUrl);
    }

    private void showJsonDataView(){
        mListView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mListView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    /**
        * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of Maximo Objects in the response.
     *
             * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
            * progress updates, so the second generic is just Void.
            *
            * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
            * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
            * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    public class MaximoQueryTask extends AsyncTask<URL, Void,  ArrayList<PO>>{

        @Override
        protected ArrayList doInBackground(URL... urls) {
            URL searchURL = urls[0];
            ArrayList<PO> maximoSearchResults = new ArrayList<>();
            Log.d(LOG_TAG, "URL " + searchURL.toString());
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            try {
                maximoSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return maximoSearchResults;
        }

    /**
     * This method runs on the main UI thread after the background work has been
     * completed. This method receives as input, the return value from the doInBackground()
     * method. First we clear out the adapter, to get rid of earthquake data from a previous
     * query to USGS. Then we update the adapter with the new list of earthquakes,
     * which will trigger the ListView to re-populate its list items.
     */
        @Override
        protected void onPostExecute(ArrayList<PO> data) {
           mProgressBar.setVisibility(View.INVISIBLE);
            // Clear the adapter of previous earthquake data
            mxObjectAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mxObjectAdapter.addAll(data);
                showJsonDataView();
            }else {
                showErrorMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
