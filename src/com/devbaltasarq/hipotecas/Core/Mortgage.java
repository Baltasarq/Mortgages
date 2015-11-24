package com.devbaltasarq.hipotecas.Core;

/**
 * Represents mortgages with a simulation of the value for each pay (by month)
 * following the french model.
 * Created by baltasarq on 23/11/15.
 */
public class Mortgage {
    public static final int TYPICAL_YEARS = 25;
    public static final double TYPICAL_AMOUNT = 120000;

    private double percentage;
    private String name;

    /** Creates a new mortgage
     * @param name the name of the bank
     * @param percentage the annual interest the bank charges */
    public Mortgage(String name, double percentage)
    {
        this.name = name;
        this.percentage = percentage;
    }

    /** @return The percentage this bank annually applies to the mortgage */
    public double getPercentage() {
        return this.percentage;
    }

    /** Simulates the mortgage cost for each month, given the total amount
     *  and the number of months. Following the french model.
     * @param numMonths the number of months, as an integer
     * @param amount the total amount of the mortgage, as a double
     * @return the cost per month, as a double
     */
    public double simulateMonths(int numMonths, double amount)
    {
        double toret = amount * ( this.getPercentage() / 12 );
        double factor = Math.pow(  1 + ( this.getPercentage() / 100 ), -numMonths );
        double divider = 100 * ( 1 - factor  );

        return ( toret / divider );
    }

    /** Simulates the mortgage cost for each month, given the total amount
     *  and the number of months. Following the french model.
     * @param numYears the number of years, as an integer
     * @param amount the total amount of the mortgage
     * @return the cost per month, as a double
     */
    public double simulateYears(int numYears, double amount)
    {
        return this.simulateMonths( numYears * 12, amount  );
    }

    /** @return a simulation with the typical years and typical amount */
    public double simulate() {
        return this.simulateYears( TYPICAL_YEARS, TYPICAL_AMOUNT );
    }

    /** @return the name of the bank */
    public String getName() {
        return this.name;
    }

    /** @return the description of the bank, the percentage, and a simulation for a typical mortgage */
    @Override
    public String toString() {
        return String.format(
                "%s (%05.2f): %06.2f",
                this.getName(),
                this.getPercentage(),
                this.simulate() );
    }
}
