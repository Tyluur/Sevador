package net.burtleburtle.script;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lazaro
 */
public class StringParser {
    private Map<String, Object> variableValues = new HashMap<String, Object>();

    public void registerVariable(String variableName, Object variableValue) {
        variableValues.put(variableName, variableValue);
    }

    public void unregisterVariable(String variableName) {
        variableValues.remove(variableName);
    }

    public Object getVariable(String variableName) {
        return variableValues.get(variableName);
    }

    public String parseString(String string) {
        StringBuilder sb = new StringBuilder();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\') {
                i++;
                sb.append(chars[i]);
            } else if (c == '%') {
                StringBuilder varNameBuilder = new StringBuilder();
                for (int x = i + 1; x < chars.length; x++) {
                    char c2 = chars[x];
                    if (c2 == '%') {
                        break;
                    }
                    varNameBuilder.append(c2);
                }
                String varName = varNameBuilder.toString();
                sb.append(getVariable(varName).toString());
                i += varName.length() + 1;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
