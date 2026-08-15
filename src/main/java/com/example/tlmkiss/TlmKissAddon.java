package com.example.tlmkiss;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TlmKissAddon implements ModInitializer {
    public static final String MOD_ID = "tlm_kiss_addon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("TLM Kiss Addon loaded!");
    }
}