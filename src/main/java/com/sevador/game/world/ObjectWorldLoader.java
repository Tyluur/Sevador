package com.sevador.game.world;

import com.sevador.game.node.Node;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.NodeTypeRepositary;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionBuilder;
import com.sevador.game.region.RegionManager;
import com.sevador.network.out.ConstructObject;
import com.sevador.network.out.RemoveObject;
import com.sevador.utility.ReturnCodes;

/**
 * A WorldLoader implementation that handles the loading of game objects.
 * @author Emperor
 *
 */
public class ObjectWorldLoader implements WorldLoader<GameObject> {

	@Override
	public LoginResult checkLogin(Node node) {
		return new LoginResult(ReturnCodes.SUCCESFUL, node);
	}

	@Override
	public GameObject load(LoginResult result) {
		GameObject gameObject = (GameObject) result.getNode();
		boolean sendRemoveObject = gameObject.hasRemoved();
		for (Player player : RegionManager.getLocalPlayers(gameObject.getLocation())) {
			if (sendRemoveObject) {
				player.getIOSession().write(new RemoveObject(player, gameObject.getRemoved()));
			}
			player.getIOSession().write(new ConstructObject(player, gameObject));
		}
		NodeType type = NodeTypeRepositary.get(gameObject);
		if (type != null) {
			gameObject.setNodeType(type);
		}
		return gameObject;
	}

	@Override
	public GameObject save(Node node) {
		GameObject gameObject = (GameObject) node;
		boolean sendConstructObject = gameObject.hasConstructed();
		for (Player player : RegionManager.getLocalPlayers(gameObject.getLocation())) {
			if (sendConstructObject) {
				player.getIOSession().write(new ConstructObject(player, gameObject.getConstructed()));
			}
			player.getIOSession().write(new RemoveObject(player, gameObject));
		}
		RegionBuilder.removeObject(gameObject.getLocation().getX(), gameObject.getLocation().getY(), gameObject.getLocation().getZ(), gameObject.getType());
		return gameObject;
	}

}
