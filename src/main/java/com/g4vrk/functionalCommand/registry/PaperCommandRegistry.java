package com.g4vrk.functionalCommand.registry;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PaperCommandRegistry implements CommandRegistry {

    private final CommandMap commandMap;
    private final Map<String, Command> knownCommands;

    private final Logger logger;

    protected PaperCommandRegistry(CommandMap commandMap, Map<String, Command> knownCommands) {
        this.commandMap = commandMap;
        this.knownCommands = knownCommands;
        logger = LoggerFactory.getLogger("CommandRegistry");
    }

    @Override
    public @NotNull Optional<CommandMap> getCommandMap() {
        return Optional.ofNullable(commandMap);
    }

    @Override
    public @NotNull Map<String, Command> getKnownCommands() {
        return new HashMap<>(knownCommands);
    }

    public @NotNull Map<String, Command> getKnownCommands(@NotNull Predicate<Command> predicate) {
        Validate.notNull(predicate, "predicate cannot be null");

        Map<String, Command> map = new HashMap<>();
        for (Map.Entry<String, Command> e : knownCommands.entrySet()) {
            if (predicate.test(e.getValue())) map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    @Override
    public void register(@NotNull Plugin plugin, @NotNull Command command) {
        Validate.notNull(plugin, "plugin cannot be null");
        Validate.notNull(command, "command cannot be null");

        unregister(command.getName());

        String pluginName = plugin.getName();
        commandMap.register(
                pluginName,
                command
        );

        logger.info("Команда /{}:{} зарегистрирована плагином {}.", pluginName.toLowerCase(Locale.ROOT), command.getName(), pluginName);
    }

    @Override
    public void registerAll(@NotNull Plugin plugin, @NotNull Command... commands) {
        for (Command command : commands)
            register(plugin, command);
    }

    @Override
    public void override(@NotNull Command oldCommand, @NotNull Plugin owner, @NotNull Command newCommand) {
        unregister(oldCommand);
        register(owner, newCommand);
    }

    @Override
    public void override(@NotNull String oldName, @NotNull Plugin owner, @NotNull Command newCommand) {
        getCommand(oldName).ifPresent(cmd -> override(cmd, owner, newCommand));
    }

    @Override
    public void unregister(@NotNull String name) {
        String lower = name.toLowerCase(Locale.ROOT);

        knownCommands.entrySet().removeIf(entry -> {
            Command command = entry.getValue();

            if (matches(lower, command)) {
                command.unregister(commandMap);
                return true;
            }

            return false;
        });
    }

    @Override
    public void unregister(@NotNull Command command) {
        Validate.notNull(command, "command cannot be null");

        String lowerName = command.getName().toLowerCase(Locale.ROOT);

        knownCommands.entrySet().removeIf(entry -> {
            Command c = entry.getValue();

            if (c == command || matches(lowerName, c)) {
                c.unregister(commandMap);
                return true;
            }

            return false;
        });
    }

    @Override
    public void unregisterAll(@NotNull Plugin plugin) {
        String namespace = plugin.getName().toLowerCase(Locale.ROOT) + ":";

        knownCommands.entrySet().removeIf(entry -> {
            String key = entry.getKey().toLowerCase(Locale.ROOT);

            if (key.startsWith(namespace)) {
                entry.getValue().unregister(commandMap);
                return true;
            }

            return false;
        });
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
        return new HashSet<>(knownCommands.values());
    }

    @Override
    public @NotNull Collection<Command> getAllCommands(@NotNull Plugin plugin) {
        String namespace = plugin.getName().toLowerCase(Locale.ROOT) + ":";

        return knownCommands.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase(Locale.ROOT).startsWith(namespace))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
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
