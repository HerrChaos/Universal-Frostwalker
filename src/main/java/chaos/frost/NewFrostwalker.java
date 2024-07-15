package chaos.frost;

import chaos.frost.block.ModBlocks;
import chaos.frost.commands.ModServerCommands;
import chaos.frost.config.UniversalConfig;
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

public class NewFrostwalker implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("frost");
	public static final String MOD_ID = "frost";
	public static UniversalConfig CONFIG = UniversalConfig.createOrLoad();
	@Override
	public void onInitialize() {
		LOGGER.info("Loading Universal frostwalker mod");
		ModServerCommands.registerCommands();

		if (!CONFIG.serverSideOnly) {
			ModBlocks.registerModBlocks();
		}

		final String packName = String.format("%s_ice_still__%s_server_only", CONFIG.generateIceWhileStill, CONFIG.serverSideOnly);

		FabricLoader.getInstance().getModContainer(MOD_ID)
				.map(container -> ResourceManagerHelper.registerBuiltinResourcePack(id(packName),
						container, Text.literal("Better Frost Walker"), ResourcePackActivationType.ALWAYS_ENABLED))
				.filter(success -> !success).ifPresent(success -> LOGGER.warn("Could not register built-in data pack."));
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

	public static boolean hasFrostWalker(LivingEntity entity, World world) {
		final RegistryKey<Registry<Enchantment>> enchantmentRegistry = RegistryKeys.ENCHANTMENT;
		return EnchantmentHelper.getEquipmentLevel(world.getRegistryManager().get(enchantmentRegistry).getEntry(RegistryKey.of(enchantmentRegistry, Enchantments.FROST_WALKER.getValue())).orElseThrow(), entity) > 0;
	}
}