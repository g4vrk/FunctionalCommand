package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.RequiredArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class BooleanArgument extends RequiredArgument<Boolean> {

    private static final String TRUE_STRING = "true";
    private static final String FALSE_STRING = "false";

    private static final char TRUE_FIRST_CHAR = 't';
    private static final char FALSE_FIRST_CHAR = 'f';

    public BooleanArgument(
            @NotNull String name
    ) {
        super(name, BoolArgumentType.bool());
        suggests((context, builder) -> suggest(builder));
    }

    @Override
    public @NotNull Optional<Boolean> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        try {
            return Optional.ofNullable(context.getArgument(getName(), Boolean.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    private @NotNull CompletableFuture<Suggestions> suggest(final @NotNull SuggestionsBuilder suggestionsBuilder) {
        final String remaining = suggestionsBuilder.getRemainingLowerCase();

        if (remaining.isEmpty()) {
            suggestionsBuilder.suggest(TRUE_STRING);
            suggestionsBuilder.suggest(FALSE_STRING);

            return suggestionsBuilder.buildFuture();
        }

        switch (remaining.charAt(0)) {
            case TRUE_FIRST_CHAR -> suggestionsBuilder.suggest(TRUE_STRING);
            case FALSE_FIRST_CHAR -> suggestionsBuilder.suggest(FALSE_STRING);
            default -> {
                suggestionsBuilder.suggest(TRUE_STRING);
                suggestionsBuilder.suggest(FALSE_STRING);
            }
        }

        return suggestionsBuilder.buildFuture();
    }
}