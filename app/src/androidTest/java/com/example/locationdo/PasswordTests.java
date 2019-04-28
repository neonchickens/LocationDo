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
public class PasswordTests {
    @Rule
    public ActivityTestRule<Register> rActivityRule = new ActivityTestRule(Register.class);

    Register testRegister;

    @Before
    public void setUp() throws Exception{
        rActivityRule.launchActivity(null);
        testRegister = rActivityRule.getActivity();
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Register testRegister = new Register();

        EditText et = new EditText(appContext);

        // check acceptable password
        et.setText("password1");
        assertTrue(testRegister.checkPassword(et));

        // check too-short password
        et.setText("pass1");
        assertFalse(testRegister.checkPassword(et));
    }
}
