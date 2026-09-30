package org.vadim.dto;

import java.util.UUID;

public record TelegramConnectDto (
    String botName,
    UUID authenticationToken
){}
