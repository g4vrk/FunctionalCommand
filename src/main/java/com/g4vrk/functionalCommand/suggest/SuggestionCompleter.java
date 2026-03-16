package com.g4vrk.functionalCommand.suggest;

import com.g4vrk.functionalCommand.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Функциональный интерфейс, отвечающий за генерацию списка подсказок
 *
 * @param <C> тип контекста запроса подсказок
 */
@FunctionalInterface
public interface SuggestionCompleter<C extends CommandContext> {

    /**
     * @param ctx контекст запроса подсказок
     * @return список подсказок
     */
    @Nullable List<String> onSuggestionRequest(@NotNull C ctx);
}