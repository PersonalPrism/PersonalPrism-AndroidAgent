package io.github.personalprism.personalprism_droid;

/**
 * The Interface PrismDataSource. Eventually, this project will have more than
 * Location data sources. They should all conform to this interface for
 * commonality.
 */
public interface PrismDataSource
{

    /**
     * Start. Starts updates.
     */
    public void start();


    /**
     * Stop. Stops updates.
     */
    public void stop();


    /**
     * Restart. Suggested to be used to reconfigure.
     */
    public void restart();
}
