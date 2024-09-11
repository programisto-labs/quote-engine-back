package com.programisto.devis_rapide.application.discord_manager.service.impl;


import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DiscordService implements com.programisto.devis_rapide.application.discord_manager.service.DiscordService {

    @Autowired
    private GatewayDiscordClient client;

    public Mono<Void> sendMessage(String channelId, String message) {
        return client.getChannelById(Snowflake.of(channelId))
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage(message))
                .retry(3) // Retry 3 time in case of error
                .doOnError(error -> System.err.println("Error sending message: " + error.getMessage()))
                .then();
    }
}

