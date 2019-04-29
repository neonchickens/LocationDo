package com.example.locationdo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class LoginTests {
    @Rule
    public ActivityTestRule<LoginActivity> lActivityRule = new ActivityTestRule(LoginActivity.class);

    @Test
    public void useAppContext() {
        LoginActivity la = lActivityRule.getActivity();

        // Test 3 - Check initial login state
        //assertTrue(!la.attemptTime.getText().equals(""));
        //assertEquals(la.loginAttempts, 0);

        //Writes text into an edit text field
        onView(withId(R.id.enterUsername)).perform(clearText(),typeText("test"));
        onView(withId(R.id.enterPassword)).perform(clearText(),typeText("fail"));

        assertEquals(la.loginAttempts, 0);
        assertFalse(!la.attemptTime.getText().equals(""));

        onView(withId(R.id.button)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.button)).perform(click());

        // Test 4 - Check that timeout is set after 3 failed attempts
        assertEquals(la.loginAttempts, 3);
        assertFalse(!la.attemptTime.getText().equals(""));

        onView(withId(R.id.button)).perform(click());

        assertEquals(la.loginAttempts, 4);
        assertFalse(la.attemptTime.getText().equals(""));

    }
}
