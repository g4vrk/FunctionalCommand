package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.RequiredArgument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class IntArgument extends RequiredArgument<Integer> {

    public IntArgument(
            @NotNull String name
    ) {
        super(name, IntegerArgumentType.integer());
        suggests((context, builder) -> suggest(builder));
    }

    public IntArgument(
            @NotNull String name,
            int min
    ) {
        super(name, IntegerArgumentType.integer(min));
        suggests((context, builder) -> suggest(builder));
    }

    public IntArgument(
            @NotNull String name,
            int min,
            int max
    ) {
        super(name, IntegerArgumentType.integer(min, max));
        suggests((context, builder) -> suggest(builder));
    }

    @Override
    public @NotNull Optional<Integer> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        try {
            return Optional.ofNullable(context.getArgument(getName(), Integer.class));
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