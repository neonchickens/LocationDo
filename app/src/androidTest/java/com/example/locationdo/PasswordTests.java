package com.example.locationdo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.support.test.*;



import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class PasswordTests {
    Register testRegister;

    @Rule
    public ActivityTestRule<Register> rActivityRule = new ActivityTestRule(Register.class);

    @Before
    public void createRegister(){
        rActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void useAppContext() {
        testRegister = rActivityRule.getActivity();

        // Test 1 - check acceptable password
        onView(withId(R.id.enterPassword)).perform(clearText(),typeText("long1fail"));
        //assertTrue(testRegister.checkPassword());

        // Test 2 - check too-short password
        onView(withId(R.id.enterPassword)).perform(clearText(),typeText("fail"));
        //assertFalse(testRegister.checkPassword());
    }
}
