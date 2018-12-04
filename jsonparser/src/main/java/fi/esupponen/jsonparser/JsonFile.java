package fi.esupponen.jsonparser;

import java.util.LinkedList;
import java.io.PrintWriter;

/**
 * @author      Essi Supponen <essi.supponen@cs.tamk.fi>
 * @version     2018-1128
 * @since       2018-1120
 */
public class JsonFile implements JsonUnit {

    /**
     * Name for the file that will be parsed.
     */
    String name;

    /**
     * Units of the file.
     */
    LinkedList<JsonUnit> units;

    /**
     * Constructs an object with given name.
     *
     * @param   name    name for the file that will be parsed
     */
    public JsonFile(String name) {
        this.name = name;
        this.units = new LinkedList<>();
    }

    /**
     * Is an unit with certain key already used.
     *
     * @return  already used
     */
    public boolean alreadyUsed(String key) {
        boolean alreadyUsed = false;

        for (JsonUnit unit : units) {
            if (unit.getKey().equals(key)) {
                alreadyUsed = true;
                break;
            }
        }

        return alreadyUsed;
    }

    /**
     * Adds given elements to units.
     *
     * Checks if element is instance of JsonFile or if it's key is already used.
     * If neither of them is true, adds element to the list.
     *
     * @param   elements    elements to be added
     */
    public void add(JsonUnit... elements) {
        for (JsonUnit element : elements) {
            if (element instanceof JsonFile) {
                throw new RuntimeException("JsonFiles can be added to JsonFile's units only inside ArrayUnit.");
            } else {
                if (!alreadyUsed(element.getKey())) {
                    units.add(element);
                } else {
                    throw new RuntimeException("Can't add new unit with already existing key.");
                }
            }
        }
    }

    /**
     * Adds a new StringUnit to units according to given parameters.
     *
     * @param   key     key for new unit
     * @param   value   value for new unit
     */
    public void add(String key, String value) {
        if (!alreadyUsed(key)) {
            units.add(new StringUnit(key, value));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new BooleanUnit to units according to given parameters.
     *
     * @param   key     key for new unit
     * @param   value   value for new unit
     */
    public void add(String key, boolean value) {
        if (!alreadyUsed(key)) {
            units.add(new BooleanUnit(key, value));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new NumberUnit to units according to given parameters.
     *
     * @param   key     key for new unit
     * @param   value   value for new unit
     */
    public void add(String key, int value) {
        if (!alreadyUsed(key)) {
            units.add(new NumberUnit(key, value));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new NumberUnit to units according to given parameters.
     *
     * @param   key     key for new unit
     * @param   value   value for new unit
     */
    public void add(String key, double value) {
        if (!alreadyUsed(key)) {
            units.add(new NumberUnit(key, value));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new ObjectUnit to units according to given parameters.
     *
     * @param   key     key for new unit
     * @param   values  values for new unit
     */
    public void add(String key, LinkedList<JsonUnit> values) {
        if (!alreadyUsed(key)) {
            units.add(new ObjectUnit(key, values));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new ArrayUnit to units according to given parameters.
     *
     * Adds a new ArrayUnit to units using given key and values. This is the
     * only way to add empty ArrayUnit to units.
     *
     * @param   key     key for new unit
     * @param   values  values for new unit
     */
    public void addArray(String key, LinkedList<? extends Object> values) {
        if (!alreadyUsed(key)) {
            units.add(new ArrayUnit(key, values));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new ArrayUnit to units according to given parameters.
     *
     * Creates new LinkedList of Strings. Adds all the given values to the
     * list. Adds new ArrayUnit to units using given key and created list.
     *
     * @param   key     key for new unit
     * @param   values  values for new unit
     */
    public void addArray(String key, String... values) {
        if (!alreadyUsed(key)) {
            LinkedList<String> arrayValues = new LinkedList<>();

            for (String value : values) {
                arrayValues.add(value);
            }

            units.add(new ArrayUnit(key, arrayValues));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new ArrayUnit to units according to given parameters.
     *
     * Creates new LinkedList of Booleans. Adds all the given values to the
     * list. Adds new ArrayUnit to units using given key and created list.
     *
     * @param   key     key for new unit
     * @param   values  values for new unit
     */
    public void addArray(String key, boolean... values) {
        if (!alreadyUsed(key)) {
            LinkedList<Boolean> arrayValues = new LinkedList<>();

            for (boolean value : values) {
                arrayValues.add(value);
            }

            units.add(new ArrayUnit(key, arrayValues));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new ArrayUnit to units according to given parameters.
     *
     * Creates new LinkedList of Doubles. Adds all the given values to the
     * list. Adds new ArrayUnit to units using given key and created list.
     *
     * @param   key     key for new unit
     * @param   values  values for new unit
     */
    public void addArray(String key, double... values) {
        if (!alreadyUsed(key)) {
            LinkedList<Double> arrayValues = new LinkedList<>();

            for (double value : values) {
                arrayValues.add(value);
            }

            units.add(new ArrayUnit(key, arrayValues));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Adds a new ArrayUnit to units according to given parameters.
     *
     * Creates new LinkedList of JsonFiles. Adds all the given values to the
     * list. Adds new ArrayUnit to units using given key and created list.
     *
     * @param   key     key for new unit
     * @param   values  values for new unit
     */
    public void addArray(String key, JsonFile... values) {
        if (!alreadyUsed(key)) {
            LinkedList<JsonFile> arrayValues = new LinkedList<>();

            for (JsonFile value : values) {
                arrayValues.add(value);
            }

            units.add(new ArrayUnit(key, arrayValues));
        } else {
            throw new RuntimeException("Can't add new unit with already existing key.");
        }
    }

    /**
     * Parses the json-file.
     *
     * Creates the json-file using PrintWriter. Goes trough the units
     * and calls their jsonPrint()-methods.
     */
    public void parse() {
        try {
            PrintWriter writer = new PrintWriter(name + ".json", "UTF-8");

            writer.println("{");

            for (int i = 0; i < units.size(); i++) {
                units.get(i).jsonPrint("  ", writer);

                if (i != units.size() - 1) {
                    writer.print(",");
                }

                writer.println();
            }

            writer.println("}");

            writer.close();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }

    /**
     * Writes the expression of this unit to given json-file.
     * 
     * Uses leadingSpaces to have proper indentation. Calls every units'
     * jsonPrint()-methods.
     *
     * @param   leadingSpaces   indentation
     * @param   writer          json-file   
     */
    public void jsonPrint(String leadingSpaces, PrintWriter writer) {
        writer.println(leadingSpaces + "{");

        for (int i = 0; i < units.size(); i++) {
            units.get(i).jsonPrint(leadingSpaces + "  ", writer);

            if (i != units.size() - 1) {
                writer.print(",");
            }

            writer.println();
        }

        writer.print(leadingSpaces + "}");
    }

    /**
     * Returns the name of the JsonFile.
     *
     * @return  name
     */
    public String getName() {
        return name;
    }

    public String getKey() {
        return null;
    }

    public LinkedList<JsonUnit> getUnits() {
        return units;
    }

    /**
     * Sets a new name for the JsonFile.
     *
     * @param   name    new name
     */
    public void setName(String name) {
        this.name = name;
    }
}