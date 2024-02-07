package com.github.hummel.wikigen;

import com.github.hummel.wikigen.command.CommandDatabase;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "wikigen", dependencies = "required-after:lotr", useMetadata = true)
public class Main {
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDatabase());
	}
}
