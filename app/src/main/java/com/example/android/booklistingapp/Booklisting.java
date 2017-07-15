package com.example.android.booklistingapp;

/**
 * Created by Bianka Matyas on 02/07/2017.
 */

/**
 * An {@link Booklisting} object contains information related to a single book.
 */

public class Booklisting {

    /**
     * Author of the book
     */
    private String mAuthor;

    /**
     * Title of the book
     */
    private String mTitle;


    /**
     * Constructs a new {@link Booklisting} object.
     *
     * @param author is the author of the book
     * @param title  of the book
     */
    public Booklisting(String author, String title) {
        mAuthor = author;
        mTitle = title;
    }

    /**
     * Returns the author of the book.
     */
    public String getmAuthor() {
        return mAuthor;
    }


    /**
     * Returns the title of the book.
     */
    public String getmTitle() {
        return mTitle;
    }
}
