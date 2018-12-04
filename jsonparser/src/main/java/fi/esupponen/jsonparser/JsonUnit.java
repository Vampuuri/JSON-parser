package fi.esupponen.jsonparser;
import java.io.PrintWriter;

/**
 * @author      Essi Supponen [essi.supponen@cs.tamk.fi]
 * @version     2018-1128
 * @since       2018-1120
 */
public interface JsonUnit {
    public void jsonPrint(String leadingSpaces, PrintWriter writer);
    public String getKey();
}