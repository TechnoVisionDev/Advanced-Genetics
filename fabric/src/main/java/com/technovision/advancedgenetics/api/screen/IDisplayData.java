package com.technovision.advancedgenetics.api.screen;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IDisplayData {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getValue();

    int getMaxValue();

    List<Component> toText();
}
