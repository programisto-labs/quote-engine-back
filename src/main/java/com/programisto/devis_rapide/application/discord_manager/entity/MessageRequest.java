package com.programisto.devis_rapide.application.discord_manager.entity;

public class MessageRequest {
    private String channelId;
    private String message;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
