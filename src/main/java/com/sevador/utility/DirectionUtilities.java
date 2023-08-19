package com.sevador.utility;

/**
 * Holds direction-related methods.
 * @author Jolt Environment v2 development team.
 * @author Emperor (converting to Java)
 *
 */
public class DirectionUtilities {
	
	/**
	 * The three bits movement type array.
	 */
    public static final int[][] ThreeBitsMovementType = new int[][] { { 0, 3, 5 }, { 1, -1, 6 }, { 2, 4, 7 } };
    
    /**
     * The four bits movement type array.
     */
    public static final int[][] FourBitsMovementType = new int[][] { { 0, 5, 7, 9, 11 }, { 1, -1, -1, -1, 12 }, { 2, -1, -1, -1, 13 }, { 3, -1, -1, -1, 14 }, { 4, 6, 8, 10, 15 } };

    /**
     * The region movement types.
     */
    public static final int[][] RegionMovementType = new int[][] { { 0, 3, 5 }, { 1, -1, 6 }, { 2, 4, 7 } };
}