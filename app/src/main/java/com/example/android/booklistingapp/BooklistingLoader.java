package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

/**
 * Created by Bianka Matyas on 02/07/2017.
 */

    /**
     * Loads a list of books by using an AsyncTask to perform the
     * network request to the given URL.
     */
    public class BooklistingLoader extends AsyncTaskLoader<List<Booklisting>> {

        /** Tag for log messages */
        private static final String LOG_TAG = BooklistingLoader.class.getName();

        /** Query URL */
        private String mUrl;

        /**
         * Constructs a new {@link BooklistingLoader}.
         *
         * @param context of the activity
         * @param url to load data from
         */
        public BooklistingLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Booklisting> loadInBackground() {
            if (mUrl == null) {
                return null;
            }

            // Perform the network request, parse the response, and extract a list of booklists.
            List<Booklisting> booklistings = QueryUtils.fetchBooklistingData(mUrl);
            return booklistings;
        }
    }


