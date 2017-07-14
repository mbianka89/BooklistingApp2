package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bianka Matyas on 02/07/2017.
 */

    /**
     * An {@link BooklistingAdapter} knows how to create a list item layout for each book
     * in the data source (a list of {@link Booklisting} objects).
     *
     * These list item layouts will be provided to an adapter view like ListView
     * to be displayed to the user.
     */

    public class BooklistingAdapter extends ArrayAdapter<Booklisting> {

        /**
         * Constructs a new {@link BooklistingAdapter}.
         *
         * @param context of the app
         * @param booklistings is the list of books, which is the data source of the adapter
         */
        public BooklistingAdapter(Context context, List<Booklisting> booklistings) {
            super(context, 0, booklistings);
        }

        /**
         * Returns a list item view that displays information about the book at the given position
         * in the list of books.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if there is an existing list item view (called convertView) that we can reuse,
            // otherwise, if convertView is null, then inflate a new list item layout.
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.booklisting_list_item, parent, false);
            }

            // Find the book at the given position in the list of booklistings
            Booklisting currentBooklisting = getItem(position);

            // Find the TextView with view ID author
            TextView authorView = (TextView) listItemView.findViewById(R.id.author_list_item);
            // Display the author of the current book in that TextView
            authorView.setText(currentBooklisting.getmAuthor());

            // Find the TextView with view ID title
            TextView titleView = (TextView) listItemView.findViewById(R.id.title_list_item);
            // Display the title of the current book in that TextView
            titleView.setText(currentBooklisting.getmTitle());


            // Return the list item view that is now showing the appropriate data
            return listItemView;
        }
}
