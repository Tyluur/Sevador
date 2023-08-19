package com.sevador.content.chardesign;

import java.io.Serializable;

public class DesignState implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5470858813509958422L;
	public static enum InterfaceState {
        MAIN,
        CUSTOMIZATION;
    }

    public static enum CustomizeCategory {
        SKIN,
        HAIR,
        TORSO,
        LEGS,
        SHOES,
        FACIAL_HAIR;

        public static CustomizeCategory getCustomIndex(int index) {
            switch (index) {
                case 0:
                    return CustomizeCategory.SKIN;
                case 1:
                    return CustomizeCategory.HAIR;
                case 2:
                    return CustomizeCategory.TORSO;
                case 3:
                    return CustomizeCategory.LEGS;
                case 4:
                    return CustomizeCategory.SHOES;
                case 5:
                    return CustomizeCategory.FACIAL_HAIR;
            }
            return null;
        }
    }

    public InterfaceState state = InterfaceState.MAIN;
    public CustomizeCategory customIndex = CustomizeCategory.SKIN;
    public int designIndex = -1;
    public int secondaryDesignIndex = -1;
}
