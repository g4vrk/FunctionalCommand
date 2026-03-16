package com.g4vrk.functionalCommand.registry;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BaseCommandRegistry implements CommandRegistry {

    private final CommandMap commandMap;
    private final Map<String, Command> knownCommands;

    protected BaseCommandRegistry(CommandMap commandMap, Map<String, Command> knownCommands) {
        this.commandMap = commandMap;
        this.knownCommands = knownCommands;
    }

    @Override
    public @NotNull Optional<CommandMap> getCommandMap() {
        return Optional.ofNullable(commandMap);
    }

    @Override
    public @NotNull Map<String, Command> getKnownCommands() {
        return Collections.unmodifiableMap(knownCommands);
    }

    public @NotNull Map<String, Command> getKnownCommands(@NotNull Predicate<Command> predicate) {
        return knownCommands.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void register(@NotNull Plugin plugin, @NotNull Command command) {
        Validate.notNull(plugin, "@NotNull Plugin cannot be null");
        Validate.notNull(command, "@NotNull Command cannot be null");

        unregister(command.getName());

        commandMap.register(
                plugin.getName().toLowerCase(Locale.ROOT),
                command
        );
    }

    @Override
    public void registerAll(@NotNull Plugin plugin, @NotNull Command... commands) {
        for (Command command : commands)
            register(plugin, command);
    }

    @Override
    public void override(@NotNull Command oldCommand, @NotNull Command newCommand) {
        unregister(oldCommand.getName());
        register(resolveOwner(oldCommand), newCommand);
    }

    @Override
    public void override(@NotNull String oldName, @NotNull Command newCommand) {
        getCommand(oldName).ifPresent(cmd -> override(cmd, newCommand));
    }

    @Override
    public void unregister(@NotNull String name) {
        String lower = name.toLowerCase(Locale.ROOT);

        Iterator<Map.Entry<String, Command>> iterator = knownCommands.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Command> entry = iterator.next();
            Command command = entry.getValue();

            if (matches(lower, command)) {
                command.unregister(commandMap);
                iterator.remove();
            }
        }
    }

    @Override
    public void unregister(@NotNull Command command) {
        Validate.notNull(command, "Command cannot be null");

        Iterator<Map.Entry<String, Command>> iterator = knownCommands.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Command> entry = iterator.next();
            Command c = entry.getValue();

            if (c == command || matches(command.getName().toLowerCase(Locale.ROOT), c)) {
                c.unregister(commandMap);
                iterator.remove();
            }
        }
    }

    @Override
    public void unregisterAll(@NotNull Plugin plugin) {
        String namespace = plugin.getName().toLowerCase(Locale.ROOT) + ":";

        Iterator<Map.Entry<String, Command>> iterator = knownCommands.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Command> entry = iterator.next();

            if (entry.getKey().toLowerCase(Locale.ROOT).startsWith(namespace)) {
                entry.getValue().unregister(commandMap);
                iterator.remove();
            }
        }
    }

    @Override
    public @NotNull Optional<Command> getCommand(@NotNull String name) {
        String lower = name.toLowerCase(Locale.ROOT);

        for (Command cmd : knownCommands.values())
            if (matches(lower, cmd)) return Optional.of(cmd);

        return Optional.empty();
    }

    @Override
    public @NotNull Collection<Command> getAllCommands() {
        return Set.copyOf(knownCommands.values());
    }

    @Override
    public @NotNull Collection<Command> getAllCommands(@NotNull Plugin plugin) {
        String namespace = plugin.getName().toLowerCase(Locale.ROOT) + ":";

        return knownCommands.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase(Locale.ROOT).startsWith(namespace))
                .map(Map.Entry::getValue)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean contains(@NotNull String name) {
        return getCommand(name).isPresent();
    }

    @Override
    public boolean contains(@NotNull Command command) {
        return knownCommands.containsValue(command);
    }

    @Override
    public int size() {
        return knownCommands.size();
    }

    private boolean matches(String lowerName, Command command) {
        if (command.getName().equalsIgnoreCase(lowerName)) return true;

        for (String alias : command.getAliases()) {
            if (alias.equalsIgnoreCase(lowerName)) return true;
        }

        return false;
    }

    private Plugin resolveOwner(Command command) {
        String cmdName = command.getName().toLowerCase(Locale.ROOT);

        for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
            Command c = entry.getValue();
            if (matches(cmdName, c)) {
                String key = entry.getKey();
                String pluginName = key.contains(":") ? key.split(":")[0] : null;
                if (pluginName != null) {
                    Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                    if (plugin != null) return plugin;
                }
            }
        }

        throw new IllegalStateException("Cannot resolve owner for command: " + command.getName());
    }
}
