package com.g4vrk.functionalCommand.context;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public record CommandContext(
        @NotNull String @NotNull [] args,
        @NotNull CommandSender sender
) {}
