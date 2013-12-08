package io.github.personalprism.personalprism_droid;

/**
 * The Interface PrismDataSource. Eventually, this project will have more than
 * Location data sources. They should all conform to this interface for
 * commonality.
 * @author Hunter Morgan <kp1108> <automaticgiant@gmail.com>
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

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled();
}
