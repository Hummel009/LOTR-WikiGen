package lotrfgen;

import java.util.List;

import lotrfgen.LFGDatabaseGenerator.Database;
import net.minecraft.command.*;

public class LFGCommandDatabase extends CommandBase {

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		switch (args.length) {
		case 1:
			List<String> list = Database.getNames();
			return CommandBase.getListOfStringsMatchingLastWord(args, list.toArray(new String[0]));
		default:
			break;
		}
		return null;
	}

	@Override
	public String getCommandName() {
		return "db";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Something went wrong.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		Database db = Database.forName(args[0]);
		if (db == null) {
			CommandBase.func_152373_a(sender, this, "Database \"" + args[0] + "\" does not exist.");
		} else {
			LFGDatabaseGenerator.display = args[0];
			CommandBase.func_152373_a(sender, this, "Database \"" + args[0] + "\" is prepared.");
		}
	}
}
