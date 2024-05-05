package chaos.frost.commands;

import chaos.frost.NewFrostwalker;
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

    private static void addCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        LiteralArgumentBuilder<ServerCommandSource> frostConfig = literal("frostConfig");

        frostConfig.then(literal("set-serverSideOnly")
                   .then(argument("isServerSide", BoolArgumentType.bool())
                   .executes(context -> {
                       final boolean isServerSide = BoolArgumentType.getBool(context, "isServerSide");
                       NewFrostwalker.CONFIG.serverSideOnlyAfterRestart = isServerSide;
                       NewFrostwalker.CONFIG.saveToFile();
                       context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                       context.getSource().sendMessage(Text.of("Set isServerSide to: " + isServerSide));
                       return 1;
                   })));

        frostConfig.then(literal("set-canStandOnPowderedSnow")
                .then(argument("canStandOnPowderedSnow", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean canStandOnPowderedSnow = BoolArgumentType.getBool(context, "canStandOnPowderedSnow");
                            NewFrostwalker.CONFIG.standingOnPowderedSnow = canStandOnPowderedSnow;
                            NewFrostwalker.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set canStandOnPowderedSnow to: " + canStandOnPowderedSnow));
                            return 1;
                        })));

        frostConfig.then(literal("set-generateIceWhileStandingStill")
                .then(argument("generateIceWhileStandingStill", BoolArgumentType.bool())
                        .executes(context -> {
                            final boolean generateIceWhileStandingStill = BoolArgumentType.getBool(context, "generateIceWhileStandingStill");
                            NewFrostwalker.CONFIG.generateIceWhileStill = generateIceWhileStandingStill;
                            NewFrostwalker.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set generateIceWhileStandingStill to: " + generateIceWhileStandingStill));
                            return 1;
                        })));

        frostConfig.then(literal("set-maxLevel")
                .then(argument("maxLevel", IntegerArgumentType.integer(1, 255))
                        .executes(context -> {
                            final int maxLevel = IntegerArgumentType.getInteger(context, "maxLevel");
                            NewFrostwalker.CONFIG.maxLevel = maxLevel;
                            NewFrostwalker.CONFIG.saveToFile();
                            context.getSource().sendMessage(Text.of("Set maxLevel to: " + maxLevel));
                            context.getSource().sendMessage(Text.of("THIS WILL REQUIRE A RESTART!!"));
                            return 1;
                        })));

        dispatcher.register(frostConfig);
    }

}
