package com.g4vrk.functionalCommand.executor;

import com.g4vrk.functionalCommand.context.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Функциональный интерфейс, отвечающий за выполнение команд.
 *
 * @param <C> тип контекста для выполнения.
 */
@FunctionalInterface
public interface CommandExecutor<C extends CommandContext> {

    /**
     * @param ctx контекст выполнения команды
     */
    void onExecutionRequest(@NotNull C ctx);
}