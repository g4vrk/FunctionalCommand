package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class BooleanArgument extends Argument<CommandSender> {

    public BooleanArgument(String name) {
        super(name);
    }

    @Override
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        RequiredArgumentBuilder<CommandSender, Boolean> builder =
                RequiredArgumentBuilder.argument(getName(), BoolArgumentType.bool());

        builder.suggests((ctx, sb) -> suggest(sb));
        return builder;
    }

    private CompletableFuture<Suggestions> suggest(SuggestionsBuilder sb) {
        String remaining = sb.getRemaining().toLowerCase();

        if ("true".startsWith(remaining)) sb.suggest("true");
        if ("false".startsWith(remaining)) sb.suggest("false");

        return sb.buildFuture();
    }
}