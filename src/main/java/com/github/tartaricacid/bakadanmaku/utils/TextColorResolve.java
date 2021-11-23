package com.github.tartaricacid.bakadanmaku.utils;

import com.github.tartaricacid.bakadanmaku.eneity.MsgAndStyle;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本颜色解析
 *
 * @author Guy_Z
 * @date 2021-11-21 10:09
 */
public class TextColorResolve {

    public static ITextComponent resolve(String msg) {

        if (!msg.contains(Constant.COLOR_KEYWORD)) {
            return new StringTextComponent(msg);
        }

        ArrayList<Integer> startIndex = getPointFromStringByString(msg, Constant.COLOR_KEYWORD);
        ArrayList<Integer> endIndex = getPointFromStringByString(msg, Constant.END_KEYWORD);
        ArrayList<MsgAndStyle> msgAndStyles = new ArrayList<>();

        int flagIndex = 0;

        for (int i = 0; i < startIndex.size(); i++) {
            msgAndStyles.add(new MsgAndStyle(msg.substring(flagIndex, startIndex.get(i))));

            String color = msg.substring(
                    startIndex.get(i) + Constant.COLOR_KEYWORD.length(),
                    msg.indexOf(" ", startIndex.get(i))
            );
            String str = msg.substring(
                    msg.indexOf(" ", startIndex.get(i)) + 1,
                    endIndex.get(i)
            );
            msgAndStyles.add(new MsgAndStyle(str, color));

            flagIndex = endIndex.get(i) + Constant.END_KEYWORD.length();
        }

        msgAndStyles.add(new MsgAndStyle(msg.substring(
                endIndex.get(endIndex.size() - 1),
                msg.length() - Constant.END_KEYWORD.length()
        )));
        return resolve(msgAndStyles);
    }

    private static ArrayList<Integer> getPointFromStringByString(String target, String find) {
        ArrayList<Integer> points = new ArrayList<>();
        int currentIndex = 0;
        while (currentIndex < target.length() - 1) {
            int i = target.indexOf(find, currentIndex);
            if (i == -1) {
                break;
            }
            points.add(i);
            currentIndex = i + 1;
        }
        return points;
    }

    public static ITextComponent resolve(List<MsgAndStyle> massagesAndStyles) {
        if (massagesAndStyles.size() < 1) {
            return changeToStringTextComponent(new MsgAndStyle(""));
        }
        IFormattableTextComponent iFormattableTextComponent = changeToStringTextComponent(massagesAndStyles.get(0));
        for (int i = 1; i < massagesAndStyles.size(); i++) {
            iFormattableTextComponent.func_230529_a_(
                    changeToStringTextComponent(massagesAndStyles.get(i))
            );
        }
        return iFormattableTextComponent;
    }

    private static IFormattableTextComponent changeToStringTextComponent(MsgAndStyle msgAndStyle) {
        return new StringTextComponent(msgAndStyle.getMsg()).func_230530_a_(msgAndStyle.getStyle());
    }

}
