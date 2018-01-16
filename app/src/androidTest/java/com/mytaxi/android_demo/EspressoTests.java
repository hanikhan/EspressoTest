package com.mytaxi.android_demo;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.mytaxi.android_demo.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Hani on 15/01/18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTests {
    private boolean isLoggedIn= false;
    private MainActivity mActivity;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void init(){
        try{
            onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        }
        catch (Exception e){
            isLoggedIn=true;
        }
        if(!isLoggedIn){
            onView(withId(R.id.edt_username)).perform(typeText("whiteelephant261"));
            onView(withId(R.id.edt_password)).perform(typeText("video"));
            snooze(2);
            onView(withId(R.id.btn_login)).perform(click());
            waitForElementVisible(R.id.textSearch, 10);
            onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkforVisibility() {
        //wait for 10 seconds for availability of element
        waitForElementVisible(R.id.toolbar, 10);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        waitForElementVisible(R.id.textSearch, 10);
        onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
    }

    @Test
     public void lowerCaseSearch(){
        waitForElementVisible(R.id.toolbar,10);
        onView(withId(R.id.textSearch)).perform(typeText("s"));
        snooze(2);
        onView(withId(R.id.textSearch)).perform(typeText("a"));
        snooze(2);
        waitForElementVisible(R.id.searchContainer,10);
        onView(withId(R.id.searchContainer)).check(matches(isDisplayed()));
        //verify count of elements returned
        onView(withId(R.id.textSearch)).check(new AutoCompleteTextViewItemCountAssertion(6));
        mActivity = mActivityRule.getActivity();
        clickSearchItem("Sarah Friedrich",mActivity,10);
        waitForElementVisible(R.id.fab,10);
        String driverName = getText(withId(R.id.textViewDriverName));
        Assert.assertTrue(driverName.trim().equals("Sarah Friedrich"));
        onView(withId(R.id.fab)).perform(click());
    }

    @Test
    public void upperCaseSearch(){
        waitForElementVisible(R.id.toolbar,10);
        onView(withId(R.id.textSearch)).perform(typeText("S"));
        snooze(2);
        onView(withId(R.id.textSearch)).perform(typeText("A"));
        snooze(2);
        waitForElementVisible(R.id.searchContainer,10);
        onView(withId(R.id.searchContainer)).check(matches(isDisplayed()));

        //verify count of elements returned
        onView(withId(R.id.textSearch)).check(new AutoCompleteTextViewItemCountAssertion(6));
        mActivity = mActivityRule.getActivity();
        clickSearchItem("Sarah Friedrich",mActivity,10);
        waitForElementVisible(R.id.textViewDriverName,10);
        waitForElementVisible(R.id.fab,10);
        String driverName = getText(withId(R.id.textViewDriverName));
        Assert.assertTrue(driverName.trim().equals("Sarah Friedrich"));
        onView(withId(R.id.fab)).perform(click());
    }

    @Test
    public void verifyLogout(){
        onView(withId(R.id.drawer_layout)).perform(open());
        snooze(5);
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        waitForElementVisible(R.id.edt_username,10);
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
    }

    public static void waitForElementVisible(int idToLocate,int time){
        for(int i=0;i<time;i++){
            try{
                onView(withId(idToLocate)).check(matches(isDisplayed()));
                break;
            }
            catch (Exception e){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    public static void clickSearchItem(String searchTerm,MainActivity mActivity,int time){
        Boolean actionPerformed=false;
        for(int i=0;i<time;i++){
            try{
                onView(withText(searchTerm))
                        .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                        .perform(click());
                actionPerformed = true;
                break;
            }
            catch (Exception e){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        Assert.assertTrue(actionPerformed);
    }

    public static void snooze(int secs){
        for(int i=0;i<secs;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
            }
        }
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
}

