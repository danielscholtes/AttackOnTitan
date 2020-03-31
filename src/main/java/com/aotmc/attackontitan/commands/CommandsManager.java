package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.codeitforyou.lib.api.command.CommandManager;

import java.util.Arrays;

public class CommandsManager
{

    private final com.codeitforyou.lib.api.command.CommandManager manager;

    public CommandsManager(final AttackOnTitan plugin)
    {
        this.manager = new com.codeitforyou.lib.api.command.CommandManager(Arrays.asList(
                GiveCommand.class, UpgradeGuiCommand.class, SpawnCommand.class
        ), "aot", plugin);
    }

    public void registerCommand()
    {
        this.manager.register();
    }

    public CommandManager getManager()
    {
        return manager;
    }

}
