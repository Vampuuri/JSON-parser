package fi.esupponen.jsonparser;

import java.text.DecimalFormat;
import java.io.PrintWriter;

/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1128
 * @since       2018-1120
 */
public class NumberUnit implements JsonUnit {

    /**
     * The key of the unit.
     */
    String key;

    /**
     * The value of the unit.
     */
    double value;

    /**
     * Format for printing integers.
     */
    DecimalFormat intFormat;

    /**
     * Constructs an unit using given key and value.
     *
     * @param   key     key of the unit
     * @param   value   value of the unit
     */
    public NumberUnit(String key, double value) {
        this.key = key;
        this.value = value;
        intFormat = new DecimalFormat("#.#");
    }

    /**
     * Prints the key and the value to the console.
     */
    public void jsonPrint() {
        if (value - (int)value != 0) {
            System.out.print("\"" + key + "\": " + value);
        } else {
            System.out.print("\"" + key + "\": " );
            System.out.print(intFormat.format(value));
        }
    }

    /**
     * Writes the expression of this unit to given json-file.
     * 
     * Uses leadingSpaces to have proper indentation. Prints key inside
     * quotation marks and then value.
     *
     * @param   leadingSpaces   indentation
     * @param   writer          json-file   
     */
    public void jsonPrint(String leadingSpaces, PrintWriter writer) {
        writer.print(leadingSpaces);

        if (value - (int)value != 0) {
            writer.print("\"" + key + "\": " + value);
        } else {
            writer.print("\"" + key + "\": " );
            writer.print(intFormat.format(value));
        }
    }

    /**
     * Returns the key of the unit.
     *
     * @return  key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the value of the unit.
     *
     * @return  value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets a new key.
     *
     * @param key       new key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Sets a new value.
     ^
     * @param value     new value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Sets a new value.
     ^
     * @param value     new value
     */
    public void setValue(int value) {
        this.value = (double)value;
    }
}