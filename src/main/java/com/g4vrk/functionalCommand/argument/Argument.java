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

public interface Argument<T>  {
    @NotNull String getName();

    @NotNull Command<CommandSender> getCommand();
    @NotNull Predicate<CommandSender> getRequirement();

    @NotNull List<CommandNode<CommandSender>> getChildren();

    @NotNull AbstractArgument<T> executes(@NotNull Command<CommandSender> command);
    @NotNull AbstractArgument<T> requires(@NotNull Predicate<CommandSender> requirement);

    @NotNull AbstractArgument<T> then(@NotNull ArgumentBuilder<CommandSender, ?> node);
    @NotNull AbstractArgument<T> then(@NotNull CommandNode<CommandSender> node);

    @NotNull Optional<T> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException;

    @NotNull CommandNode<CommandSender> buildNode();
}
