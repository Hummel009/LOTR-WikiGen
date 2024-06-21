package com.github.hummel.wikigen.command;

import com.github.hummel.wikigen.engine.WikiGenerator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandWikiGen extends CommandBase {
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			Set<String> list = WikiGenerator.Type.getNames();
			return getListOfStringsMatchingLastWord(args, list.toArray(new String[0]));
		}
		return Collections.emptyList();
	}

	@Override
	public String getCommandName() {
		return "db";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "lotr.command.db.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		WikiGenerator.Type type = WikiGenerator.Type.forName(args[0]);
		if (type == null) {
			func_152373_a(sender, this, "Database \"" + args[0] + "\" does not exist.");
		} else {
			func_152373_a(sender, this, "Database \"" + type + "\" is prepared.");

			WikiGenerator.generate(type, world, (EntityPlayer) sender);
		}
	}
}