package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FixedArgument extends Argument<String> {

    public FixedArgument(String name) {
        super(name);
    }

    @Override
    public @NotNull ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        final LiteralArgumentBuilder<CommandSender> builder =
                LiteralArgumentBuilder.literal(getName());

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