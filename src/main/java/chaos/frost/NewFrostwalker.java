package chaos.frost;

import chaos.frost.block.ModBlocks;
import chaos.frost.commands.ModServerCommands;
import chaos.frost.config.UniversalConfig;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
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
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}