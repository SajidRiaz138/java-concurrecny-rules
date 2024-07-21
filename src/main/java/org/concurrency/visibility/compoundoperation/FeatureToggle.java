package org.concurrency.visibility.compoundoperation;

public interface FeatureToggle
{
    /**
     * Toggles the feature state.
     */
    void toggle();

    /**
     * Gets the current feature state.
     *
     * @return true if the feature is enabled, false otherwise.
     */
    boolean isFeatureEnabled();
}
