package chaos.frost;

import chaos.frost.block.ModBlocks;
import chaos.frost.config.UniversalConfig;
import chaos.frost.data_attachment.ModDataAttachments;
import chaos.frost.networking.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewFrostwalker implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("frost");
	public static final String MOD_ID = "frost";
	public static UniversalConfig CONFIG = UniversalConfig.createOrLoad();
	@Override
	public void onInitialize() {
		LOGGER.info("Loading Universal frostwalker mod");

        ModNetworking.init();

		if (!CONFIG.serverSideOnly) {
			ModBlocks.registerModBlocks();
		}
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

	public static boolean hasFrostWalker(LivingEntity entity, World world) {
        if (!entity.getAttachedOrCreate(ModDataAttachments.IS_FROSTWALKER_ENABLED)) {
            return false;
        }
		final RegistryKey<Registry<Enchantment>> enchantmentRegistry = RegistryKeys.ENCHANTMENT;
		return EnchantmentHelper.getEquipmentLevel(world.getRegistryManager().getOrThrow(enchantmentRegistry).getEntry(Enchantments.FROST_WALKER.getValue()).orElseThrow(), entity) > 0;
	}
}
