package chaos.better_frost_walker;

import chaos.better_frost_walker.block.ModBlocks;
import chaos.better_frost_walker.commands.ModServerCommands;
import chaos.better_frost_walker.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterFrostWalkerMain implements ModInitializer {
	public static final String MOD_ID = "better-frost-walker";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ModConfig CONFIG = ModConfig.createOrLoad();

	@Override
	public void onInitialize() {
		LOGGER.info("Loading Better Frost Walker");
		ModServerCommands.registerCommands();

		if (!CONFIG.serverSideOnly) {
			ModBlocks.register();
		}

		final String packName = String.format("%s_ice_still__%s_server_only", CONFIG.generateIceWhileStill, CONFIG.serverSideOnly);

		FabricLoader.getInstance().getModContainer(MOD_ID)
				.map(container -> ResourceManagerHelper.registerBuiltinResourcePack(id(packName),
						container, Text.literal("Better Frost Walker"), ResourcePackActivationType.ALWAYS_ENABLED))
				.ifPresent(success -> {
					if (success) LOGGER.info("Successfully enabled built-in datapack '{}' for '{}'", packName, MOD_ID);
					else LOGGER.error("Failed to enable built-in datapack '{}' for '{}'", packName, MOD_ID);
				});
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

	public static boolean hasFrostWalker(LivingEntity entity, World world) {
		final RegistryKey<Registry<Enchantment>> enchantmentRegistry = RegistryKeys.ENCHANTMENT;
		return EnchantmentHelper.getEquipmentLevel(world.getRegistryManager().getOrThrow(enchantmentRegistry).getEntry(Enchantments.FROST_WALKER.getValue()).orElseThrow(), entity) > 0;
	}
}
