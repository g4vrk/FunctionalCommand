package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.CommandSender;

public class FixedArgument extends Argument<CommandSender> {

    public FixedArgument(String name) {
        super(name);
    }

    @Override
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        LiteralArgumentBuilder<CommandSender> builder =
                LiteralArgumentBuilder.literal(getName());

        builder.executes(context -> 1);
        return builder;
    }
}