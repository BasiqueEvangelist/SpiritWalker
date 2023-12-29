package me.basiqueevangelist.spiritwalker.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class DiedDuringSpiritWalkCriterion extends AbstractCriterion<DiedDuringSpiritWalkCriterion.Conditions> {
    @Override
    public DiedDuringSpiritWalkCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new DiedDuringSpiritWalkCriterion.Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public Conditions(Optional<LootContextPredicate> lootContextPredicate) {
            super(lootContextPredicate);
        }
    }
}
