package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ChoiceArgument extends Argument<String> {

    private final List<String> options;

    public ChoiceArgument(String name, @NotNull List<String> options) {
        super(name);
        this.options = options;
    }

    @Override
    public @NotNull ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        final RequiredArgumentBuilder<CommandSender, String> builder =
                RequiredArgumentBuilder.argument(getName(), StringArgumentType.word());

        builder.executes(context -> 1);
        builder.suggests((ctx, sb) -> suggest(sb));
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

    private CompletableFuture<Suggestions> suggest(SuggestionsBuilder sb) {
        String remaining = sb.getRemaining().toLowerCase();

        for (String option : options) {
            if (option.toLowerCase().startsWith(remaining)) {
                sb.suggest(option);
            }
        }

        return sb.buildFuture();
    }
}