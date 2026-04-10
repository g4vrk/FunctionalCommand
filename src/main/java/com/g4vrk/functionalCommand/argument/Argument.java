package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
public abstract class Argument<T> {
    private final String name;
    private Command<CommandSender> command;
    private Predicate<CommandSender> requirement;

    protected Argument(
            @NotNull String name
    ) {
        this(name, (ctx) -> Command.SINGLE_SUCCESS, (s) -> true);
    }

    protected Argument(
            @NotNull String name,
            @NotNull Command<CommandSender> command
    ) {
        this(name, command, (s) -> true);
    }

    protected Argument(
            @NotNull String name,
            @NotNull Predicate<CommandSender> requirement
    ) {
        this(name, (cxt) -> Command.SINGLE_SUCCESS, requirement);
    }

    protected Argument(
            @NotNull String name,
            @NotNull Command<CommandSender> command,
            @NotNull Predicate<CommandSender> requirement
    ) {
        this.name = name;
        this.command = command;
        this.requirement = requirement;
    }

    public @NotNull Argument<T> executes(final @NotNull Command<CommandSender> command) {
        this.command = command;
        return this;
    }

    public @NotNull Argument<T> requires(final @NotNull Predicate<CommandSender> requirement) {
        this.requirement = requirement;
        return this;
    }

    public @NotNull ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        final ArgumentBuilder<CommandSender, ?> argumentBuilder = LiteralArgumentBuilder.literal(getName());

        argumentBuilder.executes(command);
        argumentBuilder.requires(requirement);

        return argumentBuilder;
    }

    public abstract @NotNull Optional<T> getFromContext(@NotNull CommandContext<CommandSender> context);
}