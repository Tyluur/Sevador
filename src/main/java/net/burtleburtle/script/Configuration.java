package net.burtleburtle.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.sevador.utility.Misc;

/**
 * @author Lazaro
 */
public class Configuration {
    private Map<String, Object> values = new HashMap<String, Object>();

    public Configuration(File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            if (line.contains("=")) {
                String key = line.substring(0, line.indexOf("="));
                Object value = Misc.parseString(line.substring(line.indexOf("=") + 1));
                try {
                    value = Integer.parseInt(value.toString());
                } catch (NumberFormatException ex) {
                }
                if (key.contains("[") && key.contains("]")) {
                    int index = Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
                    key = key.substring(0, key.indexOf("["));
                    Object[] array = (Object[]) values.get(key);
                    array[index] = value;
                } else {
                    values.put(key, value);
                }
            } else if (line.startsWith("array") && line.contains("[") && line.contains("]")) {
                String key = line.substring(line.indexOf(" ") + 1, line.indexOf("["));
                int size = Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
                Object[] array = new Object[size];
                values.put(key, array);
            }
            in.close();
        }
    }

    public Configuration(String file) throws IOException {
        this(new File(file));
    }

    public Object[] getArray(String key) {
        Object obj = values.get(key);
        if (obj instanceof Object[])
            return (Object[]) obj;
        return null;
    }

    public int getInt(String key) {
        Object value = values.get(key);
        if (value instanceof Integer)
            return (Integer) value;
        throw new RuntimeException("Requested variable, " + key + ", is not an integer or isn't available!");
    }

    public int[] getIntArray(String key) {
        return getIntArray(key, 0);
    }

    public int[] getIntArray(String key, int defaultValue) {
        Object obj = values.get(key);
        if (obj instanceof Object[]) {
            Object[] array = getArray(key);
            int[] intArray = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    intArray[i] = (Integer) array[i];
                } else {
                    intArray[i] = defaultValue;
                }
            }
            values.remove(key);
            values.put(key, intArray);
            return intArray;
        } else if (obj instanceof int[])
            return (int[]) obj;
        return null;
    }

    public String getString(String key) {
        Object obj = values.get(key);
        if (obj instanceof String || obj instanceof Integer)
            return obj.toString();
        return null;
    }

    public String[] getStringArray(String key) {
        Object obj = values.get(key);
        if (obj instanceof Object[]) {
            Object[] array = getArray(key);
            String[] stringArray = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    stringArray[i] = array[i].toString();
                }
            }
            values.remove(key);
            values.put(key, stringArray);
            return stringArray;
        } else if (obj instanceof String[])
            return (String[]) obj;
        return null;
    }
}
