package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LiteralArgument extends AbstractArgument<String, LiteralArgument> {

    private final List<String> aliases = new ObjectArrayList<>();

    public LiteralArgument(
            @NotNull String name
    ) {
        super(name);
    }

    public LiteralArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command
    ) {
        super(name, command);
    }

    public LiteralArgument(
            @NotNull String name,
            @NotNull Predicate<CommandSender> requirement
    ) {
        super(name, requirement);
    }

    public LiteralArgument(
            @NotNull String name,
            @NotNull Command<CommandSender> command,
            @NotNull Predicate<CommandSender> requirement
    ) {
        super(name, command, requirement);
    }

    public @NotNull LiteralArgument alias(final @NotNull String alias) {
        this.aliases.add(alias);
        return getThis();
    }

    public @NotNull LiteralArgument aliases(final @NotNull Collection<String> aliases) {
        this.aliases.addAll(aliases);
        return getThis();
    }

    public @NotNull LiteralArgument clearAliases() {
        this.aliases.clear();
        return getThis();
    }

    public @NotNull List<String> getAliases() {
        return aliases;
    }

    @Override
    public @NotNull CommandNode<CommandSender> buildNode() {
        final LiteralArgumentBuilder<CommandSender> argumentBuilder = LiteralArgumentBuilder.literal(getName());

        argumentBuilder.requires(getRequirement());
        argumentBuilder.executes(getCommand());

        for (CommandNode<CommandSender> child : getChildren()) {
            argumentBuilder.then(child);
        }

        return argumentBuilder.build();
    }

    @Override
    public @NotNull List<CommandNode<CommandSender>> buildAliases(@NotNull CommandNode<CommandSender> mainNode) {
        List<CommandNode<CommandSender>> nodes = new ObjectArrayList<>();

        for (String alias : getAliases()) {
            nodes.add(
                    LiteralArgumentBuilder.<CommandSender>literal(alias)
                            .requires(getRequirement())
                            .redirect(mainNode)
                            .build()
            );
        }

        return nodes;
    }

    @Override
    public @NotNull LiteralArgument getThis() {
        return this;
    }

    @Override
    public @NotNull Optional<String> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        try {
            return Optional.ofNullable(context.getArgument(getName(), String.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }
}
