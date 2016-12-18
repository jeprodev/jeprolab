package com.jeprolab.models;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabWarehouseModel {
    /** @var int identifier of the warehouse */
    public int warehouse_id;

    /** @var int Id of the address associated to the warehouse */
    public int address_id;

    /** @var string Reference of the warehouse */
    public String reference;

    /** @var string Name of the warehouse */
    public String name;

    /** @var int Id of the employee who manages the warehouse */
    public int employee_id;

    /** @var int Id of the valuation currency of the warehouse */
    public int currency_id;

    /** @var bool True if warehouse has been deleted (hence, no deletion in DB) */
    public boolean deleted = false;

    /**
     * Describes the way a Warehouse is managed
     *
     * @var string enum WA|LIFO|FIFO
     */
    public String management_type;
}
