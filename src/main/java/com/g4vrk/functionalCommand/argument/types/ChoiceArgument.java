package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.bukkit.command.CommandSender;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class WordArgument extends Argument<CommandSender> {

    public WordArgument(String name) {
        super(name);
    }

    @Override
    public RequiredArgumentBuilder<CommandSender, ?> argumentBuilder() {
        return argument(getName(), StringArgumentType.word());
    }
}