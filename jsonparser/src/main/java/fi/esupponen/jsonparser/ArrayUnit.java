package fi.esupponen.jsonparser;

import java.util.LinkedList;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1129
 * @since       2018-1120
 */
public class ArrayUnit<T> implements JsonUnit {

    /**
     * The key of the unit.
     */
    String key;

    /**
     * The values of the array.
     */
    LinkedList<T> values;

    /**
     * Constructs an unit using given key and value.
     *
     * @param   key     key of the unit
     * @param   values  values of the unit
     */
    public ArrayUnit(String key, LinkedList<T> values) {
        this.key = key;
        this.values = values;
    }

    /**
     * Constructs an unit with empty array.
     *
     * @param   key     key of the unit
     */
    public ArrayUnit(String key) {
        this.key = key;
        this.values = new LinkedList<T>();
    }

    /**
     * Writes the expression of this unit to given json-file.
     * 
     * Uses leadingSpaces to have proper indentation. Prints key inside
     * quotation marks. If there is no values at all, just prints null.
     * Otherwise prints open square brackets and starts printing the
     * values. In the end closes the square brackets. If values are
     * instances of JsonFile, uses its jsonPrint()-method.
     *
     * @param   leadingSpaces   indentation
     * @param   writer          json-file   
     */
    public void jsonPrint(String leadingSpaces, PrintWriter writer) {
        if (values == null) {
            writer.print(leadingSpaces + "\"" + key + "\": null");
        } else {
            writer.print(leadingSpaces + "\"" + key + "\": [");

            if (values.size() == 0) {
                // Do nothing
            } else if (values.get(0) instanceof String) {
                for (int i = 0; i < values.size(); i++) {
                    writer.print("\"" + values.get(i) + "\"");

                    if (i != values.size()-1) {
                        writer.print(", ");
                    }
                }
            } else if (values.get(0) instanceof JsonFile) {
                writer.println();

                for (int i = 0; i < values.size(); i++) {
                    JsonFile value = (JsonFile)(values.get(i));
                    value.jsonPrint(leadingSpaces + "  ", writer);

                    if (i != values.size()-1) {
                        writer.println(", ");
                    }
                }
            } else if (values.get(0) instanceof Double) {
                DecimalFormat intFormat = new DecimalFormat("#.#");

                for (int i = 0; i < values.size(); i++) {
                    Double value = (Double) (values.get(i));

                    if (value.doubleValue() - (int)value.doubleValue() != 0) {
                        writer.print(value);
                    } else {
                        writer.print(intFormat.format(value.doubleValue()));
                    }

                    if (i != values.size()-1) {
                        writer.print(", ");
                    }
                }
            } else {
                for (int i = 0; i < values.size(); i++) {
                    writer.print(values.get(i));

                    if (i != values.size()-1) {
                        writer.print(", ");
                    }
                }
            }

            writer.print("]");
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
     * Returns the values of the unit as a LinkedList.
     *
     * @return  values in LinkedList
     */
    public LinkedList<T> getValues() {
        return values;
    }

    /**
     * Set a new key.
     *
     * @param key       new key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Set a new value.
     ^
     * @param values    new values as LinkedList
     */
    public void setValues(LinkedList<T> values) {
        this.values = values;
    }
}