package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.ArrayList;

import net.burtleburtle.cache.format.CS2ScriptSettings;

import com.sevador.game.node.model.Location;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.MusicPacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Misc;

public final class MusicManager implements Serializable {

	private static final long serialVersionUID = 1020415702861567375L;

	private static final int[] CONFIG_IDS = new int[] { 20, 21, 22, 23, 24, 25,
			298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1104, 1136,
			1180, 1202, 1381, 1394, 1434, 1596, 1618, 1619, 1620, 1865, 1864,
			2246, 2019 };
	private static final int[] PLAY_LIST_CONFIG_IDS = new int[] { 1621, 1622,
			1623, 1624, 1625, 1626 };

	private transient Player player;
	private transient int playingMusic;
	private transient long playingMusicDelay;
	private transient boolean settedMusic;
	private ArrayList<Integer> unlockedMusics;
	private ArrayList<Integer> playList;

	private transient boolean playListOn;
	private transient int nextPlayListMusic;
	private transient boolean shuffleOn;

	public MusicManager() {
		unlockedMusics = new ArrayList<Integer>();
		playList = new ArrayList<Integer>(12);
		// auto unlocked musics
		unlockedMusics.add(62);
		unlockedMusics.add(400);
		unlockedMusics.add(16);
		unlockedMusics.add(466);
		unlockedMusics.add(321);
		unlockedMusics.add(547);
		unlockedMusics.add(621);
		unlockedMusics.add(207);
		unlockedMusics.add(401);
		unlockedMusics.add(147);
		unlockedMusics.add(457);
		unlockedMusics.add(552);
		unlockedMusics.add(858);
	}

	public void passMusics(Player p) {
		for (int musicId : p.getMusicsManager().unlockedMusics) {
			if (!unlockedMusics.contains(musicId))
				unlockedMusics.add(musicId);
		}
	}

	public boolean hasMusic(int id) {
		return unlockedMusics.contains(id);
	}

	public void setPlayer(Player player) {
		this.player = player;
		playingMusic = player.getLocation().getMusicId();
	}

	public void switchShuffleOn() {
		if (shuffleOn) {
			playListOn = false;
			refreshPlayListConfigs();
		}
		shuffleOn = !shuffleOn;
	}

	public void switchPlayListOn() {
		if (playListOn) {
			playListOn = false;
			shuffleOn = false;
			refreshPlayListConfigs();
		} else {
			playListOn = true;
			nextPlayListMusic = 0;
			replayMusic();
		}
	}

	public void clearPlayList() {
		if (playList.isEmpty())
			return;
		playList.clear();
		refreshPlayListConfigs();
	}

	public void addPlayingMusicToPlayList() {
		addToPlayList((int) CS2ScriptSettings.forId(1351).getKey(
				playingMusic));
	}

	public void addToPlayList(int musicIndex) {
		if (playList.size() == 12)
			return;
		int musicId = CS2ScriptSettings.forId(1351).getIntValue(musicIndex);
		if (musicId != -1 && unlockedMusics.contains(musicId)
				&& !playList.contains(musicId)) {
			playList.add(musicId);
			if (playListOn)
				switchPlayListOn();
			else
				refreshPlayListConfigs();
		}
	}

	public void removeFromPlayList(int musicIndex) {
		Integer musicId = CS2ScriptSettings.forId(1351)
				.getIntValue(musicIndex);
		if (musicId != -1 && unlockedMusics.contains(musicId)
				&& playList.contains(musicId)) {
			playList.remove(musicId);
			if (playListOn)
				switchPlayListOn();
			else
				refreshPlayListConfigs();
		}
	}

	public void refreshPlayListConfigs() {
		int[] configValues = new int[PLAY_LIST_CONFIG_IDS.length];
		for (int i = 0; i < configValues.length; i++)
			configValues[i] = -1;
		for (int i = 0; i < playList.size(); i += 2) {
			Integer musicId1 = playList.get(i);
			Integer musicId2 = (i + 1) >= playList.size() ? null : playList
					.get(i + 1);
			if (musicId1 == null && musicId2 == null)
				break;
			int musicIndex = (int) CS2ScriptSettings.forId(1351)
					.getIntValue(musicId1);
			int configValue;
			if (musicId2 != null) {
				int musicIndex2 = (int) CS2ScriptSettings.forId(1351)
						.getIntValue(musicId2);
				configValue = musicIndex | musicIndex2 << 15;
			} else
				configValue = musicIndex | -1 << 15;
			configValues[i / 2] = configValue;
		}
		for (int i = 0; i < PLAY_LIST_CONFIG_IDS.length; i++)
			player.getIOSession()
					.write(new ConfigPacket(player, PLAY_LIST_CONFIG_IDS[i],
							configValues[i]));
	}

