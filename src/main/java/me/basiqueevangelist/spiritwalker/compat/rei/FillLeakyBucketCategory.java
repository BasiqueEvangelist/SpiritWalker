package me.basiqueevangelist.spiritwalker.compat.rei;

import io.wispforest.owo.compat.rei.ReiUIAdapter;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.VerticalAlignment;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.List;

public class FillLeakyBucketCategory implements DisplayCategory<FillLeakyBucketDisplay> {
    @Override
    public CategoryIdentifier<? extends FillLeakyBucketDisplay> getCategoryIdentifier() {
        return SpiritWalkerReiClientPlugin.FILL_LEAKY_BUCKET;
    }

    @Override
    public List<Widget> setupDisplay(FillLeakyBucketDisplay display, Rectangle bounds) {
        var adapter = new ReiUIAdapter<>(bounds, Containers::horizontalFlow);
        var root = adapter.rootComponent();

        root
            .gap(5)
            .horizontalAlignment(HorizontalAlignment.CENTER)
            .verticalAlignment(VerticalAlignment.CENTER);

        root
            .child(adapter.wrap(Widgets.createRecipeBase(bounds))
                .positioning(Positioning.absolute(0, 0)));

        root.child(adapter.wrap(Widgets::createSlot, slot -> slot.entries(display.getInputEntries().get(0))));
        root.child(adapter.wrap(Widgets.createArrow(ReiUIAdapter.LAYOUT)));
        root.child(adapter.wrap(Widgets::createSlot, slot -> slot.entries(display.getInputEntries().get(1))));
        root.child(adapter.wrap(Widgets.createArrow(ReiUIAdapter.LAYOUT)));
        root.child(adapter.wrap(Widgets::createSlot, slot -> slot.entries(display.getOutputEntries().get(0))));

        adapter.prepare();
        return List.of(adapter);
    }

    @Override
    public int getDisplayHeight() {
        return DisplayCategory.super.getDisplayHeight() - 30;
    }

    @Override
    public int getDisplayWidth(FillLeakyBucketDisplay display) {
        return DisplayCategory.super.getDisplayWidth(display) - 10;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.spirit-walker.fill_leaky_bucket");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpiritWalker.FILLED_LEAKY_BUCKET);
    }
}
