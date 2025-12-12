package com.goblinfix;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("goblinfix")
public class GoblinFixMod {
    public static final Logger LOGGER = LogManager.getLogger("GoblinFix");

    public GoblinFixMod() {
        LOGGER.info("[Goblin Fix] Mod loaded! Tensura goblin clothing crash fix active.");
    }
}

