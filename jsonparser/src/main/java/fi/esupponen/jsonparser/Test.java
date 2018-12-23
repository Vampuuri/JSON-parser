package fi.esupponen.jsonparser;

import java.util.LinkedList;

class Test {
    public static void main(String[] args) {
        JsonFile file = new JsonFile("file");
        file.add("boolean", true);
        file.add("string", "text");
        file.add("double", 3.12);
        file.add("int", 3);

        file.addArray("many boolean", true, true, false);
        file.addArray("many number", 3.12, 4.563, 67);
        file.addArray("many string", "text1", "text2");

        LinkedList<JsonUnit> object = new LinkedList<>();
        object.add(new BooleanUnit("true", false));
        object.add(new StringUnit("string", "thing"));

        file.add("object", object);

        JsonFile unit1 = new JsonFile("unit");
        unit1.add("boolean", true);
        JsonFile unit2 = new JsonFile("unit");
        unit2.add("boolean", false);

        file.addArray("array", unit1, unit2);

        file.parse();

        System.out.println(file);
    }
}