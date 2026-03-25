package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public abstract class Argument<T> {

    private final String name;

    protected Argument(@NotNull String name) {
        this.name = name;
    }

    public abstract @NotNull Optional<T> getFromContext(@NotNull CommandContext<CommandSender> context);

    public abstract @NotNull ArgumentBuilder<CommandSender, ?> argumentBuilder();
}