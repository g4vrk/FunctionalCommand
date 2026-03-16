package com.g4vrk.functionalCommand;

import com.g4vrk.functionalCommand.argument.Argument;
import com.g4vrk.functionalCommand.registry.CommandRegistry;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommand extends Command {

    private final LiteralArgumentBuilder<CommandSender> root;

    private volatile CommandDispatcher<CommandSender> dispatcher;
    private volatile boolean treeDirty = true;

    protected AbstractCommand(@NotNull String name) {
        super(name);
        this.root = LiteralArgumentBuilder.<CommandSender>literal(name)
                .executes(ctx -> 1);
    }

    protected AbstractCommand then(@NotNull Argument<CommandSender> argument) {
        return then(argument.argumentBuilder());
    }

    protected AbstractCommand then(@NotNull ArgumentBuilder<CommandSender, ?> node) {
        root.then(node);
        treeDirty = true;
        return this;
    }

    protected AbstractCommand executes(@NotNull com.mojang.brigadier.Command<CommandSender> command) {
        root.executes(command);
        treeDirty = true;
        return this;
    }

    protected LiteralArgumentBuilder<CommandSender> root() {
        return root;
    }

    private CommandDispatcher<CommandSender> dispatcher() {

        CommandDispatcher<CommandSender> local = dispatcher;

        if (local == null || treeDirty) {
            synchronized (this) {

                local = dispatcher;

                if (local == null || treeDirty) {
                    local = new CommandDispatcher<>();
                    local.register(root);

                    dispatcher = local;
                    treeDirty = false;
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

        String input;

        if (args.length == 0) {
            input = alias;
        } else {
            input = alias + " " + String.join(" ", args);
        }

        try {
            dispatcher().execute(input, sender);
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

            String input = alias;
            if (args.length > 0)
                input += " " + String.join(" ", args);

            ParseResults<CommandSender> parse =
                    dispatcher().parse(input, sender);

            CompletableFuture<Suggestions> future =
                    dispatcher().getCompletionSuggestions(parse);

            Suggestions suggestions = future.get();

            List<String> result = new ArrayList<>();
            suggestions.getList().forEach(s -> result.add(s.getText()));

            return result;

        } catch (Throwable t) {
            return List.of();
        }
    }

    private LiteralArgumentBuilder<CommandSender> cloneForCommodore() {
        LiteralCommandNode<CommandSender> built = root.build();

        //noinspection unchecked
        return (LiteralArgumentBuilder<CommandSender>) cloneNode(built);
    }

    private ArgumentBuilder<CommandSender, ?> cloneNode(CommandNode<CommandSender> node) {

        ArgumentBuilder<CommandSender, ?> builder;

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

    public void register(@NotNull Plugin plugin) {
        CommandRegistry.of(plugin.getServer()).register(plugin, this);

        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);
            commodore.register(this, cloneForCommodore());
        }
    }

    public void unregister(@NotNull Server server) {
        CommandRegistry.of(server).unregister(this);
    }
}