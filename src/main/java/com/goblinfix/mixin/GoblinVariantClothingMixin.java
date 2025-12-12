package com.goblinfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to fix ArrayIndexOutOfBoundsException in GoblinVariant$Clothing.byId()
 * when the Clothing ID is -1 or otherwise out of bounds (valid range: 0-5)
 */
@Mixin(targets = "com.github.manasmods.tensura.entity.variant.GoblinVariant$Clothing", remap = false)
public class GoblinVariantClothingMixin {

    /**
     * Intercept the byId method and clamp invalid IDs to valid range
     */
    @Inject(method = "byId", at = @At("HEAD"), cancellable = true)
    private static void fixInvalidClothingId(int id, CallbackInfoReturnable<Object> cir) {
        // Valid clothing IDs are 0-5 (array length 6)
        if (id < 0 || id > 5) {
            // Log the fix
            System.out.println("[Goblin Fix] Caught invalid Clothing ID: " + id + ", defaulting to 0");
            
            // Get the enum values and return the first one (index 0)
            try {
                Class<?> clothingClass = Class.forName("com.github.manasmods.tensura.entity.variant.GoblinVariant$Clothing");
                Object[] values = clothingClass.getEnumConstants();
                if (values != null && values.length > 0) {
                    cir.setReturnValue(values[0]);
                }
            } catch (ClassNotFoundException e) {
                // Fallback: just return null and let it handle
            }
        }
    }
}

