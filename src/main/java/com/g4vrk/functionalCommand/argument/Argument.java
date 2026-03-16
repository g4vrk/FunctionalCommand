package com.g4vrk.functionalCommand.argument;

import com.mojang.brigadier.builder.ArgumentBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Argument<S> {

    private String name;

    protected Argument(String name) {
        this.name = name;
    }

    public abstract ArgumentBuilder<S, ?> argumentBuilder();
}