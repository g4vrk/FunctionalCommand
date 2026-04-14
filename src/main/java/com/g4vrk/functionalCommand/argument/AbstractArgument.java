package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractArgument<T, R extends AbstractArgument<T, R>> implements Argument<T, R> {

    private final String name;

    private Command<CommandSender> command;
    private Predicate<CommandSender> requirement;

    private final List<CommandNode<CommandSender>> arguments = new ObjectArrayList<>();

    protected AbstractArgument(
            @NotNull String name
    ) {
        this(name, (ctx) -> Command.SINGLE_SUCCESS, (s) -> true);
    }

    protected AbstractArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command
    ) {
        this(name, command, (s) -> true);
    }

    protected AbstractArgument(
            @NotNull String name,
            @NotNull Predicate<CommandSender> requirement
    ) {
        this(name, (cxt) -> Command.SINGLE_SUCCESS, requirement);
    }

    protected AbstractArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command,
            @NotNull Predicate<CommandSender> requirement
    ) {
        this.name = name;
        this.command = command;
        this.requirement = requirement;
    }

    @Override
    public @NotNull R executes(final @NotNull Command<CommandSender> command) {
        this.command = command;
        return getThis();
    }

    @Override
    public @NotNull R requires(final @NotNull Predicate<CommandSender> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    @Override
    public @NotNull R then(final @NotNull ArgumentBuilder<CommandSender, ?> node) {
        this.arguments.add(node.build());
        return getThis();
    }

    @Override
    public @NotNull R then(final @NotNull CommandNode<CommandSender> node) {
        this.arguments.add(node);
        return getThis();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull Command<CommandSender> getCommand() {
        return this.command;
    }

    @Override
    public @NotNull Predicate<CommandSender> getRequirement() {
        return this.requirement;
    }

    @Override
    public @NotNull List<CommandNode<CommandSender>> getChildren() {
        return this.arguments;
    }

    @Override
    public abstract @NotNull CommandNode<CommandSender> buildNode();

    @Override
    public abstract @NotNull Optional<T> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException;
}