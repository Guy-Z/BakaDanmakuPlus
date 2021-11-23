package com.github.tartaricacid.bakadanmaku.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import static net.minecraft.util.Util.field_240973_b_;

/**
 * @author Guy_Z
 * @date 2021-11-23 9:49
 */
public class MsgToPlayer {

    public static void sendMsgToLocalPlayer(ITextComponent iTextComponent) {
        if (Minecraft.getInstance().player != null && iTextComponent != null) {
            Minecraft.getInstance().player.sendMessage(iTextComponent, field_240973_b_);
        }
    }

    public static void sendMsgToLocalPlayer(String msg) {
        if (!StringUtils.isNullOrEmpty(msg)) {
            sendMsgToLocalPlayer(new StringTextComponent(msg));
        }
    }

}
