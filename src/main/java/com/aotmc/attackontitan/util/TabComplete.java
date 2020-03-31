package com.aotmc.attackontitan.util;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.blades.Blades;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TabComplete implements TabCompleter
{

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args)
    {

        if (args.length > 3)
        {
            return Collections.emptyList();
        }

        if (args.length == 1)
        {
            final List<String> commands = new ArrayList<>();
            for (final Map.Entry<String, Method> commandPair : AttackOnTitan.getInstance().getManager().getManager().getCommands().entrySet())
            {
                if (commandPair.getKey().toLowerCase().startsWith(args[0].toLowerCase()))
                {
                    commands.add(commandPair.getKey());
                }
            }

            return commands;
        }
        else if (args.length == 2)
        {
            final List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getServer().getOnlinePlayers())
            {
               players.add(player.getName());
            }

            return players;
        }
        else if (args.length == 3)
        {
            final List<String> blades = new ArrayList<>();

            for (Blades blade : Blades.values())
            {
                blades.add(blade.toString());
            }

            return blades;
        }
        return Collections.emptyList();
    }

}
