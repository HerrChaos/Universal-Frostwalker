package chaos.frost.commands;

import chaos.frost.NewFrostwalker;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModServerCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(ModServerCommands::addCommands);
    }

    private static void addCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        LiteralArgumentBuilder<ServerCommandSource> frostConfig = literal("frostConfig");

        frostConfig.then(literal("setServerSideOnly"))
                   .then(argument("isServerSide", BoolArgumentType.bool()))
                   .executes(context -> {
                       final boolean isServerSide = BoolArgumentType.getBool(context, "isServerSide");
                       NewFrostwalker.CONFIG.serverSideOnly = isServerSide;
                       return 1;
                   });

        dispatcher.register(frostConfig);
    }

}
