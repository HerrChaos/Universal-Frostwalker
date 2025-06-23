package chaos.better_frost_walker.commands;

import blue.endless.jankson.Comment;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;

import static chaos.better_frost_walker.BetterFrostWalkerMain.MOD_ID;
import static chaos.better_frost_walker.BetterFrostWalkerMain.config;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModServerCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(ModServerCommands::addCommands);
    }

    // TODO: change this to /better-frost-walker config set {optionName} {value} as part of refactor
    private static void addCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        final LiteralArgumentBuilder<ServerCommandSource> setCommand = literal("set");
        final LiteralArgumentBuilder<ServerCommandSource> getCommand = literal("get"); // todo

        addBooleanOption(setCommand, "generateIceWhileStill", true);
        addBooleanOption(setCommand, "standingOnPowderedSnow", false);
        addBooleanOption(setCommand, "serverSideOnly", true);
        addBooleanOption(setCommand, "noIceFallDamage", false);
        addBooleanOption(setCommand, "meltIceInTheDark", false);

        dispatcher.register(literal(MOD_ID).then(literal("config").then(setCommand)));
        dispatcher.register(literal(MOD_ID).then(literal("config").then(getCommand))); // todo

        /*
        LiteralArgumentBuilder<ServerCommandSource> frostConfig = literal("frostConfig");

        frostConfig.then(literal("set-serverSideOnly")
                   .then(argument("isServerSide", BoolArgumentType.bool())
                   .executes(context -> {
                       final boolean isServerSide = BoolArgumentType.getBool(context, "isServerSide");
                       BetterFrostWalkerMain.config.get().serverSideOnlyAfterRestart = isServerSide;
                       BetterFrostWalkerMain.config.get().saveToFile();
                       context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                       context.getSource().sendMessage(Text.of("Set isServerSide to: " + isServerSide));
                       return 1;
                   })));

        frostConfig.then(literal("set-canStandOnPowderedSnow")
                .then(argument("canStandOnPowderedSnow", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean canStandOnPowderedSnow = BoolArgumentType.getBool(context, "canStandOnPowderedSnow");
                            BetterFrostWalkerMain.config.get().standingOnPowderedSnow = canStandOnPowderedSnow;
                            BetterFrostWalkerMain.config.get().saveToFile();
                            context.getSource().sendMessage(Text.of("Set canStandOnPowderedSnow to: " + canStandOnPowderedSnow));
                            return 1;
                        })));

        frostConfig.then(literal("set-noIceFallDamage")
                .then(argument("noIceFallDamage", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean noIceFallDamage = BoolArgumentType.getBool(context, "noIceFallDamage");
                            BetterFrostWalkerMain.config.get().noIceFallDamage = noIceFallDamage;
                            BetterFrostWalkerMain.config.get().saveToFile();
                            context.getSource().sendMessage(Text.of("Set noIceFallDamage to: " + noIceFallDamage));
                            return 1;
                        })));

        frostConfig.then(literal("set-meltIceInDark")
                .then(argument("meltIceInDark", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean meltIceInDark = BoolArgumentType.getBool(context, "meltIceInDark");
                            BetterFrostWalkerMain.config.get().meltIceInTheDark = meltIceInDark;
                            BetterFrostWalkerMain.config.get().saveToFile();
                            context.getSource().sendMessage(Text.of("Set meltIceInDark to: " + meltIceInDark));
                            return 1;
                        })));

        frostConfig.then(literal("set-generateIceWhileStandingStill")
                .then(argument("generateIceWhileStandingStill", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean generateIceWhileStandingStill = BoolArgumentType.getBool(context, "generateIceWhileStandingStill");
                            BetterFrostWalkerMain.config.get().generateIceWhileStillAfterRestart = generateIceWhileStandingStill;
                            BetterFrostWalkerMain.config.get().saveToFile();
                            context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                            context.getSource().sendMessage(Text.of("Set generateIceWhileStandingStill to: " + generateIceWhileStandingStill));
                            return 1;
                        })));

        frostConfig.then(literal("set-maxLevel")
                .then(argument("maxLevel", IntegerArgumentType.integer(1, 255))
                        .executes(context -> {
                            final int maxLevel = IntegerArgumentType.getInteger(context, "maxLevel");
                            BetterFrostWalkerMain.config.get().maxLevel = maxLevel;
                            BetterFrostWalkerMain.config.get().saveToFile();
                            context.getSource().sendMessage(Text.of("Set maxLevel to: " + maxLevel));
                            context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                            return 1;
                        })));

        dispatcher.register(frostConfig);
         */
    }

    private static void addBooleanOption(final LiteralArgumentBuilder<ServerCommandSource> setCommand, final String optionName, final boolean requiresRestart) {
        setCommand.then(
                literal(optionName)
                        .then(
                                argument("newValue", BoolArgumentType.bool())
                                        .executes(context -> {
                                            final boolean newValue = BoolArgumentType.getBool(context, "newValue");

                                            try {
                                                config.getConfigClass().getDeclaredField(optionName).set(config.get(), newValue);
                                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                                throw new RuntimeException(e);
                                            }
                                            ConfigManager.INSTANCE.save(config);

                                            if (requiresRestart) context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                                            context.getSource().sendMessage(Text.of("Set '%s' to '%s'".formatted(optionName, newValue)));

                                            return 1;
                                        })
                        )
                );
    }
}
