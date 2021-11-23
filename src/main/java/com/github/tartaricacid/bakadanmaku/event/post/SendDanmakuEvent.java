package com.github.tartaricacid.bakadanmaku.event.post;


import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.github.tartaricacid.bakadanmaku.utils.MsgToPlayer;
import com.github.tartaricacid.bakadanmaku.utils.TextColorResolve;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SendDanmakuEvent extends Event {
    private final String message;

    public SendDanmakuEvent(String message) {
        this.message = message;
    }

    @SubscribeEvent
    public static void onSendDanmaku(SendDanmakuEvent event) {
        try {
            MsgToPlayer.sendMsgToLocalPlayer(TextColorResolve.resolve(event.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            BakaDanmaku.LOGGER.error("msg error: {}", event.getMessage());
        }
    }

    public String getMessage() {
        return message;
    }
}
