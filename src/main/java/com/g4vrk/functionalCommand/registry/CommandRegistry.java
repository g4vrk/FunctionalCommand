package com.g4vrk.functionalCommand.registry;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Реестр команд, предоставляющий API для регистрации, поиска,
 * замены и удаления команд в {@link CommandMap}.
 *
 * <p>Инкапсулирует доступ к внутреннему хранилищу команд сервера
 * и позволяет централизованно управлять их жизненным циклом.</p>
 */
public interface CommandRegistry {

    /**
     * Возвращает {@link CommandMap}, если он доступен.
     *
     * @return Optional с CommandMap
     */
    @NotNull Optional<CommandMap> getCommandMap();

    /**
     * Возвращает Map всех известных команд.
     *
     * @return Map: имя → команда
     */
    @NotNull Map<String, Command> getKnownCommands();

    /**
     * Возвращает карту известных команд, по указанному фильтру.
     *
     * @param predicate условие фильтрации
     * @return map имя → команда
     */
    @NotNull Map<String, Command> getKnownCommands(Predicate<Command> predicate);

    /**
     * Регистрирует команду от имени плагина.
     *
     * @param plugin владелец команды
     * @param command команда
     */
    void register(@NotNull Plugin plugin, @NotNull Command command);

    /**
     * Регистрирует несколько команд.
     *
     * @param plugin владелец команд
     * @param commands команды
     */
    void registerAll(@NotNull Plugin plugin, @NotNull Command... commands);

    /**
     * Заменяет одну команду другой.
     *
     * @param oldCommand старая команда
     * @param newCommand новая команда
     */
    void override(@NotNull Command oldCommand, @NotNull Command newCommand);

    /**
     * Заменяет команду по имени.
     *
     * @param oldName имя существующей команды
     * @param newCommand новая команда
     */
    void override(@NotNull String oldName, @NotNull Command newCommand);

    /**
     * Удаляет команду по имени.
     *
     * @param name имя команды
     */
    void unregister(@NotNull String name);

    /**
     * Удаляет команду.
     *
     * @param command команда
     */
    void unregister(@NotNull Command command);

    /**
     * Удаляет все команды, зарегистрированные указанным плагином.
     *
     * @param plugin владелец команд
     */
    void unregisterAll(@NotNull Plugin plugin);

    /**
     * Возвращает команду по имени.
     *
     * @param name имя команды
     * @return Optional с командой
     */
    @NotNull Optional<Command> getCommand(@NotNull String name);

    /**
     * Возвращает коллекцию всех команд, зарегистрированных на сервере.
     *
     * @return все зарегистрированные команды
     */
    @NotNull Collection<Command> getAllCommands();

    /**
     * Возвращает команды, принадлежащие плагину.
     *
     * @param plugin владелец команд
     * @return все его команды
     */
    @NotNull Collection<Command> getAllCommands(@NotNull Plugin plugin);

    /**
     * Проверяет наличие команды по имени.
     *
     * @param name имя
     * @return true если существует
     */
    boolean contains(@NotNull String name);

    /**
     * Проверяет наличие команды.
     *
     * @param command команда
     * @return true если существует
     */
    boolean contains(@NotNull Command command);

    /**
     * Возвращает количество зарегистрированных команд.
     *
     * @return размер реестра
     */
    int size();


    /**
     * Создает новый реестр команд исходя из обьекта сервера.
     *
     * @param server обьект сервера
     * @return новый реестр {@link CommandRegistry}
     */
    static CommandRegistry of(Server server) {
        CommandMap commandMap = server.getCommandMap();
        Map<String, Command> knownCommands = commandMap.getKnownCommands();

        return new PaperCommandRegistry(commandMap, knownCommands);
    }
}