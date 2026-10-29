package com.skybreaker.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkybreakerClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("skybreaker-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Skybreaker Client renderer & effects");
    }
}
