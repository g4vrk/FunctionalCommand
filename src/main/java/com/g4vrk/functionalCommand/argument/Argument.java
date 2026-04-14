package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Argument<T, R extends Argument<T, R>>  {
    @NotNull R getThis();

    @NotNull String getName();

    @NotNull Command<CommandSender> getCommand();
    @NotNull Predicate<CommandSender> getRequirement();

    @NotNull List<CommandNode<CommandSender>> getChildren();

    @NotNull R executes(@NotNull Command<CommandSender> command);
    @NotNull R requires(@NotNull Predicate<CommandSender> requirement);

    @NotNull R then(@NotNull ArgumentBuilder<CommandSender, ?> node);
    @NotNull R then(@NotNull CommandNode<CommandSender> node);

    @NotNull Optional<T> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException;

    @NotNull CommandNode<CommandSender> buildNode();
    @NotNull List<CommandNode<CommandSender>> buildAliases(@NotNull CommandNode<CommandSender> mainNode);
}
