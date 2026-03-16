package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class IntArgument extends Argument<CommandSender> {

    private final Integer min;
    private final Integer max;

    public IntArgument(String name) {
        super(name);
        this.min = null;
        this.max = null;
    }

    public IntArgument(String name, int min) {
        super(name);
        this.min = min;
        this.max = null;
    }

    public IntArgument(String name, int min, int max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    @Override
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        RequiredArgumentBuilder<CommandSender, Integer> builder;

        if (min != null && max != null)
            builder = RequiredArgumentBuilder.argument(getName(), IntegerArgumentType.integer(min, max));

        else if (min != null)
            builder = RequiredArgumentBuilder.argument(getName(), IntegerArgumentType.integer(min));

        else builder = RequiredArgumentBuilder.argument(getName(), IntegerArgumentType.integer());

        builder.suggests((ctx, sb) -> tabComplete(sb));

        return builder;
    }

    private CompletableFuture<Suggestions> tabComplete(SuggestionsBuilder sb) {
        String input = sb.getRemaining();

        if (input.isEmpty()) {
            sb.suggest("0");
            return sb.buildFuture();
        }

        int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            sb.suggest("Неверное целое число!");
            return sb.buildFuture();
        }

        if (min != null && value < min) {
            sb.suggest(input + " < " + min + "!");
            return sb.buildFuture();
        }

        if (max != null && value > max) {
            sb.suggest(input + " > " + max + "!");
            return sb.buildFuture();
        }

        sb.suggest(String.valueOf(value));
        return sb.buildFuture();
    }
}