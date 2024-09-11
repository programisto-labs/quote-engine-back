package com.programisto.devis_rapide.application.discord_manager;

import com.programisto.devis_rapide.application.discord_manager.service.impl.DiscordService;
import com.programisto.devis_rapide.application.discord_manager.entity.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discord")
public class DiscordController {

    @Autowired
    private DiscordService discordService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageRequest request) {
        discordService.sendMessage(request.getChannelId(), request.getMessage()).subscribe(
                null, //value -> System.out.println("Message sent: " + value),
                error -> System.err.println("Error sending message: " + error),
                () -> System.out.println("Completed")
        );
    }
}


