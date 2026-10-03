package net.neverandy.moredyes.compat.hei;

import mezz.jei.api.ICollapsibleGroupRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * Folds every color of each kind of item into one collapsible group in HEI's (Had Enough Items, CleanroomMC's JEI
 * fork) item list. JEI and HEI find this class by its annotation, so it is only loaded when one of them is installed.
 * The original JEI never calls registerCollapsibleGroups, and the HEI types it uses are only touched in
 * {@link HEIGroups}, so the plugin loads under the original JEI as well and does nothing there.
 */
@JEIPlugin
public class MoreDyesJEIPlugin implements IModPlugin
{
	@Override
	public void registerCollapsibleGroups(ICollapsibleGroupRegistry registry)
	{
		// HEI drops a plugin whose method throws, so a problem here only costs the groups.
		try
		{
			HEIGroups.register(registry);
		}
		catch(RuntimeException|LinkageError e)
		{
			LogHelper.error("Could not add the More Dyes groups to HEI: "+e);
		}
	}
}
