package com.aotmc.attackontitan.general.util;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.blades.BladeType;
import com.aotmc.attackontitan.odmgear.ODMType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TabComplete implements TabCompleter
{

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args)
    {
        if (args.length > 5)
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

        final List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
            players.add(player.getName());
        }

        if (args[0].equalsIgnoreCase("spawn"))
        {
            return Arrays.asList("SMALL", "MEDIUM", "LARGE");
        }
        else if (args[0].equalsIgnoreCase("blade"))
        {
            if (args.length == 2)
            {
                return Arrays.asList("upgrade", "give");
            }
            else if (args.length == 3)
            {
                return players;
            }
            else if (args.length == 4)
            {
                final List<String> blades = new ArrayList<>();

                for (BladeType bladeType : BladeType.values())
                {
                    blades.add(bladeType.toString());
                }

                return blades;
            }
        }
        else if (args[0].equalsIgnoreCase("odm"))
        {
            if (args.length == 2)
            {
                return Collections.singletonList("give");
            }
            else if (args.length == 3) {
                return players;
            }
            else if (args.length == 4)
            {
                final List<String> odm = new ArrayList<>();

                for (ODMType odmType : ODMType.values())
                {
                    odm.add(odmType.toString());
                }

                return odm;
            }
        }
        return Collections.emptyList();
    }

}
