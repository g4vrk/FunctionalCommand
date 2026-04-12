package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.RequiredArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ChoiceArgument extends RequiredArgument<String> {

    private final @NotNull List<String> options;

    public ChoiceArgument(
            @NotNull String name,
            @NotNull List<String> options
    ) {
        super(name, StringArgumentType.string());
        this.options = options;
    }

    @Override
    public @NotNull Optional<String> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        try {
            return Optional.ofNullable(context.getArgument(getName(), String.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    private @NotNull CompletableFuture<Suggestions> suggest(final @NotNull SuggestionsBuilder suggestionsBuilder) {
        final String remaining = suggestionsBuilder.getRemaining();

        for (final String option : options) {
            if (StringUtil.startsWithIgnoreCase(option, remaining)) {
                suggestionsBuilder.suggest(option);
            }
        }

        return suggestionsBuilder.buildFuture();
    }
}