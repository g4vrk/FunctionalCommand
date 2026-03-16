package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import org.bukkit.command.CommandSender;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class StringArgument extends Argument<CommandSender> {

    public StringArgument(String name) {
        super(name);
    }

    @Override
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        return argument(getName(), StringArgumentType.string());
    }
}