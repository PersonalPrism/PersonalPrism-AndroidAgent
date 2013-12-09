package io.github.personalprism.personalprism_droid;

import android.app.Service;
import android.app.IntentService;
import android.content.ContextWrapper;
import android.test.mock.MockApplication;
import android.test.ServiceTestCase;
/**
 *  Attempt at testing DbHandler.
 *  @param <T>
 * 
 *  @author Hunter Morgan (kp1108)
 *  @version 2013.12.8
 */
public class DbHandlerTest<T extends Service>
    extends ServiceTestCase<T>
{


    private Class<T> DbHandler;

    // ----------------------------------------------------------
    /**
     * Create a new DbHandlerTest object.
     * @param DbHandler the instance of DbHandler to test.
     */
    public DbHandlerTest(Class<T> DbHandler) {
        super(DbHandler);
        this.DbHandler = DbHandler;
    }


    private MockApplication mMockApplication;
    private DbHandler mService;

    public void setUp()
    {
        //        this.setContext(getContext());
        //this.setContext(new ContextWrapper(base));
        //        mService = new DbHandler();
        //        mService.set
    }

    /**
     * Tests if the DB can open and close successfully.
     */
    public void testDbOpenClose()
    {
        mService.onCreate();
    //    AssertEquals(0, mService.);
        mService.onDestroy();
    }

}
