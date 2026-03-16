package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class DoubleArgument extends Argument<CommandSender> {

    private final Double min;
    private final Double max;

    public DoubleArgument(String name) {
        super(name);
        this.min = null;
        this.max = null;
    }

    public DoubleArgument(String name, double min) {
        super(name);
        this.min = min;
        this.max = null;
    }

    public DoubleArgument(String name, double min, double max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    @Override
    public ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        RequiredArgumentBuilder<CommandSender, Double> builder;

        if (min != null && max != null)
            builder = RequiredArgumentBuilder.argument(getName(), DoubleArgumentType.doubleArg(min, max));

        else if (min != null)
            builder = RequiredArgumentBuilder.argument(getName(), DoubleArgumentType.doubleArg(min));

        else builder = RequiredArgumentBuilder.argument(getName(), DoubleArgumentType.doubleArg());

        builder.suggests((ctx, sb) -> tabComplete(sb));

        return builder;
    }

    private CompletableFuture<Suggestions> tabComplete(SuggestionsBuilder sb) {
        String input = sb.getRemaining();

        if (input.isEmpty()) {
            sb.suggest("0.0");
            return sb.buildFuture();
        }

        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            sb.suggest("Неверное число!");
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