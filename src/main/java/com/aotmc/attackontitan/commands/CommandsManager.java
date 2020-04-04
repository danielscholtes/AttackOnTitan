package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.commands.gui.ConverterGUI;
import com.aotmc.attackontitan.commands.gui.MaterialsGUI;
import com.aotmc.attackontitan.commands.gui.UpgradeGUI;
import com.codeitforyou.lib.api.command.CommandManager;

import java.util.Arrays;

public class CommandsManager
{

    private final com.codeitforyou.lib.api.command.CommandManager manager;

    public CommandsManager(final AttackOnTitan plugin)
    {
        this.manager = new com.codeitforyou.lib.api.command.CommandManager(Arrays.asList(
                GiveCommand.class, UpgradeGUI.class, SpawnCommand.class,
                MaterialsGUI.class, ConverterGUI.class,
                GiveMaterialCommand.class
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
