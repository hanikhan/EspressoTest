package com.mytaxi.android_demo;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;
import android.widget.AutoCompleteTextView;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Hani on 15/01/18.
 */

public class AutoCompleteTextViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    public AutoCompleteTextViewItemCountAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }
        AutoCompleteTextView tv = (AutoCompleteTextView) view;
        //Adding a temp workaround to wait for 2 secs before asserting count of items returned
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(tv.getAdapter().getCount(), is(expectedCount));
    }
}