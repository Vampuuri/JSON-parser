import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import fi.esupponen.jsonparser.*;
import java.util.LinkedList;

public class JsonBasicsTest {
    @Test
    public void testBooleanUnit() {
        BooleanUnit bu = new BooleanUnit("boolean");
        Assert.assertEquals(bu.toString(), "\"boolean\": null");
        bu.setValue(true);
        Assert.assertEquals(bu.toString(), "\"boolean\": true");
        bu.setValue(false);
        Assert.assertEquals(bu.toString(), "\"boolean\": false");
    }

    @Test
    public void testStringUnit() {
        StringUnit su = new StringUnit("string");
        Assert.assertEquals(su.toString(), "\"string\": null");
        su.setValue("");
        Assert.assertEquals(su.toString(), "\"string\": \"\"");
        su.setValue("value");
        Assert.assertEquals(su.toString(), "\"string\": \"value\"");
    }

    @Test
    public void testNumberUnit() {
        NumberUnit nu = new NumberUnit("number", 0);
        Assert.assertEquals(nu.toString(), "\"number\": 0");
        nu.setValue(23);
        Assert.assertEquals(nu.toString(), "\"number\": 23");
        nu.setValue(3.1453);
        Assert.assertEquals(nu.toString(), "\"number\": 3.1453");
    }

    @Test
    public void testArrayUnitDouble() {
        LinkedList<Double> listDouble = new LinkedList<>();
        ArrayUnit<Double> aud = new ArrayUnit<>("doubles", listDouble);
        Assert.assertEquals(aud.toString(), "\"doubles\": []");
        listDouble.add(new Double(13.0));
        listDouble.add(new Double(3.13));
        Assert.assertEquals(aud.toString(), "\"doubles\": [13, 3.13]");
    }

    @Test
    public void testArrayUnitString() {
        LinkedList<String> listString = new LinkedList<>();
        listString.add("string1");
        listString.add("string2");
        ArrayUnit<String> aus = new ArrayUnit<>("strings", listString);
        Assert.assertEquals(aus.toString(), "\"strings\": [\"string1\", \"string2\"]");
    }

    @Test
    public void testObjectUnit() {
        ObjectUnit ou = new ObjectUnit("object", null);
        Assert.assertEquals(ou.toString(), "\"object\": null");
        ou.add(new BooleanUnit("boolean", true));
        ou.add(new NumberUnit("PI", 3.14159265));
        ou.add(new StringUnit("string", "string"));
        Assert.assertEquals(ou.toString(), "\"object\": {\"boolean\": true, \"PI\": 3.14159265, \"string\": \"string\"}");
    }

    @Test
    public void testJsonFile() {
        JsonFile file = new JsonFile("file");
        file.add("string", "string");
        file.add("boolean", true);
        file.add("double", 3.24);
        file.add("integer", 3);
        Assert.assertEquals(file.toString(),"{\"string\": \"string\", \"boolean\": true, \"double\": 3.24, \"integer\": 3}");
    }
}