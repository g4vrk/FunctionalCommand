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

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static final char T = 't';
    private static final char F = 'f';

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

        if (remaining.isEmpty()) {
            sb.suggest(TRUE);
            sb.suggest(FALSE);

            return sb.buildFuture();
        }

        switch (remaining.charAt(0)) {
            case T -> sb.suggest(TRUE);
            case F -> sb.suggest(FALSE);
            default -> {
                sb.suggest(TRUE);
                sb.suggest(FALSE);
            }
        }

        return sb.buildFuture();
    }
}