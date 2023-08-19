package com.sevador.game.node.control;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;

public abstract class Controler {

	// private static final long serialVersionUID = 8384350746724116339L;

	protected Player player;

	public final void setPlayer(Player player) {
		this.player = player;
	}

	public final Object[] getArguments() {
		return player.getControlerManager().getLastControlerArguments();
	}
	
	public final void setArguments(Object[] objects) {
		player.getControlerManager().setLastControlerArguments(objects);
	}

	public final void removeControler() {
		player.getControlerManager().removeControlerWithoutCheck();
	}

	public abstract void start();

	/*
	 * after the normal checks, extra checks, only called when you attacking
	 */
	public boolean keepCombating(Entity target) {
		return true;
	}

	public boolean canEquip(int slotId, int itemId) {
		return true;
	}

	/*
	 * after the normal checks, extra checks, only called when you start trying
	 * to attack
	 */
	public boolean canAttack(Entity target) {
		return true;
	}

	public void trackXP(int skillId, int addedXp) {

	}

	public boolean canDeleteInventoryItem(int itemId, int amount) {
		return true;
	}

	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		return true;
	}
	
	public boolean closeInterface() {
		return true;
	}

	public boolean canAddInventoryItem(int itemId, int amount) {
		return true;
	}

	public boolean canPlayerOption1(Player target) {
		return true;
	}

	/*
	 * hits as ice barrage and that on multi areas
	 */
	public boolean canHit(Entity entity) {
		return true;
	}

	/*
	 * processes every game ticket, usualy not used
	 */
	public void process() {

	}

	public void moved() {

	}

	public void magicTeleported(int type) {

	}

	public void sendInterfaces() {

	}

	/*
	 * return can use script
	 */
	public boolean useDialogueScript(Object key) {
		return true;
	}

	/*
	 * return can teleport
	 */
	public boolean processMagicTeleport() {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processNPCClick1(NPC npc) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processNPCClick2(NPC npc) {
		return true;
	}

	/*
	 * return let default death
	 */
	public boolean sendDeath() {
		return true;
	}

	/*
	 * return can move that step
	 */
	public boolean canMove(int dir) {
		return true;
	}

	/*
	 * return can set that step
	 */
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		return true;
	}

	/*
	 * return remove controler
	 */
	public boolean login() {
		return true;
	}

	/*
	 * return remove controler
	 */
	public boolean logout() {
		return true;
	}

	public void forceClose() {
	}

	public boolean processItemOnNPC(NPC npc, Item item) {
		return true;
	}

	public boolean handleItemOption1(Player player, int slotId, int itemId,
			Item item) {
		return false;
	}

	public boolean processObjectClick1(GameObject object) {
		return true;
	}
}
