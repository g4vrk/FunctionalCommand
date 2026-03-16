package com.g4vrk.functionalCommand;

import com.g4vrk.functionalCommand.context.CommandContext;
import com.g4vrk.functionalCommand.executor.CommandExecutor;
import com.g4vrk.functionalCommand.suggest.SuggestionCompleter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter @Setter
public abstract class Command extends org.bukkit.command.Command {

    private CommandExecutor<CommandContext> commandExecutor;
    private SuggestionCompleter<CommandContext> suggestionCompleter;

    public Command(
            @NotNull String name
    ) {
        super(name);
    }

    public Command(
            @NotNull String name,
            @NotNull CommandExecutor<CommandContext> commandExecutor
    ) {
        super(name);
        this.commandExecutor = commandExecutor;
    }

    public Command(
            @NotNull String name,
            @NotNull CommandExecutor<CommandContext> commandExecutor,
            @NotNull SuggestionCompleter<CommandContext> suggestionCompleter
    ) {
        super(name);
        this.commandExecutor = commandExecutor;
        this.suggestionCompleter = suggestionCompleter;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        commandExecutor.onExecutionRequest(new CommandContext(args, sender));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions = suggestionCompleter.onSuggestionRequest(new CommandContext(args, sender));

        return completions != null
                ? completions
                : super.tabComplete(sender, alias, args);
    }
}
