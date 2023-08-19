package com.sevador.content.misc;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class IconManager {

    public static final int ARROW_ON_FEET = 40497;

    /**
     * @author 'Mystic Flow
     */
    public static class Icon {

        private int index, slot, targetType, arrowId, modelId;

        public Icon(int slot, int index, int targetType, int arrowId, int modelId) {
            this.slot = slot;
            this.index = index;
            this.targetType = targetType;
            this.arrowId = arrowId;
            this.modelId = modelId;
        }

        public int getSlot() {
            return slot;
        }

        public int getIndex() {
            return index;
        }

        public int getTargetType() {
            return targetType;
        }

        public int getArrowId() {
            return arrowId;
        }

        public int getModelId() {
            return modelId;
        }
    }

    public static final int MAX_ICONS = 8;

    public static int iconOnMob(Player player, Entity mob, int arrowId, int modelId) {
        int slot = freeIconSlot(player, mob);
        if (slot > -1) {
            //player.setAttribute("icon_slot" + slot, new Icon().setSlot(slot).setIndex(mob.getIndex()).setTargetType(mob.isPlayer() ? 1 : 10).setArrowId(arrowId).setModelId(modelId));
            Icon icon = new Icon(slot, mob.getIndex(), mob.isPlayer() ? 10 : 1, arrowId, modelId);
            player.setAttribute("icon_slot" + slot, icon);
            player.getPacketSender().sendIcon(player, icon);
        }
        return slot;
    }

    public static void removeIcon(Player player, Entity mob) {
        int slot = freeIconSlot(player, mob);
        Icon icon = player.getAttribute("icon_slot" + slot);
        if (icon != null) {
            icon.targetType = 0;
            player.getPacketSender().sendIcon(player, icon);
            player.removeAttribute("icon_slot" + slot);
        }
    }

    public static void iconOnCoordinate(Player player) {

    }

    public static int freeIconSlot(Player player, Entity mob) {
        for (int i = 0; i < MAX_ICONS; i++) {
            Icon icon = player.getAttribute("icon_slot" + i);
            if (mob != null && icon != null && icon.getIndex() == mob.getIndex()) {
                return i;
            }
            if (icon == null) {
                return i;
            }
        }
        return -1;
    }

}
