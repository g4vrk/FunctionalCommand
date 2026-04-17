package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.RequiredArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PlayerArgument extends RequiredArgument<Player> {

    public PlayerArgument(@NotNull String name) {
        super(name, StringArgumentType.word());

        suggests(this::suggest);
    }

    private @NotNull CompletableFuture<Suggestions> suggest(
            final @NotNull CommandContext<CommandSender> context,
            final @NotNull SuggestionsBuilder suggestionsBuilder
    ) {
        final String lastWord = suggestionsBuilder.getRemainingLowerCase();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final String name = player.getName();

            if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                suggestionsBuilder.suggest(name);
            }
        }

        return suggestionsBuilder.buildFuture();
    }

    @Override
    public @NotNull Optional<Player> parse(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        try {
            final String name = context.getArgument(getName(), String.class);
            return Optional.ofNullable(Bukkit.getPlayerExact(name));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }
}