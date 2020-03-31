package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;

import java.util.Collections;

public class CommandsManager
{

    private final com.codeitforyou.lib.api.command.CommandManager manager;

    public CommandsManager(final AttackOnTitan plugin)
    {
        this.manager = new com.codeitforyou.lib.api.command.CommandManager(Collections.singletonList(
                GiveCommand.class
        ), "aot", plugin);
    }

    public void registerCommand()
    {
        this.manager.register();
    }

}
