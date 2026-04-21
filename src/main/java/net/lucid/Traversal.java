package net.lucid;

import net.fabricmc.api.ModInitializer;

import net.lucid.traversal.block.ModBlocks;
import net.lucid.traversal.item.ModItemGroups;
import net.lucid.traversal.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Traversal implements ModInitializer {
	public static final String MOD_ID = "traversal";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();
	}
}