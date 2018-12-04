import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import fi.esupponen.jsonparser.*;

public class JsonParserTest {
    JsonFile testFile;

    public JsonParserTest() {
        testFile = new JsonFile("test");

        testFile.add("boolean", true);
        testFile.add("string", "merkkijono");
        testFile.add("double", 3.45);
        testFile.add("integer", 24);
        testFile.addArray("stringarray", "merkkijono1", "merkkijono2", "merkkijono3");
        testFile.addArray("numberArray", 2, 4.5, 0.23, 32);
        testFile.addArray("booleanArray", true, true, false);

        JsonFile arrayUnit = new JsonFile("unit");
        arrayUnit.add("test", "test");
        arrayUnit.add("totta", true);

        testFile.addArray("fileArray", arrayUnit, arrayUnit);
    }

    @Test
    public void testKeyUsed() {
        Assert.assertTrue("Key 'boolean' should be found", testFile.alreadyUsed("boolean"));
        Assert.assertFalse("Key 'key' should not be found", testFile.alreadyUsed("key"));
    }

    @Test(expected = Exception.class)
    public void testAdditionFailed() {
        testFile.add("integer", false);
    }

    @Test(expected = Exception.class)
    public void testArrayAdditionFailed() {
        testFile.addArray("string", 0, 3, 4, 5);
    }

    @Test(expected = Exception.class)
    public void testJsonfileAdditionFailed() {
        testFile.add(new JsonFile("fails"));
    }
}