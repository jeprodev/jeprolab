package com.jeprolab.models.tax;

import com.jeprolab.models.JeproLabTaxModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by jeprodev on 04/02/14.
 */
public class JeproLabTaxCalculator {
    /**
     * COMBINE_METHOD sum taxes
     * eg: 100€ * (10% + 15%)
     */
    private static int COMBINE_METHOD = 1;

    /**
     * ONE_AFTER_ANOTHER_METHOD apply taxes one after another
     * eg: (100€ * 10%) * 15%
     */
    private static int ONE_AFTER_ANOTHER_METHOD = 2;

    /**
     * @var array $taxes
     */
    public List<JeproLabTaxModel> taxes;

    /**
     * @var int $computation_method (COMBINE_METHOD | ONE_AFTER_ANOTHER_METHOD)
     */
    public int computation_method;


    public JeproLabTaxCalculator(){
        this(new ArrayList<>());
    }

    public JeproLabTaxCalculator(List taxes){
        this(taxes, JeproLabTaxCalculator.COMBINE_METHOD);
    }
    /**
     * @param taxes
     * @param computationMethod (COMBINE_METHOD | ONE_AFTER_ANOTHER_METHOD)
     */
    public JeproLabTaxCalculator(List taxes, int computationMethod){
        /* / sanity check
        for(JeproLabTaxModel tax : taxes) {
            if (!(tax instanceof JeproLabTaxModel)) {
                throw new Exception('Invalid Tax Object');
            }
        }*/

        this.taxes = taxes;
        this.computation_method = computationMethod;
    }

    /**
     * Compute and add the taxes to the specified price
     *
     * @param priceTaxExcluded price tax excluded
     * @return float price with taxes
     */
    public float addTaxes(float priceTaxExcluded){
        return priceTaxExcluded * (1 + (this.getTotalRate() / 100));
    }


    /**
     * Compute and remove the taxes to the specified price
     *
     * @param priceTaxIncluded price tax inclusive
     * @return float price without taxes
     */
    public float removeTaxes(float priceTaxIncluded){
        return priceTaxIncluded / (1 + this.getTotalRate() / 100);
    }

    /**
     * @return taxes total taxes rate
     */
    public float getTotalRate(){
        float taxes = 0;
        if (this.computation_method == JeproLabTaxCalculator.ONE_AFTER_ANOTHER_METHOD) {
            taxes = 1;
            for(JeproLabTaxModel tax : this.taxes) {
                taxes *= (1 + (Math.abs(tax.rate) / 100));
            }

            taxes = taxes - 1;
            taxes = taxes * 100;
        } else {
            for(JeproLabTaxModel tax : this.taxes) {
                taxes += Math.abs(tax.rate);
            }
        }

        return taxes;
    }
/*
    public function getTaxesName()
    {
        $name = '';
        foreach ($this->taxes as $tax) {
        $name .= $tax->name[(int)Context::getContext()->language->id].' - ';
    }

        $name = rtrim($name, ' - ');

        return $name;
    }

    /**
     * Return the tax amount associated to each taxes of the TaxCalculator
     *
     * @param float $price_te
     * @return array $taxes_amount
     * /
    public function getTaxesAmount($price_te)
    {
        $taxes_amounts = array();

        foreach ($this->taxes as $tax) {
        if ($this->computation_method == TaxCalculator::ONE_AFTER_ANOTHER_METHOD) {
            $taxes_amounts[$tax->id] = $price_te * (abs($tax->rate) / 100);
            $price_te = $price_te + $taxes_amounts[$tax->id];
        } else {
            $taxes_amounts[$tax->id] = ($price_te * (abs($tax->rate) / 100));
        }
    }

        return $taxes_amounts;
    }

    /**
     * Return the total taxes amount
     *
     * @param float $price_te
     * @return float $amount
     * /
    public function getTaxesTotalAmount($price_te)
    {
        $amount = 0;

        $taxes = $this->getTaxesAmount($price_te);
        foreach ($taxes as $tax) {
        $amount += $tax;
    }

        return $amount;
    } */
}
