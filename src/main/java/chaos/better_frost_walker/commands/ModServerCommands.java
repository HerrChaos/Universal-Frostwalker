package chaos.better_frost_walker.commands;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModServerCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(ModServerCommands::addCommands);
    }

    // TODO: change this to /better-frost-walker config set {optionName} {value} as part of refactor
    private static void addCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        LiteralArgumentBuilder<ServerCommandSource> frostConfig = literal("frostConfig");

        frostConfig.then(literal("set-serverSideOnly")
                   .then(argument("isServerSide", BoolArgumentType.bool())
                   .executes(context -> {
                       final boolean isServerSide = BoolArgumentType.getBool(context, "isServerSide");
                       BetterFrostWalkerMain.CONFIG.serverSideOnlyAfterRestart = isServerSide;
                       BetterFrostWalkerMain.CONFIG.saveToFile();
                       context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                       context.getSource().sendMessage(Text.of("Set isServerSide to: " + isServerSide));
                       return 1;
                   })));

        frostConfig.then(literal("set-canStandOnPowderedSnow")
                .then(argument("canStandOnPowderedSnow", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean canStandOnPowderedSnow = BoolArgumentType.getBool(context, "canStandOnPowderedSnow");
                            BetterFrostWalkerMain.CONFIG.standingOnPowderedSnow = canStandOnPowderedSnow;
                            BetterFrostWalkerMain.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set canStandOnPowderedSnow to: " + canStandOnPowderedSnow));
                            return 1;
                        })));

        frostConfig.then(literal("set-noIceFallDamage")
                .then(argument("noIceFallDamage", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean noIceFallDamage = BoolArgumentType.getBool(context, "noIceFallDamage");
                            BetterFrostWalkerMain.CONFIG.noIceFallDamage = noIceFallDamage;
                            BetterFrostWalkerMain.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set noIceFallDamage to: " + noIceFallDamage));
                            return 1;
                        })));

        frostConfig.then(literal("set-meltIceInDark")
                .then(argument("meltIceInDark", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean meltIceInDark = BoolArgumentType.getBool(context, "meltIceInDark");
                            BetterFrostWalkerMain.CONFIG.meltIceInTheDark = meltIceInDark;
                            BetterFrostWalkerMain.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set meltIceInDark to: " + meltIceInDark));
                            return 1;
                        })));

        frostConfig.then(literal("set-generateIceWhileStandingStill")
                .then(argument("generateIceWhileStandingStill", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean generateIceWhileStandingStill = BoolArgumentType.getBool(context, "generateIceWhileStandingStill");
                            BetterFrostWalkerMain.CONFIG.generateIceWhileStillAfterRestart = generateIceWhileStandingStill;
                            BetterFrostWalkerMain.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                            context.getSource().sendMessage(Text.of("Set generateIceWhileStandingStill to: " + generateIceWhileStandingStill));
                            return 1;
                        })));

        frostConfig.then(literal("set-maxLevel")
                .then(argument("maxLevel", IntegerArgumentType.integer(1, 255))
                        .executes(context -> {
                            final int maxLevel = IntegerArgumentType.getInteger(context, "maxLevel");
                            BetterFrostWalkerMain.CONFIG.maxLevel = maxLevel;
                            BetterFrostWalkerMain.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set maxLevel to: " + maxLevel));
                            context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                            return 1;
                        })));

        dispatcher.register(frostConfig);
    }

}
