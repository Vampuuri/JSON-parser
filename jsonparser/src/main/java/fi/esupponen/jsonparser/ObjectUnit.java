package fi.esupponen.jsonparser;

import java.util.LinkedList;
import java.io.PrintWriter;

/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1217
 * @since       2018-1120
 */
public class ObjectUnit implements JsonUnit {

    /**
     * The key of the unit.
     */
    String key;

    /**
     * The value of the unit.
     */
    LinkedList<JsonUnit> values;

    /**
     * Constructs an unit using given key and value.
     *
     * @param   key     key of the unit
     * @param   values  values of the unit
     */
    public ObjectUnit(String key, LinkedList<JsonUnit> values) {
        this.key = key;
        this.values = new LinkedList<>();

        if (values == null) {
            this.values = null;
        } else  {
            for (JsonUnit value : values) {
                if (alreadyUsed(value.getKey())) {
                    throw new RuntimeException("Can't set a list with multiple same keys.");
                } else {
                    this.values.add(value);
                }
            }
        }
    }

    /**
     * Is an unit with certain key already used.
     *
     * @param   key     key to be found
     * @return  already used
     */
    public boolean alreadyUsed(String key) {
        boolean alreadyUsed = false;

        for (JsonUnit value : values) {
            if (value.getKey().equals(key)) {
                alreadyUsed = true;
                break;
            }
        }

        return alreadyUsed;
    }

    /**
     * Adds new unit to values.
     *
     * Checks if unit is instance of JsonFile or if it's key is already used.
     * If neither of them is true, adds unit to the list.
     *
     * @param   unit    to be added
     */
    public void add(JsonUnit unit) {
        if (values == null) {
            values = new LinkedList<>();
        }

        if (unit instanceof JsonFile) {
            throw new RuntimeException("JsonFiles can be added to ObjectUnit only in ArrayUnit.");
        } else {
            if (alreadyUsed(unit.getKey())) {
                throw new RuntimeException("Can't add new unit with already existing key.");
            } else {
                values.add(unit);
            }
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
        writer.print(leadingSpaces + "\"" + key + "\": ");

        if (values == null) {
            writer.print("null");
        } else {
            writer.println("{");
            for (int i = 0; i < values.size(); i++) {
                values.get(i).jsonPrint(leadingSpaces + "  ", writer);

                if (i < values.size() - 1) {
                    writer.println(",");
                }
            }
            writer.println();
            writer.print(leadingSpaces + "}");
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
     * @return  values
     */
    public LinkedList<JsonUnit> getValues() {
        return values;
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
     * @param values    new value
     */
    public void setValue(LinkedList<JsonUnit> values) {
        if (values == null) {
            this.values = null;
        } else {
            this.values = new LinkedList<JsonUnit>();

            for (JsonUnit value : values) {
                if (alreadyUsed(value.getKey())) {
                    throw new RuntimeException("Can't set a list with multiple same keys.");
                } else {
                    this.values.add(value);
                }
            }
        }
    }

    /**
     * Returns a string representation of the object.
     *
     * @return  string representation of object
     */
    @Override
    public String toString() {
        String str = "\"" + key + "\": ";

        if (values == null) {
            str += "null";
        } else {
            str += "{";
            for (int i = 0; i < values.size(); i++) {
                str += values.get(i).toString();

                if (i < values.size() - 1) {
                    str += ", ";
                }
            }
            str += "}";
        }

        return str;
    }
}