package net.lucid.traversal.registry.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class ModBlockTags {
//    public static final TagKey<Block> SOUL_FIRE_BASE_BLOCKS = of("soul_fire_base_blocks");
    public static final TagKey<Block> CHARGED_SOUL_FIRE_BASE_BLOCKS = of("charged_soul_fire_base_blocks");

    private ModBlockTags() {
    }

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla(id));
    }
}
