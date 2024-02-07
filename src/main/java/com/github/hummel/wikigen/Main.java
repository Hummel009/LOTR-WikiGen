package com.github.hummel.wikigen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "wikigen", dependencies = "required-after:lotr", useMetadata = true)
public class Main {

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new DbCommand());
	}
}
