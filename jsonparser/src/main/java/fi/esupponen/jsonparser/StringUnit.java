package fi.esupponen.jsonparser;

import java.io.PrintWriter;

/**
 * @author      Essi Supponen <essi.supponen@cs.tamk.fi>
 * @version     2018-1129
 * @since       2018-1120
 */
public class StringUnit implements JsonUnit {

    /**
     * The key of the unit.
     */
    String key;

    /**
     * The value of the unit.
     */
    String value;

    /**
     * Constructs an unit using given key and value.
     *
     * @param   key     key of the unit
     * @param   value   value of the unit
     */
    public StringUnit(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Prints the key and the value to the console.
     */
    public void jsonPrint() {
        System.out.print("\"" + key + "\": \"" + value + "\"");
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
        writer.print(leadingSpaces + "\"" + key + "\": \"" + value + "\"");
    }

    /**
     * Returns a string representation of the object.
     *
     * @return  string representation of object
     */
    public String toString() {
        return key + " : " + value;
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
    public String getValue() {
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
    public void setValue(String value) {
        this.value = value;
    }
}