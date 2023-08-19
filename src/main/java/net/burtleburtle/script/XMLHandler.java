package net.burtleburtle.script;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.content.areas.impl.CircularArea;
import com.sevador.content.areas.impl.IrregularArea;
import com.sevador.content.areas.impl.RectangularArea;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.skills.summoning.SummoningPouch;
import com.sevador.utility.WeaponInterface;
import com.thoughtworks.xstream.XStream;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
@SuppressWarnings("unchecked")
public final class XMLHandler {

    private XMLHandler() {
    }

    private static XStream xmlHandler;

    static {
        xmlHandler = new XStream();
        xmlHandler.alias("item", Item.class);
        xmlHandler.alias("rectangle", RectangularArea.class);
        xmlHandler.alias("circle", CircularArea.class);
        xmlHandler.alias("irregular", IrregularArea.class);
        xmlHandler.alias("position", Location.class);
        xmlHandler.alias("ban", String.class);
        xmlHandler.alias("npcDefinition", NPCDefinition.class);
        xmlHandler.alias("weaponInterface", WeaponInterface.class);
        xmlHandler.alias("pouch", SummoningPouch.class);
    }

    public static void toXML(String file, Object object) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            xmlHandler.toXML(object, out);
            out.flush();
        } finally {
            out.close();
        }
    }

    public static <T> T fromXML(String file) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            return (T) xmlHandler.fromXML(in);
        } finally {
            in.close();
        }
    }

}
