package io.github.personalprism.personalprism_droid;

import android.content.Intent;
import android.test.*;
import android.widget.EditText;
import junit.framework.*;

public class MainUIScreenTest extends TestCase {

	private MainUIScreen screen;
	protected void setUp() {
		screen = new MainUIScreen();
	}

	public void testOnResume() {
		fail("Not yet implemented");
	}

	public void testOnCreateBundle() {
		fail("Not yet implemented");
	}

	public void testOnCreateOptionsMenuMenu() {
		fail("Not yet implemented");
	}

	public void testDateStartClick() {
		EditText text = (EditText) screen.findViewById(R.id.editDateStart);
		text.performClick();
		assertEquals("mm/dd/yyyy", text.getText());
		
		
	}

	public void testDateStopClick() {
		fail("Not yet implemented");
	}

	public void testTimeClickStart() {
		fail("Not yet implemented");
	}

	public void testTimeClickStop() {
		fail("Not yet implemented");
	}

	public void testGetMap() {
		fail("Not yet implemented");
	}

	public void testGetHistory() {
		fail("Not yet implemented");
	}

	public void testCollectDataToggle() {
		fail("Not yet implemented");
	}

}
