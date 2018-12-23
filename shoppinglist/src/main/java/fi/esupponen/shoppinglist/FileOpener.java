package fi.esupponen.shoppinglist;

import fi.esupponen.jsonparser.JsonFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1218
 * @since       2018-1217
 */
public class FileOpener {

    /**
     * Removes all the elements that dont have ':' from given list.
     *
     * Goes trough given list backwards. If there is no character ':' removes the
     * element from the list.
     *
     * @param   list    list to be trimmed
     * @return          list without extra lines
     */
    static public LinkedList<String> trimExtraLines(LinkedList<String> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!list.get(i).contains(":")) {
                list.remove(i);
            }
        }

        return list;
    }

    /**
     * Finds two quotation marks from string and returns substring between them.
     *
     * Finds last and first quotationmarks from given string. If there is no
     * quotationmarks at all or there is only one, throws a runtime exception.
     * In other cases returns substring between the quotationmarks.
     *
     * @param   str     string to be trimmed
     * @return          substring between quotationmarks
     */
    static public String trimQuotationMarks(String str) {
        int firstMark = str.indexOf("\"");
        int lastMark = str.lastIndexOf("\"");

        // If there is only one quotation mark or there is none at all.
        if (firstMark == lastMark) {
            throw new RuntimeException("Misspelling in file, or wrong unit type");
        }

        return str.substring(firstMark + 1, lastMark);
    }

    /**
     * Makes JsonFile from given list and names.
     *
     * Creates new JsonFile object with given name. Goes trough given list
     * and splits every line at character ':'. Uses trimQuotationMarks() to
     * trim both of the parts. Adds the key and the value to the JsonFile.
     *
     * @param   list    list of lines from the source file
     * @param   name    name for the created JsonFile object
     * @return          JsonFile object
     */
    static public JsonFile parseJsonFile(LinkedList<String> list, String name) {
        JsonFile file = new JsonFile(name);

        for (String str : list) {
            String[] pieces = str.split(":");
            String key = trimQuotationMarks(pieces[0]);
            String value = trimQuotationMarks(pieces[1]);
            file.add(key, value);
        }

        return file;
    }

    /**
     * Reads a file and return JsonFile object representing it.
     *
     * Checks if given file is JSON-file. If not, throws an exception. Takes
     * the name of the given file. Creates a list and adds every line from the
     * file to it. Uses trimExtraLines() to get rid of extra lines and then
     * uses  parseJsonFile() with the lsit and the name.
     *
     * @param   path    full path to the JSON-file
     * @return          JsonFile presentation of given file
     * @throws  IOException
     */
    static public JsonFile readFile(String path) throws IOException {
        if (!path.endsWith(".json")) {
            throw new RuntimeException("Given file was not a JSON-file!");
        }

        LinkedList<String> lines = new LinkedList<>();
        BufferedReader reader;
        String name = "";

        if (path.lastIndexOf("/") != -1) {
            name = path.substring(path.lastIndexOf("/") + 1, path.length() - 5);
        } else {
            name = path.substring(path.lastIndexOf("\\") + 1, path.length() - 5);
        }

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("No such file found");
        }

        lines = trimExtraLines(lines);

        return parseJsonFile(lines, name);
    }
}