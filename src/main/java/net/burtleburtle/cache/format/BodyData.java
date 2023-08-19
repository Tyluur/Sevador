package net.burtleburtle.cache.format;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import net.burtleburtle.cache.CacheManager;
import net.burtleburtle.cache.stream.RSInputStream;

/**
 * Represents body data.
 * @author Jolt Environment V2 Development team.
 * @author Emperor (Converting to Java)
 *
 */
public class BodyData {
	
	/**
	 * The parts data.
	 */
    public int[] partsData;
    
    /**
     * Something to do with weapon display.
     */
    public int somethingWithWeaponDisplay;
   
    /**
     * Contains weapon data.
     */
    public int[] weaponData;
    
    /**
     * Contains shield data.
     */
    public int[] shieldData;

    /**
     * Something to do with shield display.
     */
    public int somethingWithShieldDisplay;
    
    /**
     * Constructs a new {@code BodyData} {@code Object}.
     */
    public BodyData() {
        this.partsData = new int[0];
        this.somethingWithWeaponDisplay = -1;
        this.somethingWithShieldDisplay = -1;
    }
    
    /**
     * Parses body data from the given buffer.
     * @param buffer The buffer.
     */
    private void parse(RSInputStream buffer) {
        for (;;) {
            byte opcode;
			try {
				opcode = (byte) buffer.readUnsignedByte();
			} catch (IOException e) {
				e.printStackTrace();
				opcode = 0;
			}
            if (opcode == 0) {
                break;
            }
            this.parse(opcode, buffer);
        }
    }

    /**
     * Parses the current opcode.
     * @param opcode The opcode.
     * @param buffer The buffer to parse from.
     */
    private void parse(byte opcode, RSInputStream buffer) {
    	try {
        if (opcode == 1) {
            int length = buffer.readUnsignedByte();
            this.partsData = new int[length];
            for (int i = 0; i < this.partsData.length; i++)
                this.partsData[i] = buffer.readUnsignedByte();
        } else if (opcode == 3) {
            this.somethingWithShieldDisplay = buffer.readUnsignedByte();
        } else if (opcode == 4) {
            this.somethingWithWeaponDisplay = buffer.readUnsignedByte();
        } else if (opcode == 5) {
            int length = buffer.readUnsignedByte();
            this.shieldData = new int[length];
            for (int i = 0; i < this.shieldData.length; i++)
                this.shieldData[i] = buffer.readUnsignedByte();
        } else if (opcode == 6) {
            int length = buffer.readUnsignedByte();
            this.weaponData = new int[length];
            for (int i = 0; i < this.weaponData.length; i++)
                this.weaponData[i] = buffer.readUnsignedByte();
        } else
        	throw new Exception("Unknown opcode:" + opcode);
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    }

    /**
     * Reads body data from the cache.
     * @return The body data object, or null if it failed.
     */
    public static BodyData read() {
        BodyData data = new BodyData();
        try {
            byte[] buff = CacheManager.getData(28, 6, 0);
            RSInputStream reader = new RSInputStream(new ByteArrayInputStream(buff));
            data.parse(reader);
            reader.close();
            return data;
        } catch (Exception exception) {
            /**
             * There shouldn't be any fails.
             */
            exception.printStackTrace();
            return null;
        }
    }
}