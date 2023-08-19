package com.sevador.utility.saving;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;

import com.sevador.game.action.impl.TradeAction;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Constants;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SerializeSave {
	
	public synchronized static void savePlayer(Player player) {
		try {
			if (player != null && player.getActionManager() != null && player.getActionManager().contains(TradeAction.FLAG))
				player.getActionManager().unregister(TradeAction.FLAG);
			if (player != null)
			storeSerializableClass(player, new File(Constants.SAVE_PATH + player.getCredentials().getUsername()
					+ ".ser"));
		} catch (ConcurrentModificationException e) {
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static final void storeSerializableClass(Serializable o, File f)
			throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o);
		out.close();
	}


}
