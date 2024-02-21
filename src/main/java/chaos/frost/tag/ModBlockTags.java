package chaos.frost.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import static chaos.frost.NewFrostwalker.id;

public final class ModBlockTags {
    private ModBlockTags() {

    }

    public static final TagKey<Block> REPLACED_BY_FROST_WALKER = TagKey.of(RegistryKeys.BLOCK, id("replaced_by_frost_walker"));
}
