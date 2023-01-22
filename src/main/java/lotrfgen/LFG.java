package lotrfgen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;

@Mod(modid = "lotrfgen", dependencies = "required-after:lotr")
public class LFG {

	@Mod.EventHandler
	public void load(FMLInitializationEvent e) {
		LFGReflectionHelper.registerStructure(2019, LFGDatabaseGenerator.class, "DatabaseGenerator", 9605778, 9605778);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new LFGCommandDatabase());
	}
}
