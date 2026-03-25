package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class StringArgument extends Argument<String> {

    private final StringArgumentType type;

    private StringArgument(String name, StringArgumentType type) {
        super(name);
        this.type = type;
    }

    public static StringArgument word(String name) {
        return new StringArgument(name, StringArgumentType.word());
    }

    public static StringArgument phrase(String name) {
        return new StringArgument(name, StringArgumentType.string());
    }

    public static StringArgument greedy(String name) {
        return new StringArgument(name, StringArgumentType.greedyString());
    }

    @Override
    public @NotNull ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        RequiredArgumentBuilder<CommandSender, ?> builder =
                RequiredArgumentBuilder.argument(getName(), type);

        builder.executes(context -> 1);
        return builder;
    }

    @Override
    public @NotNull Optional<String> getFromContext(@NotNull CommandContext<CommandSender> context) {
        try {
            return Optional.ofNullable(context.getArgument(getName(), String.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }
}