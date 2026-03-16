package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import org.bukkit.command.CommandSender;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class StringArgument extends Argument<CommandSender> {

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
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        return argument(getName(), type);
    }
}