package com.goblinfix.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to fix ArrayIndexOutOfBoundsException in GoblinVariant$Clothing.byId()
 * when the Clothing ID is -1 or otherwise out of bounds (valid range: 0-5)
 */
@Pseudo
@Mixin(targets = "com.github.manasmods.tensura.entity.variant.GoblinVariant$Clothing", remap = false)
public class GoblinVariantClothingMixin {

    private static final Logger LOGGER = LogManager.getLogger("GoblinFix");
    private static final String CLOTHING_CLASS = "com.github.manasmods.tensura.entity.variant.GoblinVariant$Clothing";

    /** Cached enum constants to avoid repeated reflection when clamping invalid IDs. */
    private static volatile Object[] cachedEnumConstants;

    /**
     * Intercept the byId method and clamp invalid IDs to valid range.
     * Always cancels when id is out of range so the original method is not invoked.
     */
    @Inject(method = "byId", at = @At("HEAD"), cancellable = true)
    private static void fixInvalidClothingId(int id, CallbackInfoReturnable<Object> cir) {
        // Valid clothing IDs are 0-5 (array length 6)
        if (id < 0 || id > 5) {
            LOGGER.debug("[Goblin Fix] Caught invalid Clothing ID: {}, defaulting to 0", id);

            Object[] values = getEnumConstants();
            if (values != null && values.length > 0) {
                cir.setReturnValue(values[0]);
            } else {
                LOGGER.warn("[Goblin Fix] Could not resolve Clothing enum; returning null for invalid id {}", id);
                cir.setReturnValue(null);
            }
            cir.cancel();
        }
    }

    private static Object[] getEnumConstants() {
        Object[] cached = cachedEnumConstants;
        if (cached != null) {
            return cached;
        }
        try {
            Class<?> clothingClass = Class.forName(CLOTHING_CLASS);
            Object[] values = clothingClass.getEnumConstants();
            if (values != null && values.length > 0) {
                cachedEnumConstants = values;
                return values;
            }
        } catch (ClassNotFoundException e) {
            LOGGER.debug("Tensura Clothing class not yet available: {}", e.getMessage());
        }
        return null;
    }
}
