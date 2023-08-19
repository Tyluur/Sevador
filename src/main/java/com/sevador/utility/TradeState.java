package com.sevador.utility;

/**
 * Represents the trade states.
 * @author Emperor
 *
 */
public enum TradeState {

	/**
	 * The trade is currently being requested.
	 */
	REQUESTING,
	
	/**
	 * The trade has commenced and we're in the first screen where we can offer/remove items.
	 */
	FIRST_SCREEN,
	
	/**
	 * The trade is in the second screen where we check what items have been offered.
	 */
	SECOND_SCREEN,
	
	/**
	 * The trade is finished.
	 */
	FINISHED;
	
}