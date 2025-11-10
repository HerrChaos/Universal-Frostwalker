package chaos.frost.mixin;

import chaos.frost.access.ReplaceDiskEnchantmentEffectAccess;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    /*
    @Shadow public abstract <T> List<T> getEffect(ComponentType<List<T>> type);

    @Inject(
            method = "definition(Lnet/minecraft/registry/entry/RegistryEntryList;IILnet/minecraft/enchantment/Enchantment$Cost;Lnet/minecraft/enchantment/Enchantment$Cost;I[Lnet/minecraft/component/type/AttributeModifierSlot;)Lnet/minecraft/enchantment/Enchantment$Definition;",
            at = @At("TAIL")
    )
    private void betterfrostwalker$setEnchantmentEffectsFromFrostWalker(RegistryEntryList<Item> supportedItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, AttributeModifierSlot[] slots, CallbackInfoReturnable<Enchantment.Definition> cir) {
        for (EnchantmentEffectEntry<EnchantmentLocationBasedEffect> locationBasedEffect : getEffect(EnchantmentEffectComponentTypes.LOCATION_CHANGED)) {
            betterfrostwalker$setEnchantmentEffectFromFrostWalker((Enchantment) (Object) this, locationBasedEffect.effect());
        }

        for (EnchantmentEffectEntry<EnchantmentEntityEffect> locationBasedEffect : getEffect(EnchantmentEffectComponentTypes.TICK)) {
            betterfrostwalker$setEnchantmentEffectFromFrostWalker((Enchantment) (Object) this, locationBasedEffect.effect());
        }
    }

    //@WrapOperation(
    //        method = "applyLocationBasedEffects",
    //        at = @At(
    //                value = "INVOKE",
    //                target = "Lnet/minecraft/enchantment/effect/EnchantmentLocationBasedEffect;apply(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Z)V"
    //        )
    //)
    //private void betterfrostwalker$setLocationBasedEnchantmentEffectFromFrostWalker(EnchantmentLocationBasedEffect instance, ServerWorld serverWorld, int i, EnchantmentEffectContext enchantmentEffectContext, Entity entity, Vec3d vec3d, boolean b, Operation<Void> original) {
    //    betterfrostwalker$setEnchantmentEffectFromFrostWalker((Enchantment) (Object) this, instance);

    //    original.call(instance, serverWorld, i, enchantmentEffectContext, entity, vec3d, b);
    //}

    //@WrapOperation(
    //        method = "method_60045",
    //        at = @At(
    //                value = "INVOKE",
    //                target = "Lnet/minecraft/enchantment/effect/EnchantmentEntityEffect;apply(Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/enchantment/EnchantmentEffectContext;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;)V"
    //        )
    //)
    //private static void betterfrostwalker$setTickEnchantmentEffectFromFrostWalker(EnchantmentEntityEffect instance, ServerWorld serverWorld, int i, EnchantmentEffectContext enchantmentEffectContext, Entity entity, Vec3d vec3d, Operation<Void> original) {
    //    betterfrostwalker$setEnchantmentEffectFromFrostWalker((Enchantment) (Object) this, instance);
    //}

    @Unique
    private static void betterfrostwalker$setEnchantmentEffectFromFrostWalker(Enchantment thiz, EnchantmentLocationBasedEffect effect) {
        if (!(thiz.description().getContent() instanceof TranslatableTextContent translatableTextContent)) return;

        if (!"enchantment.minecraft.frost_walker".equals(translatableTextContent.getKey())) return;
        if (!(effect instanceof ReplaceDiskEnchantmentEffectAccess effectAccess)) return;

        effectAccess.betterfrostwalker$setIsFrostWalker();
    }

     */
}
