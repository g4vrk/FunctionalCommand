package com.g4vrk.functionalLib.command.registry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface CommandRegistry {

    @NotNull Optional<CommandMap> getCommandMap();

    @NotNull Map<String, Command> getKnownCommands();
    @NotNull Map<String, Command> getKnownCommands(Predicate<Command> predicate);

    void register(@NotNull Plugin plugin, @NotNull Command command);
    void registerAll(@NotNull Plugin plugin, @NotNull Command... commands);

    void override(@NotNull Command oldCommand, @NotNull Command newCommand);
    void override(@NotNull String oldName, @NotNull Command newCommand);

    void unregister(@NotNull String name);
    void unregister(@NotNull Command command);
    void unregisterAll(@NotNull Plugin plugin);

    @NotNull Optional<Command> getCommand(@NotNull String name);
    @NotNull Collection<Command> getAllCommands();
    @NotNull Collection<Command> getAllCommands(@NotNull Plugin plugin);

    boolean contains(@NotNull String name);
    boolean contains(@NotNull Command command);

    int size();
}
