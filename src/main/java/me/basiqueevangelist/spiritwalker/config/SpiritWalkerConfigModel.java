package me.basiqueevangelist.spiritwalker.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RestartRequired;
import io.wispforest.owo.config.annotation.Sync;

@Config(name = "spirit-walker", wrapperName = "SpiritWalkerConfig")
@Modmenu(modId = "spirit-walker")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
public class SpiritWalkerConfigModel {
    @RestartRequired public boolean enableDefaultRecipe = true;
    public boolean listenFromBody = true;
}
