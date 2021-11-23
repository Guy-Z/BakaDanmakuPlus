package com.github.tartaricacid.bakadanmaku.eneity;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;

/**
 * 文字与风格
 * <p>
 * 用于发送给玩家文字的数据传输
 *
 * @author Guy_Z
 * @date 2021-11-23 9:50
 */
public class MsgAndStyle {

    private String msg;
    private Style style;

    public MsgAndStyle() {
    }

    public MsgAndStyle(String msg) {
        this.msg = msg;
        this.style = Style.field_240709_b_;
    }

    public MsgAndStyle(String msg, Style style) {
        this.msg = msg;
        this.style = style;
    }

    /**
     * 进一步复用代码
     * @param msg
     * @param color 10进制颜色码
     */
    public MsgAndStyle(String msg, String color) {
        this.msg = msg;
        this.style = Style.field_240709_b_.func_240718_a_(Color.func_240743_a_(Integer.parseInt(color)));
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
