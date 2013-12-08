package sohail.aziz.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * The Class MyResultReceiver. This was an implementation of Android
 * ResultReceiver I stole because I couldn't grok it. It's so minimal, there's
 * not really any rewriting it, so I take and give credit. Courtesy of:
 * http://sohailaziz.com/2012/05/intentservice-providing-data-back-to.html
 * @author sohail aziz
 * @version 2012/04/22
 */
public class MyResultReceiver
    extends ResultReceiver
{

    private Receiver mReceiver;


    public MyResultReceiver(Handler handler)
    {
        super(handler);
        // TODO Auto-generated constructor stub
    }


    public interface Receiver
    {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }


    public void setReceiver(Receiver receiver)
    {
        mReceiver = receiver;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData)
    {

        if (mReceiver != null)
        {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