	public void refreshListConfigs() {
		int[] configValues = new int[CONFIG_IDS.length];
		for (int musicId : unlockedMusics) {
			int musicIndex = (int) CS2ScriptSettings.forId(1351)
					.getIntValue(musicId);
			if (musicIndex == -1)
				continue;
			int index = getConfigIndex(musicIndex);
			if (index >= CONFIG_IDS.length)
				continue;
			configValues[index] |= 1 << (musicIndex - (index * 32));
		}
		for (int i = 0; i < CONFIG_IDS.length; i++) {
			if (configValues[i] != 0)
				player.getIOSession().write(
						new ConfigPacket(player, CONFIG_IDS[i], configValues[i]));
		}
	}

	public void addMusic(int musicId) {
		unlockedMusics.add(musicId);
		refreshListConfigs();
	}

	public int getConfigIndex(int musicId) {
		return (musicId + 1) / 32;
	}

	public void unlockMusicPlayer() {
		//player.getPacketSender().sendUnlockIComponentOptionSlots(187, 1, 0,
		//		CS2ScriptSettings.forId(1351).getSize() * 2, 0, 2, 3);
	}

	public void init() {
		// unlock music inter all options
		if (playingMusic >= 0)
			playMusic(playingMusic);
		refreshListConfigs();
		refreshPlayListConfigs();
	}

	public boolean musicEnded() {
		return playingMusic != -2
				&& playingMusicDelay + (180000) < Misc.currentTimeMillis();
	}

	public void replayMusic() {
		if (playListOn && playList.size() > 0) {
			if (shuffleOn)
				playingMusic = playList
						.get(Misc.random(playList.size() - 1));
			else {
				if (nextPlayListMusic >= playList.size())
					nextPlayListMusic = 0;
				playingMusic = playList.get(nextPlayListMusic++);
			}
		} else if (unlockedMusics.size() > 0) // random music
			playingMusic = unlockedMusics.get(Misc.random(unlockedMusics
					.size() - 1));
		playMusic(playingMusic);
	}

	public void checkMusic(int requestMusicId) {
		if (playListOn || settedMusic
				&& playingMusicDelay + (180000) >= Misc.currentTimeMillis())
			return;
		settedMusic = false;
		if (playingMusic != requestMusicId)
			playMusic(requestMusicId);
	}

	public void forcePlayMusic(int musicId) {
		settedMusic = true;
		playMusic(musicId);
	}

	public void reset() {
		settedMusic = false;
		player.getMusicsManager().checkMusic(player.getLocation().getMusicId());
	}

	public void playAnotherMusic(int musicIndex) {
		int musicId = CS2ScriptSettings.forId(1351).getIntValue(musicIndex);
		if (musicId != -1 && unlockedMusics.contains(musicId)) {
			settedMusic = true;
			if (playListOn)
				switchPlayListOn();
			playMusic(musicId);
		}

	}

	public void playMusic(int musicId) {
		if (!player.isLoggedIn())
			return;
		playingMusicDelay = Misc.currentTimeMillis();
		if (musicId == -2) {
			playingMusic = musicId;
			player.getIOSession().write(new MusicPacket(player, -1));
			player.getIOSession().write(new StringPacket(player, "", 187, 4));
			return;
		}
		player.getIOSession().write(new MusicPacket(player, musicId));
		playingMusic = musicId;
		int musicIndex = (int) CS2ScriptSettings.forId(1351).getKey(
				musicId);
		if (musicIndex != -1) {
			String musicName = (String) CS2ScriptSettings.forId(1345).getSettings().get((Integer) musicIndex);
			if (musicName.equals(" "))
				musicName = Location.getMusicName1(player.getLocation().getRegionID());
			player.getIOSession().write(
					new StringPacket(player,
							musicName != null ? musicName : "", 187, 4));
			if (!unlockedMusics.contains(musicId)) {
				addMusic(musicId);
				if (musicName != null)
					player.getPacketSender().sendMessage(
							"<col=ff0000>You have unlocked a new music track: "
									+ musicName + ".", true);
			}
		}
	}

}
