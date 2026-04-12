package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.RequiredArgument;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DoubleArgument extends RequiredArgument<Double> {

    public DoubleArgument(
            @NotNull String name
    ) {
        super(name, DoubleArgumentType.doubleArg());
        suggests((context, builder) -> suggest(builder));
    }

    public DoubleArgument(
            @NotNull String name,
            double min
    ) {
        super(name, DoubleArgumentType.doubleArg(min));
        suggests((context, builder) -> suggest(builder));
    }

    public DoubleArgument(
            @NotNull String name,
            double min,
            double max
    ) {
        super(name, DoubleArgumentType.doubleArg(min, max));
        suggests((context, builder) -> suggest(builder));
    }

    @Override
    public @NotNull Optional<Double> parse(@NotNull CommandContext<CommandSender> context) {
        try {
            return Optional.ofNullable(context.getArgument(getName(), Double.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    private @NotNull CompletableFuture<Suggestions> suggest(final @NotNull SuggestionsBuilder suggestionsBuilder) {
        final String input = suggestionsBuilder.getRemaining();

        if (input.isEmpty()) {
            suggestionsBuilder.suggest("0.0");
            return suggestionsBuilder.buildFuture();
        }

        return suggestionsBuilder.buildFuture();
    }
}