package com.sevador.content.activity;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public interface Activity {

	public boolean start();
	
	public boolean onEnter(Entity mob);
	
	public boolean onLeave(Entity mob);
	
	public void idleTick();

	public void activeTick();
	
	public void end();

	public ActivityConstraint getConstraint();
	
	public EventManager getEventListener();
	
}
