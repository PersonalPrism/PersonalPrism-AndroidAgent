package io.github.personalprism.personalprism_droid;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.test.*;
import android.widget.EditText;
import junit.framework.*;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author Stuart Harvey (stu)
 *  @version 2013.12.8
 */
public class MainUIScreenTest extends student.AndroidTestCase<MainUIScreen> {

    private MainUIScreen screen;
    
    private Button historyButton;
    private Button dateRangeButton;
    private EditText editDateStop;
    private EditText editDateStart;
    
    /**
     * Sets up a new anroid test case for MainUIScreen.
     */
	public MainUIScreenTest()
    {
        super(MainUIScreen.class);
    }

	/**
	 * Sets up MainUIScreen object for testing, not necessary yet.
	 */
	protected void setUp() {
	    screen = new MainUIScreen();
	    Bundle bundle = new Bundle(); // typically null except for a rare case
        screen.onCreate(bundle);
	}

	/**
	 * Tests the onResume results by calling onResume and asserting that there
	 * are no errors thrown.
	 */
	public void testOnResume() {
		Exception thrown = null;
	    try
		{
		    screen.onResume();
		}
	    catch (Exception e)
	    {
	        // will be called if google play services is not available.
	        thrown = e;
	        e.printStackTrace();
	    }
	    assertNull(thrown);
	}

	/**
	 * Tests that all buttons, editTexts, and 
	 */

}
