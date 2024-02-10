package chaos.frost;

import chaos.frost.block.ModBlocks;
import chaos.frost.config.MyConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewFrostwalker implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("frost");
	public static final String MOD_ID = "frost";
	public static MyConfig CONFIG = MyConfig.createAndLoad();
	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		LOGGER.info("Hello Fabric world!");
	}
}