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
        Register ra = rActivityRule.getActivity();

        // Test 1 - check acceptable password
        assertTrue(ra.checkPassword("!testG00Dpw"));

        // Test 2 - check too-short password
        assertFalse(ra.checkPassword("fail"));
    }
}
