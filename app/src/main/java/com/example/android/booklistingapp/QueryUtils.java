package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Bianka Matyas on 02/07/2017.
 */


/**
 * Helper methods related to requesting and receiving booklisting data from Google Books API.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String UNKNOWN_AUTHOR = "Unknown Author";
    private static final String AUTHOR_SEPARATOR = ", ";
    private static final String JSON_KEY_BOOK_AUTHORS = "authors";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google Books API dataset and return a list of {@link Booklisting} objects.
     */
    public static List<Booklisting> fetchBooklistingData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Booklisting}s
        List<Booklisting> booklistings = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Booklisting}s
        return booklistings;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the booklist JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Booklisting} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Booklisting> extractFeatureFromJson(String booklistingJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(booklistingJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding booklistings to
        List<Booklisting> booklistings = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(booklistingJSON);

            if (!baseJsonResponse.has("items")) {
                return null;
            }
            JSONArray booklistingArray = baseJsonResponse.getJSONArray("items");

            // For each book in the booklistingArray, create an {@link Booklisting} object
            for (int i = 0; i < booklistingArray.length(); i++) {

                // Get a single booklisting at position i within the list of booklistings
                JSONObject currentBooklisting = booklistingArray.getJSONObject(i);

                // For a given booklisting, extract the JSONObject associated with the
                // key called "items", which represents a list of all items
                // for that booklisting.
                JSONObject items = currentBooklisting.getJSONObject("volumeInfo");


                // Extract the value for the key called "author"...

                JSONArray authorArray = items.optJSONArray(JSON_KEY_BOOK_AUTHORS);
                ArrayList<String> authors = new ArrayList<>();

                if (null != authorArray && authorArray.length() != 0) {
                    for (int j = 0; j < authorArray.length(); j++) {
                        try {
                            authors.add(authorArray.getString(j));
                        } catch (JSONException e) {
                            authors.add(UNKNOWN_AUTHOR);
                        }
                    }
                } else {
                    authors.add(UNKNOWN_AUTHOR);
                }

                String finalAuthorsString = TextUtils.join(AUTHOR_SEPARATOR, authors);


                // Extract the value for the key called "title"
                String title = items.getString("title");

                // Create a new {@link Booklisting} object with the author and title
                //  from the JSON response.
                Booklisting booklisting = new Booklisting(finalAuthorsString, title);

                // Add the new {@link Booklisting} to the list of booklistings.
                booklistings.add(booklisting);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the booklist JSON results", e);
        }

        // Return the list of booklist
        return booklistings;
    }
}
