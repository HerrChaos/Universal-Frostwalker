package chaos.better_frost_walker.tag;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import static chaos.better_frost_walker.BetterFrostWalkerMain.id;

public final class ModBlockTags {
    private ModBlockTags() {

    }

    // todo: unused?? ohh ok it's not referenced in code, but the tag itself is used in the enchantment definition stuff in datapacks. Hmm though I think ReplaceDiskEnchantmentEffectMixin should also be using it and isn't? ah no of course not, the enchantment definition from the datapack already matches this so that mixin only runs when this is matched already, got it :D wow this is a long todo comment that I will definitely forget about before pushing the next commit so this is going to stay on github, even if just on one commit, forever.
    public static final TagKey<Block> REPLACED_BY_FROST_WALKER = TagKey.of(RegistryKeys.BLOCK, id("replaced_by_frost_walker"));
}
