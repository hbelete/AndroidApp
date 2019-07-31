package com.example.helinabelete.finalproject4;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class FetchCountry extends AsyncTask<String,Integer,String>{

    // Variables for the search input field, and results TextViews
    private EditText mCountryInput;
    private TextView mTitleText;
    //private TextView mAuthorText;
    private ArrayList<Countries> countries;
    private RecyclerView mRecyclerView;
    private CardViewDataAdapter mAdapter;
    private ProgressBar progressBar;


    // Class name for Log tag
    private static final String LOG_TAG = FetchCountry.class.getSimpleName();


    // Constructor providing a reference to the views in MainActivity
    public FetchCountry(TextView titleText, EditText countryInput,ProgressBar progressBar, RecyclerView mRecyclerView,CardViewDataAdapter mAdapter) {
        this.mTitleText = titleText;
        //this.mAuthorText = authorText;
        this.mCountryInput = countryInput;
        this.progressBar = progressBar;
        this.progressBar.setProgress(0);
        this.mRecyclerView = mRecyclerView;
        this.mAdapter = mAdapter;
    }


    /*
     * Makes the Books API call off of the UI thread.
     *
     * @param params String array containing the search data.
     * @return Returns the JSON string from the Books API or
     *         null if the connection failed.
     */

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setMax(100);
        progressBar.setProgress(0);
    }


    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String countryJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "http://countryapi.gear.host/v1/Country/getCountries?";

            final String QUERY_PARAM = "pName"; // Parameter for the search string.
            //final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            //final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, queryString).build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
                publishProgress();
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            countryJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return countryJSONString;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.incrementProgressBy(20);
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.INVISIBLE);
        countries = new ArrayList<Countries>();
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s); //get top level object
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("Response"); //first array of book objects

            // Initialize iterator and results fields.
            int i = 0;
            String name = null;
            //String region = null;
            //String description = null;
            //String publishedDate = null;
            //String thumbnail = null;
            //int averageRating = 0;


            //need imageLinks object
            //need saleInfo object
            //need listPrice object
            //double amount = 0;
            //String currencyCode = null;
            //String saleability = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            //loads last item found
            while (i < itemsArray.length() || (name == null)) { //authors == null &&
                // Get the current item information.
                JSONObject country = itemsArray.getJSONObject(i);
                //JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    name = country.getString("Name");
                    Countries ct = new Countries();
                    //bk.setAuthor(authors);
                    ct.setName(name);
                    ct.setRegion(country.getString("Region"));
                    ct.setCurrency(country.getString("CurrencyName"));
                    ct.setNativeName(country.getString("NativeName"));
                    ct.setLat(country.getDouble("Latitude"));
                    ct.setLon(country.getDouble("Longitude"));
                    //bk.setPublishedAt(volumeInfo.getString("publishedDate"));

                    /*if(volumeInfo.has("averageRating")) {
                        bk.setAverageRating(volumeInfo.getInt("averageRating"));
                    }
                    else
                        bk.setAverageRating(0);

                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    bk.setThumbnail(imageLinks.getString("thumbnail"));

                    JSONObject saleInfo = book.getJSONObject("saleInfo");
                    saleability = saleInfo.getString("saleability");

                    if (!saleability.equals("NOT_FOR_SALE")) {
                        JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");
                        bk.setAmount(retailPrice.getDouble("amount"));
                        bk.setCurrencyCode(retailPrice.getString("currencyCode"));
                    }
                    else {
                        bk.setAmount(0);
                        bk.setCurrencyCode("NOT_FOR_SALE");
                    }*/
                    countries.add(ct);


                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (name != null ){ //&& authors != null
                //mTitleText.setText(title);
                //mAuthorText.setText(authors);
                mCountryInput.setText("");
            } else {
                // If none are found, update the UI to show failed results.
                mCountryInput.setText("nothing found");
            }

            // create an Object for Adapter
            mAdapter = new CardViewDataAdapter(countries);

            // set the adapter object to the Recyclerview
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mCountryInput.setText("");
            e.printStackTrace();
        }
    }
}
