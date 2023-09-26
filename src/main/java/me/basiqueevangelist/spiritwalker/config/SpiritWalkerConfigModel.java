package me.basiqueevangelist.spiritwalker.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Config(name = "spirit-walker", wrapperName = "SpiritWalkerConfig")
@Modmenu(modId = "spirit-walker")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
public class SpiritWalkerConfigModel {
    @RestartRequired public boolean enableDefaultRecipe = true;
    @RestartRequired public int normalPotionLength = 15;
    @RestartRequired public int strongPotionLength = 15;
    @RestartRequired public int longPotionLength = 30;
    public boolean allowReturningImmediately = false;
    public boolean disableSplashAndLingering = true;
    public boolean listenFromBody = false;
    public boolean leakyBucket = true;
}
