package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.builder.ArgumentBuilder;

public abstract class CommandArgument<S> {

    private final String name;

    protected CommandArgument(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public abstract ArgumentBuilder<S, ?> brigadier();

}