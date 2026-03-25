package com.g4vrk.functionalCommand.argument.types;

import com.g4vrk.functionalCommand.argument.Argument;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FloatArgument extends Argument<Float> {

    private final float min;
    private final float max;

    public FloatArgument(String name) {
        super(name);
        this.min = Float.MIN_VALUE;
        this.max = Float.MAX_VALUE;
    }

    public FloatArgument(String name, float min) {
        super(name);
        this.min = min;
        this.max = Float.MAX_VALUE;
    }

    public FloatArgument(String name, float min, float max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    @Override
    public @NotNull ArgumentBuilder<CommandSender, ?> argumentBuilder() {
        final RequiredArgumentBuilder<CommandSender, Float> builder =
                RequiredArgumentBuilder.argument(getName(), FloatArgumentType.floatArg(min, max));

        builder.executes(context -> 1);
        builder.suggests((ctx, sb) -> suggest(sb));
        return builder;
    }

    @Override
    public @NotNull Optional<Float> getFromContext(@NotNull CommandContext<CommandSender> context) {
        try {
            return Optional.ofNullable(context.getArgument(getName(), Float.class));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    private CompletableFuture<Suggestions> suggest(SuggestionsBuilder sb) {
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

        if (value < min) {
            sb.suggest(input + " < " + min + "!");
            return sb.buildFuture();
        }

        if (value > max) {
            sb.suggest(input + " > " + max + "!");
            return sb.buildFuture();
        }

        sb.suggest(String.valueOf(value));
        return sb.buildFuture();
    }
}