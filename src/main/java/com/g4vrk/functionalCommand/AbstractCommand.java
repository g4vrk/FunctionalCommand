package com.g4vrk.functionalCommand;

import com.g4vrk.functionalCommand.argument.Argument;
import com.g4vrk.functionalCommand.registry.CommandRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class AbstractCommand extends BaseCommand {

    private final LiteralArgumentBuilder<CommandSender> root;

    private volatile CommandDispatcher<CommandSender> dispatcher;

    protected AbstractCommand(
            @NotNull String name,
            @NotNull Predicate<CommandSender> requirement,
            @NotNull Command<CommandSender> command
    ) {
        super(name);
        this.root = LiteralArgumentBuilder.<CommandSender>literal(name)
                .executes(command)
                .requires(requirement);
    }

    public AbstractCommand(
            @NotNull String name,
            @NotNull Predicate<CommandSender> requirement
    ) {
        this(name, requirement, ctx -> 1);
    }

    public AbstractCommand(@NotNull String name) {
        this(name, s -> true);
    }

    protected AbstractCommand then(@NotNull Argument<?> argument) {
        return then(argument.argumentBuilder());
    }

    protected AbstractCommand then(@NotNull ArgumentBuilder<CommandSender, ?> node) {
        root.then(node);
        dispatcher = null;
        return this;
    }

    protected AbstractCommand requires(@NotNull Predicate<CommandSender> requirement) {
        root.requires(requirement);
        dispatcher = null;
        return this;
    }

    protected AbstractCommand executes(@NotNull Command<CommandSender> command) {
        root.executes(command);
        dispatcher = null;
        return this;
    }

    protected LiteralArgumentBuilder<CommandSender> getRoot() {
        return root;
    }

    private CommandDispatcher<CommandSender> getDispatcher() {

        CommandDispatcher<CommandSender> local = dispatcher;

        if (local == null) {
            synchronized (this) {

                local = dispatcher;

                if (local == null) {
                    local = new CommandDispatcher<>();
                    local.register(root);

                    dispatcher = local;
                }
            }
        }

        return local;
    }

    @Override
    public boolean execute(
            @NotNull CommandSender sender,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        final String input;

        if (args.length == 0) {
            input = alias;
        } else {
            input = alias + " " + String.join(" ", args);
        }

        try {
            getDispatcher().execute(input, sender);
        } catch (final CommandSyntaxException ex) {
            sender.sendMessage(ex.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        try {

            final String input = getName() + (args.length == 0 ? "" : " " + String.join(" ", args));

            final ParseResults<CommandSender> parse =
                    getDispatcher().parse(input, sender);

            final CompletableFuture<Suggestions> future =
                    getDispatcher().getCompletionSuggestions(parse);

            final Suggestions suggestions = future.join();

            List<String> result = new ArrayList<>();
            suggestions.getList().forEach(s -> result.add(s.getText()));

            return result;

        } catch (Throwable t) {
            t.printStackTrace();
            return Collections.emptyList();
        }
    }

    protected LiteralArgumentBuilder<CommandSender> cloneForCommodore() {
        LiteralCommandNode<CommandSender> built = root.build();

        //noinspection unchecked
        return (LiteralArgumentBuilder<CommandSender>) cloneNode(built);
    }

    private ArgumentBuilder<CommandSender, ?> cloneNode(@NotNull CommandNode<CommandSender> node) {

        final ArgumentBuilder<CommandSender, ?> builder;

        if (node instanceof LiteralCommandNode<CommandSender> literal)
            builder = LiteralArgumentBuilder.literal(literal.getLiteral());

        else if (node instanceof ArgumentCommandNode<CommandSender, ?> arg)
            builder = RequiredArgumentBuilder.argument(arg.getName(), arg.getType());

        else
            throw new IllegalStateException("Unknown brigadier node: " + node);

        for (CommandNode<CommandSender> child : node.getChildren())
            builder.then(cloneNode(child));

        return builder;
    }

    public void register(@NotNull Plugin plugin, boolean override) {

        final CommandRegistry commandRegistry = CommandRegistry.of(plugin.getServer());

        if (override) {
            if (commandRegistry.getCommand(getName()).isEmpty()) {
                commandRegistry.register(plugin, this);
            } else {
                commandRegistry.override(getName(), this);
            }
        } else {
            commandRegistry.register(plugin, this);
        }

        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);
            commodore.register(this, cloneForCommodore());
        }
    }

    public void unregister(@NotNull Server server) {
        CommandRegistry.of(server).unregister(this);
    }
}