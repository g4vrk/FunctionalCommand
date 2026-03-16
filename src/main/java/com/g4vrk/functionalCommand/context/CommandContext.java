package com.g4vrk.functionalCommand.context;

import lombok.Data;
import org.bukkit.command.CommandSender;

@Data
public final class CommandContext {
    private final String[] args;
    private final CommandSender sender;
}
