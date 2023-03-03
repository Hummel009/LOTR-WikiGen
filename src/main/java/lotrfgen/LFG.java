package lotrfgen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "lotrfgen", dependencies = "required-after:lotr")
public class LFG {

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new LFGCommandDatabase());
	}
}
