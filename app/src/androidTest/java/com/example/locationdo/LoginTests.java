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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class LoginTests {
    @Rule
    public ActivityTestRule<LoginActivity> lActivityRule = new ActivityTestRule(LoginActivity.class);

    LoginActivity testLogin;

    @Before
    public void setUp() throws Exception{
        lActivityRule.launchActivity(null);
        testLogin = lActivityRule.getActivity();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        LoginActivity testLogin = new LoginActivity();

        testLogin.username.setText("bob");
        testLogin.password.setText("failtest");

        // Test 3 - Check initial login state
        assertTrue(!testLogin.attemptTime.getText().equals(""));
        assertEquals(testLogin.loginAttempts, 0);

        testLogin.transition();
        testLogin.transition();

        // Test 4 - Check that timeout is set after 3 failed attempts
        assertEquals(testLogin.loginAttempts, 3);
        assertFalse(!testLogin.attemptTime.getText().equals(""));
    }
}
