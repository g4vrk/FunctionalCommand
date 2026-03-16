package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChoiceArgument extends Argument<CommandSender> {

    private final List<String> options;

    public ChoiceArgument(String name, @NotNull List<String> options) {
        super(name);
        this.options = options;
    }

    @Override
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        RequiredArgumentBuilder<CommandSender, String> builder =
                RequiredArgumentBuilder.argument(getName(), StringArgumentType.word());

        builder.suggests((context, suggestionsBuilder) -> suggest(suggestionsBuilder));

        return builder;
    }

    private CompletableFuture<Suggestions> suggest(SuggestionsBuilder builder) {

        String remaining = builder.getRemaining().toLowerCase();

        for (String option : options) {
            if (option.toLowerCase().startsWith(remaining)) {
                builder.suggest(option);
            }
        }

        return builder.buildFuture();
    }
}