package com.g4vrk.functionalCommand.argument;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class RequiredArgument<T> extends AbstractArgument<T, RequiredArgument<T>> {

    private final ArgumentType<?> type;
    private SuggestionProvider<CommandSender> suggestionsProvider;

    public RequiredArgument(
            @NotNull String name,
            @NotNull ArgumentType<?> type
    ) {
        super(name);
        this.type = type;
    }

    public RequiredArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command,
            @NotNull ArgumentType<?> type
    ) {
        super(name, command);
        this.type = type;
    }

    public RequiredArgument(
            @NotNull String name,
            @NotNull Predicate<CommandSender> requirement,
            @NotNull ArgumentType<?> type
    ) {
        super(name, requirement);
        this.type = type;
    }

    public RequiredArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command,
            @NotNull Predicate<CommandSender> requirement,
            @NotNull ArgumentType<?> type
    ) {
        super(name, command, requirement);
        this.type = type;
    }

    public RequiredArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command,
            @NotNull Predicate<CommandSender> requirement,
            @NotNull SuggestionProvider<CommandSender> suggestionsProvider,
            @NotNull ArgumentType<?> type
    ) {
        super(name, command, requirement);
        this.suggestionsProvider = suggestionsProvider;
        this.type = type;
    }

    public @Nullable RequiredArgument<T> suggests(final @NotNull SuggestionProvider<CommandSender> suggestionsProvider) {
        this.suggestionsProvider = suggestionsProvider;
        return getThis();
    }

    @Override
    public @NotNull RequiredArgument<T> getThis() {
        return this;
    }

    public @NotNull ArgumentType<?> getType() {
        return type;
    }

    public @Nullable SuggestionProvider<CommandSender> getSuggestionsProvider() {
        return suggestionsProvider;
    }

    @Override
    public @NotNull CommandNode<CommandSender> buildNode() {
        final RequiredArgumentBuilder<CommandSender, ?> argumentBuilder = RequiredArgumentBuilder.argument(getName(), type);

        argumentBuilder.requires(getRequirement());
        argumentBuilder.executes(getCommand());

        if (suggestionsProvider != null) {
            argumentBuilder.suggests(suggestionsProvider);
        }

        for (CommandNode<CommandSender> child : getChildren()) {
            argumentBuilder.then(child);
        }

        return argumentBuilder.build();
    }

    @Override
    public @NotNull List<CommandNode<CommandSender>> buildAliases(@NotNull CommandNode<CommandSender> mainNode) {
        return ImmutableList.of();
    }

    @Override
    public abstract @NotNull Optional<T> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException;
}
