package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.RequiredArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StringArgument extends RequiredArgument<String> {

    protected StringArgument(
            @NotNull String name,
            @NotNull StringArgumentType type
    ) {
        super(name, type);
    }

    public static StringArgument word(String name) {
        return new StringArgument(name, StringArgumentType.word());
    }

    public static StringArgument string(String name) {
        return new StringArgument(name, StringArgumentType.string());
    }

    public static StringArgument greedy(String name) {
        return new StringArgument(name, StringArgumentType.greedyString());
    }

    @Override
    public @NotNull Optional<String> parse(@NotNull CommandContext<CommandSender> context) {
        try {
            return Optional.ofNullable(context.getArgument(getName(), String.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }
}