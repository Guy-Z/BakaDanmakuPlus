package com.github.tartaricacid.bakadanmaku.site.bilibili;

import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.utils.Constant;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<String> {
    private final BilibiliConfig config;

    public MessageDeserializer(BilibiliConfig config) {
        this.config = config;
    }

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject data = json.getAsJsonObject();
            String type = data.get("cmd").getAsString();
            switch (type) {
                case "DANMU_MSG":
                    if (config.getDanmaku().isShow()) {
                        return handDanmaku(data);
                    }
                    break;
                case "SEND_GIFT":
                    if (config.getGift().isShow()) {
                        return handGift(data);
                    }
                    break;
                case "COMBO_SEND":
                    if (config.getGift().isShow()) {
                        return handComboGift(data);
                    }
                    break;
                case "INTERACT_WORD":
                    if (config.getEnter().isShowNormal()) {
                        return handNormalEnter(data);
                    }
                    break;
                case "WELCOME":
                    if (config.getEnter().isShowNormal()) {
                        return handWelcome(data);
                    }
                    break;
                case "WELCOME_GUARD":
                    if (config.getEnter().isShowGuard()) {
                        return handGuardWelcome(data);
                    }
                    break;
                case "GUARD_BUY":
                    if (config.getGuard().isShow()) {
                        return handBuyGuard(data);
                    }
                    break;
                case "SUPER_CHAT_MESSAGE":
                    if (config.getSc().isShow()) {
                        return handSuperChat(data);
                    }
                    break;
                default:
            }
        }
        return null;
    }

    private String handDanmaku(JsonObject dataIn) {
        JsonArray info = dataIn.getAsJsonArray("info");
        BakaDanmaku.LOGGER.info("info: {}", info);
        JsonArray user = info.get(2).getAsJsonArray();
        JsonArray medalAbout = info.get(3).getAsJsonArray();

        String userName = removeSensitiveStr(user.get(1).getAsString());

        int danmakuColor = info.get(0).getAsJsonArray().get(3).getAsInt();
        String danmaku = removeSensitiveStr(info.get(1).getAsString());
        for (String block : config.getDanmaku().getBlockWord()) {
            if (danmaku.contains(block)) {
                return null;
            }
        }
        StringBuilder stringBuilderDanmaku = appendColorKeyword(danmakuColor, danmaku);

        StringBuilder stringBuilderMedal = new StringBuilder();
        if (medalAbout.size() >= 5) {
            int medalColor = medalAbout.get(4).getAsInt();
            stringBuilderMedal.append(
                    String.format(
                            config.getDanmaku().getMedalStyleFormatted(),
                            appendColorKeyword(medalColor, removeSensitiveStr(medalAbout.get(1).getAsString())),
                            appendColorKeyword(medalColor, medalAbout.get(0).getAsString())
                    )
            );
        }

        String styleFormatted;
        if (user.get(2).getAsInt() == 1) {
            styleFormatted = config.getDanmaku().getAdminStyleFormatted();
        } else if (StringUtils.isNotBlank(user.get(7).getAsString())) {
            styleFormatted = config.getDanmaku().getGuardStyleFormatted();
        } else {
            styleFormatted = config.getDanmaku().getNormalStyleFormatted();
        }
        return String.format(styleFormatted, userName, stringBuilderDanmaku, stringBuilderMedal);
    }

    private String removeSensitiveStr(String str) {
        return str.replace(Constant.COLOR_KEYWORD, "").replace(Constant.END_KEYWORD, "");
    }

    private StringBuilder appendColorKeyword(int color, String msg){
        return new StringBuilder().append(Constant.COLOR_KEYWORD)
                .append(color)
                .append(" ")
                .append(msg)
                .append(Constant.END_KEYWORD);
    }

    private String handGift(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();
        String action = data.get("action").getAsString();
        String giftName = data.get("giftName").getAsString();
        int num = data.get("num").getAsInt();

        for (String block : config.getGift().getBlockGift()) {
            if (giftName.equals(block)) {
                return null;
            }
        }

        return String.format(config.getGift().getStyleFormatted(), userName, action, giftName, num);
    }

    private String handComboGift(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();
        String action = data.get("action").getAsString();
        String giftName = data.get("gift_name").getAsString();
        int num = data.get("total_num").getAsInt();

        for (String block : config.getGift().getBlockGift()) {
            if (giftName.equals(block)) {
                return null;
            }
        }

        return String.format(config.getGift().getStyleFormatted(), userName, action, giftName, num);
    }

    private String handNormalEnter(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();

        return String.format(config.getEnter().getNormalStyleFormatted(), userName);
    }

    private String handWelcome(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("uname").getAsString();

        return String.format(config.getEnter().getNormalStyleFormatted(), userName);
    }

    private String handGuardWelcome(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("username").getAsString();
        int level = data.get("guard_level").getAsInt();
        switch (level) {
            case 1:
                return String.format(config.getEnter().getGuardStyle1Formatted(), userName);
            case 2:
                return String.format(config.getEnter().getGuardStyle2Formatted(), userName);
            case 3:
                return String.format(config.getEnter().getGuardStyle3Formatted(), userName);
            default:
                return null;
        }
    }

    private String handBuyGuard(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.get("username").getAsString();
        int level = data.get("guard_level").getAsInt();
        switch (level) {
            case 1:
                return String.format(config.getGuard().getGuardStyle1Formatted(), userName);
            case 2:
                return String.format(config.getGuard().getGuardStyle2Formatted(), userName);
            case 3:
                return String.format(config.getGuard().getGuardStyle3Formatted(), userName);
            default:
                return null;
        }
    }

    private String handSuperChat(JsonObject dataIn) {
        JsonObject data = dataIn.getAsJsonObject("data");
        String userName = data.getAsJsonObject("user_info").get("uname").getAsString();
        String message = data.get("message").getAsString();
        int price = data.get("price").getAsInt();

        return String.format(config.getSc().getStyleFormatted(), userName, message, price);
    }
}
