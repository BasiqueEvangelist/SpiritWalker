package me.basiqueevangelist.potionofdissociation.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RestartRequired;
import io.wispforest.owo.config.annotation.Sync;

@Config(name = "potion-of-dissociation", wrapperName = "PotionOfDissociationConfig")
@Modmenu(modId = "potion-of-dissociation")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
public class PotionOfDissociationConfigModel {
    @RestartRequired public boolean enableDefaultRecipe = true;
    public boolean listenFromBody = true;
}
