package com.aotmc.attackontitan.commands.manager;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.commands.BladeCommand;
import com.aotmc.attackontitan.commands.GasCanisterCommand;
import com.aotmc.attackontitan.commands.ODMCommand;
import com.aotmc.attackontitan.commands.SpawnCommand;
import com.codeitforyou.lib.api.command.CommandManager;

import java.util.Arrays;

public class CommandsManager {

    private final com.codeitforyou.lib.api.command.CommandManager manager;

    public CommandsManager(final AttackOnTitan plugin) {
        this.manager = new com.codeitforyou.lib.api.command.CommandManager(Arrays.asList(
                SpawnCommand.class, BladeCommand.class, ODMCommand.class, GasCanisterCommand.class
        ), "aot", plugin);
    }

    public void registerCommand() {
        this.manager.register();
    }

    public CommandManager getManager() {
        return manager;
    }

}
