package net.burtleburtle.cache.format;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.cache.CacheManager;

import com.sevador.utility.BufferUtils;

/**
 *
 * @author `Discardedx2
 */
public class CS2ScriptSettings  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5955933066930440753L;
	/**
	 * The definitions mapping.
	 */
	private static final Map<Integer, CS2ScriptSettings> DEFINITIONS = new HashMap<Integer, CS2ScriptSettings>();
    private String defaultString;
    private char valueType;
    private char keyType;
    private int defaultInteger;
    private Map<Integer, Object> settings;

    /**
     * The client script setting definitions.
     * @param id The client script id.
     * @return The definitions.
     */
    public static CS2ScriptSettings forId(int id) {
    	CS2ScriptSettings def = DEFINITIONS.get(id);
    	if (def != null) {
    		return def;
    	}
    	try {
			byte[] data = CacheManager.getData(17, id >>> 8, id & 0xFF);
			def = decode(ByteBuffer.wrap(data));
			DEFINITIONS.put(id, def);
			return def;
		} catch (Throwable e) {
			//e.printStackTrace();
		}    	
		return null;
    }
    
    /**
     *
     * @param buffer The buffer which contains information
     * for the file.
     * @return a new CachedSettingDefinition
     */
    private static CS2ScriptSettings decode(ByteBuffer buffer) {
        CS2ScriptSettings def = new CS2ScriptSettings();
        def.settings = new HashMap<Integer, Object>();
        while (true) {
            int opcode = buffer.get() & 0xFF;
            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                def.keyType = BufferUtils.getCPCharacter(buffer);
            } else if (opcode == 2) {
                def.valueType = BufferUtils.getCPCharacter(buffer);
            } else if (opcode == 3) {
                def.defaultString = BufferUtils.readRS2String(buffer);
            } else if (opcode == 4) {
                def.defaultInteger = buffer.getInt();
            } else if (opcode == 5 || opcode == 6) {
                int range = buffer.getShort() & 0xFFFF;
                for (int offset = 0; offset < range; offset++) {
                    int settingIdentifier = buffer.getInt();
                    Object data;
                    if (opcode == 5) {
                        data = BufferUtils.readRS2String(buffer);
                        //System.out.println(settingIdentifier + ", " + data);
                    } else {
                        data = buffer.getInt();
                    }
                    def.settings.put(settingIdentifier, data);
                }
            } else if (opcode == 7) {
				buffer.getShort();
				int length = buffer.getShort() & 0xFF;
				for (int i_5_ = 0; length > i_5_; i_5_++) {
					int key = (buffer.getShort() & 0xFF);
                    def.settings.put(key, BufferUtils.readRS2String(buffer));
                    //System.out.println(key + ", " + def.settings.get(key));
				}
			} else if (opcode == 8) {
				buffer.getShort();
				int length = buffer.getShort() & 0xFF;
				for (int i_8_ = 0; i_8_ < length; i_8_++) {
                    def.settings.put((buffer.getShort() & 0xFF), new Integer(buffer.getInt()));
				}
			} else {
				System.out.println("Opcode: " + opcode);
			}
        }
        return def;
    }

    public int getDefaultInteger() {
        return defaultInteger;
    }

    public String getDefaultString() {
        return defaultString;
    }

    public char getKeyType() {
        return keyType;
    }

    public Map<Integer, Object> getSettings() {
        return settings;
    }

    public char getValueType() {
        return valueType;
    }

	public int getKey(Object o) {
		for (int i : settings.keySet()) {
			if (settings.get(i).equals(o)) {
				return i;
			}
		}
		return -1;
	}

	public int getIntValue(int musicIndex) {
		for (int i : settings.keySet()) {
			if (settings.get(i).equals(musicIndex)) {
				return (Integer) i;
			}
		}
		return -1;
	}

	public String getStringValue(int musicIndex) {
		for (Object i : settings.keySet()) {
			if (settings.get(i).equals(musicIndex)) {
				return (String) i;
			}
		}
		return null;
	}
}