package com.sevador.game.node;

public class ChatMessage {

    private int effects;
    private int numChars;
    private String chatText;

    private int fileId;
    private byte[] data;
    private boolean quickChat;
    public ChatMessage(int effects, int numChars, String chatText) {
        this.effects = effects;
        this.numChars = numChars;
        this.chatText = chatText;
    }

    
    public ChatMessage(int fileId, byte[] data) {
		this.fileId = fileId;
		this.data = data;
		this.effects = 0x8000;
		this.chatText = data != null ? new String(data) : null;
		this.quickChat = true;
	}

	public int getEffects() {
        return effects;
    }

    public int getNumChars() {
        return numChars;
    }

    public String getChatText() {
        return chatText;
    }


	/**
	 * @return the fileId
	 */
	public int getFileId() {
		return fileId;
	}


	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}


	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}


	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}


	/**
	 * @return the quickChat
	 */
	public boolean isQuickChat() {
		return quickChat;
	}


	/**
	 * @param quickChat the quickChat to set
	 */
	public void setQuickChat(boolean quickChat) {
		this.quickChat = quickChat;
	}

}
