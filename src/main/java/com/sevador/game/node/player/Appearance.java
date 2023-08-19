package com.sevador.game.node.player;

import java.io.Serializable;

import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.node.Item;
import com.sevador.utility.Constants;


/**
 * The appearance of the player.
 * 
 * @author Emperor
 * 
 */
public class Appearance implements Serializable {

	/*
	 * int[] textures = player.textures;
			int mask = 2048;
			buf.putShort(mask);
			int pos = 0;
			for (int slot = 0; slot < textures.length; slot++) {
				int texture = textures[slot];
				if (texture == 0) {
					if ((1 >> pos & mask) != 0) {
						int textureSet = 0x8;
						if ((textureSet & 0x8) != 0) {
							for (int val = 0; val < 2; val++) {
								if (slot != 15) {
									buf.putShort(1000);
								}
							}
						}
					}
					pos++;
				}
			}
	 */
	
	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 1429572935338669434L;

	/**
	 * The player's body parts data.
	 */
	private int[] bodyParts;
	
	/**
	 * The look.
	 */
	private int[] look;
	
	/**
	 * An array containing all the player's colours.
	 */
	private int[] colors;

	/**
	 * If the player is male or female.
	 */
	private boolean male;

	/**
	 * The NPC id of the player. (In case of being an NPC.)
	 */
	private int npcId;
	
	/**
	 * The player's render emote.
	 */
	private int renderEmote = 1426;

	/**
	 * The constructor.
	 * 
	 * @param player
	 *            The player.
	 */
	public Appearance() {
		setDefaultAppearance();
	}

	/**
	 * Sets the default appearance.
	 */
	public void setDefaultAppearance() {
		this.look = new int[7];
		this.bodyParts = new int[ItemDefinition.getBodyData().length];
		this.colors = new int[10];
		setLook(0, 1);
		setLook(1, 10);
		setLook(2, 18);
		setLook(3, 28);
		setLook(4, 34);
		setLook(5, 39);
		setLook(6, 42);
		for (int i = 0; i < 10; i++) { 
			colors[i] = i * 6 + 2; 
		}
		this.colors[0] = 1;
		this.colors[1] = 7;
		this.colors[2] = 6;
		this.male = true;
		this.npcId = -1;
	}
	
	/**
	 * Draws an item on a body part.
	 * @param part The body part.
	 * @param item The item to draw.
	 */
    public void drawItem(int part, Item item) {
    	if(item.getDefinition() == null) item.setDefinition(ItemDefinition.forId(item.getId()));
        this.bodyParts[part] = item.getDefinition().getEquipId() + 0x8000;
    }

    /**
     * Draws clothing on a body part.
     * @param part The body part.
     * @param clothesId The clothes id.
     */
    public void drawClothes(int part, int clothesId) {
        this.bodyParts[part] = clothesId + 0x100;
    }

    /**
     * Clears a body part.
     * @param part The part to clear.
     */
    public void clearBodyPart(int part) {
        this.bodyParts[part] = 0;
    }

    /**
     * Gets a body part.
     * @param part The part.
     * @return The body part data.
     */
    public int getBodyPart(int part) {
        return this.bodyParts[part];
    }
    
	/**
	 * Prepares the body parts data.
	 */
	public void prepareBodyData(Player player) {
        Item chest = player.getEquipment().get(Constants.SLOT_CHEST);
        Item shield = player.getEquipment().get(Constants.SLOT_SHIELD);
        Item legs = player.getEquipment().get(Constants.SLOT_LEGS);
        Item hat = player.getEquipment().get(Constants.SLOT_HAT);
        Item hands = player.getEquipment().get(Constants.SLOT_HANDS);
        Item feet = player.getEquipment().get(Constants.SLOT_FEET);
        Item aura = player.getEquipment().get(Constants.SLOT_AURA);
        Item cape = player.getEquipment().get(Constants.SLOT_CAPE);
        Item amulet = player.getEquipment().get(Constants.SLOT_AMULET);
        Item weapon = player.getEquipment().get(Constants.SLOT_WEAPON);
        if (hat != null) {
            drawItem(0, hat);
        } else {
            clearBodyPart(0);
        }
        if (cape != null) {
            drawItem(1, cape);
		} else {
            clearBodyPart(1);
		}
        if (amulet != null) {
            drawItem(2, amulet);
        } else {
            clearBodyPart(2);
        }
        if (weapon != null) {
        	if (npcId == -1) {
        		if (weapon.getDefinition() == null)
        			weapon.setDefinition(ItemDefinition.forId(weapon.getId()));
        		renderEmote = weapon.getDefinition().getRenderAnimId();
        	}
            drawItem(3, weapon);
        } else {
        	if (npcId == -1) {
        		renderEmote = 1426;
        	}
            clearBodyPart(3);
        }
        if (chest != null) {
            drawItem(4, chest);
        } else {
            drawClothes(4, getLook(2));
        }
        if (shield != null) {
            drawItem(5, shield);
        } else {
            clearBodyPart(5);
        }
        if (chest != null && chest.getDefinition().isFullBody()) {
            clearBodyPart(6);
        } else {
            drawClothes(6, getLook(3));
        }
        if (legs != null) {
            drawItem(7, legs);
        } else {
            drawClothes(7, getLook(5));
        }
        if (hat != null && (hat.getDefinition().isFullMask() || hat.getDefinition().isFullHat())) {
            clearBodyPart(8);
        } else {
            drawClothes(8, getLook(0));
        }
        if (hands != null) {
            drawItem(9, hands);
        } else {
            drawClothes(9, getLook(4));
        }
        if (feet != null) {
            drawItem(10, feet);
        } else {
            drawClothes(10, getLook(6));
        }
        if (hat != null && hat.getDefinition().isFullMask()) {
            clearBodyPart(11);
        } else {
            drawClothes(11, getLook(1));
        }
        if (aura != null) {
            drawItem(14, aura);
        } else {
            clearBodyPart(14);
        }
	}

	/**
	 * Sets a colour.
	 * 
	 * @param slot
	 *            The slot.
	 * @param color
	 *            The colour.
	 */
	public void setColor(byte slot, byte color) {
		this.colors[slot] = color;
	}
	
	public void setColor(int i, int j) {
		this.colors[i] = j;
	}

	/**
	 * Gets the colour on the given slot.
	 * 
	 * @param slot
	 *            The slot.
	 * @return The colour.
	 */
	public int getColor(int slot) {
		return colors[slot];
	}

	/**
	 * @param male
	 *            the male to set
	 */
	public void setMale(boolean male) {
		this.male = male;
	}

	/**
	 * @return the male
	 */
	public boolean isMale() {
		return male;
	}

	/**
	 * @param npcId
	 *            the npcId to set
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
		if (npcId > 0) {
			this.renderEmote = NPCDefinition.forId(npcId).renderEmote;
		} else {
			this.renderEmote = 1426;//TODO
		}
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @param i 
	 * @return the look
	 */
	public int getLook(int i) {
		return look[i];
	}

	/**
	 * @param look the look to set
	 */
	public void setLook(int slot, int look) {
		this.look[slot] = look;
	}

	/**
	 * @return the renderEmote
	 */
	public int getRenderEmote() {
		return renderEmote;
	}

	/**
	 * @param renderEmote the renderEmote to set
	 */
	public void setRenderEmote(int renderEmote) {
		this.renderEmote = renderEmote;
	}

}
