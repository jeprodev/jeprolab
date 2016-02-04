package com.jeprolab.models.tax;

import com.jeprolab.models.JeproLabAddressModel;

/**
 *
 * Created by jeprodev on 04/02/14.
 */
public interface JeproLabTaxManagerInterface {
    /**
     * This method determine if the tax manager is available for the specified address.
     *
     * @param address
     * @return bool
     */
    default boolean isAvailableForThisAddress(JeproLabAddressModel address) {
        return false;
    }

    /**
     * Return the tax calculator associated to this address
     *
     * @return JeproLabTaxCalculator
     */
    JeproLabTaxCalculator getTaxCalculator();
}
