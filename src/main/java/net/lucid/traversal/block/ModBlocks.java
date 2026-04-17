package net.lucid.traversal.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lucid.traversal.Traversal;
import net.lucid.traversal.block.custom.ChargedSoulSand;
import net.lucid.traversal.block.custom.NetherOilBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block SOUL_SANDSTONE = registerBlock("soul_sandstone",
            new Block(AbstractBlock.Settings.create().strength(0.25F)
                    .sounds(BlockSoundGroup.SOUL_SOIL).luminance(state -> 11)));

    public static final Block CHARGED_SOUL_SAND = registerBlock("charged_soul_sand",
            new ChargedSoulSand(
                    AbstractBlock.Settings.create().luminance(state -> 14).mapColor(MapColor.BLUE).strength(0.25F).sounds(BlockSoundGroup.SOUL_SAND).dynamicBounds()));

    public static final Block CRYSTALLIZED_SOULS = registerBlock("crystallized_souls",
            new ExperienceDroppingBlock(UniformIntProvider.create(1, 3),
                    AbstractBlock.Settings.create().strength(1.5f).requiresTool().sounds(BlockSoundGroup.SOUL_SAND)));

    public static final Block NETHER_OIL = registerBlock("nether_oil",
            new NetherOilBlock(
                    AbstractBlock.Settings.create().strength(0.5f)));

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(Traversal.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Traversal.MOD_ID, name), block);
    }

    public static void registerModBlocks(){
        Traversal.LOGGER.info("Registering modded blocks for "+Traversal.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(SOUL_SANDSTONE);
            entries.add(CHARGED_SOUL_SAND);
            entries.add(CRYSTALLIZED_SOULS);
            entries.add(NETHER_OIL);
        });
    }



}
