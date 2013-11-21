package io.github.personalprism.personalprism_droid;

import com.db4o.ext.StoredClass;
import android.location.Location;
import com.db4o.ObjectContainer;
import com.db4o.Db4oEmbedded;
import android.widget.TextView;
import java.util.Observable;
import java.util.Observer;
import android.util.Log;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainUIScreen
    extends Activity
// implements Observer
{

    TextView              text;
    private SourceManager sourceManager;
    public static String DB4OFILENAME;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        DB4OFILENAME = getApplicationContext().getDir("prism_db4o", 0) + "/PersonalPrism.db4o";
        Log.d(getClass().getSimpleName(), "starting main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uiscreen);

        text = (TextView)findViewById(R.id.text); //in case we want to output

        sourceManager = new SourceManager(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_uiscreen, menu);
        return true;
    }



}
