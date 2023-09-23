package me.basiqueevangelist.spiritwalker.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

import java.util.List;

public class FillLeakyBucketDisplay extends BasicDisplay {
    public FillLeakyBucketDisplay(EntryIngredient input1, EntryIngredient input2, EntryIngredient output) {
        super(List.of(input1, input2), List.of(output));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SpiritWalkerReiClientPlugin.FILL_LEAKY_BUCKET;
    }
}
