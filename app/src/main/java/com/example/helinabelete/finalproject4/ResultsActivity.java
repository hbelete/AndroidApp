package com.example.helinabelete.finalproject4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private EditText mCountryInput;
    private TextView mTitleText;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private CardViewDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Initialize all the view variables.
        mCountryInput = (EditText) findViewById(R.id.countryInput);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    /**
     * Gets called when the user pushes the "Search Country" button
     *
     * @param view The view (Button) that was clicked.
     */
    public void searchCountry(View view) {
        // Get the search string from the input field.
        String queryString = mCountryInput.getText().toString();

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (TextUtils.isEmpty(queryString)) {
            Toast.makeText(ResultsActivity.this, "Enter a Country!", Toast.LENGTH_SHORT).show();
        }

        //if (queryString != mTitleText.getText().toString()) {
        //    Toast.makeText(ResultsActivity.this, "Does Not Exist", Toast.LENGTH_SHORT).show();}

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            new FetchCountry(mTitleText, mCountryInput, progressBar, mRecyclerView, mAdapter).execute(queryString);
        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
                //mBookInput.setText(R.string.no_search_term);
                mCountryInput.setHint(R.string.no_search_term);
                //a cheap way to clear the RecyclerView
                mRecyclerView.setAdapter(null);

            } else {
                mCountryInput.setText(R.string.no_network);
            }
        }

    }
}

