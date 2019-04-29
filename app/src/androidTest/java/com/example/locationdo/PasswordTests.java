package com.example.locationdo;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class PasswordTests {
    @Rule
    public ActivityTestRule<Register> rActivityRule = new ActivityTestRule(Register.class);

    @Test
    @UiThreadTest
    public void useAppContext() {
        String goodPW = "!testG00Dpw";
        String badPW = "fail";

        Register ra = rActivityRule.getActivity();

        // Test 1 - check password length function
        assertTrue(ra.validLength(goodPW));
        assertFalse(ra.validLength(badPW));

        // Test 2 - check password required characters function
        assertTrue(ra.hasVariedChar(goodPW));
        assertFalse(ra.hasVariedChar(badPW));

        // Test 3 - check combined checkPassword function
        assertTrue(ra.checkPassword(goodPW));
        assertFalse(ra.checkPassword(badPW));
    }
}
