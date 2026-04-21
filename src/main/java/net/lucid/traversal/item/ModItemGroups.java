package net.lucid.traversal.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.lucid.Traversal;
import net.lucid.traversal.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup TRAVERSAL_BLOCKS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Traversal.MOD_ID, "traversal_blocks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.CHARGED_SOUL_SAND))
                    .displayName(Text.translatable("itemgroup.traversal.traversal_blocks"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.CHARGED_SOUL_SAND);
                        entries.add(ModBlocks.NETHER_OIL_BLOCK);
                        entries.add(ModBlocks.CRYSTALLIZED_SOULS);
                        entries.add(ModBlocks.SOUL_SANDSTONE);
                    }).build());

    public static final ItemGroup TRAVERSAL_ITEMS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Traversal.MOD_ID, "traversal_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.CHARGED_SOUL_SAND))
                    .displayName(Text.translatable("itemgroup.traversal.traversal_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.KELP_WRAPPED_SOUL);
                        entries.add(ModItems.BUCKET_OF_NETHER_OIL);
                        entries.add(ModItems.DRIED_KELP_WRAPPED_FRIED_SOUL);
                        entries.add(ModItems.SHATTERED_SOUL);
                    }).build());


    public static void registerItemGroups() {
        Traversal.LOGGER.info("Registering Item Groups for " + Traversal.MOD_ID);
    }
}
