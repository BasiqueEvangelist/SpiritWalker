package me.basiqueevangelist.spiritwalker.criteria;

import com.google.gson.JsonObject;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SavedFromFallDeathCriterion extends AbstractCriterion<SavedFromFallDeathCriterion.Conditions> {
    public static final Identifier ID = SpiritWalker.id("saved_from_fall_death");

    @Override
    public Identifier getId() {
        return ID;
    }

    public SavedFromFallDeathCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new SavedFromFallDeathCriterion.Conditions(ID, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(Identifier identifier, LootContextPredicate lootContextPredicate) {
            super(identifier, lootContextPredicate);
        }
    }
}
