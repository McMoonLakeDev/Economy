package com.minecraft.moonlake.economy;

import com.minecraft.moonlake.validate.Validate;

/**
 * Created by IntelliJ IDEA.
 * User: MoonLake
 * Date: 2016/10/16
 * Time: 11:38
 *
 * @author Month_Light
 * @version 1.0
 */
public enum EconomyType {

    /**
     * 经济类型: 金币
     */
    MONEY("Money", "金币"),
    /**
     * 经济类型: 点券
     */
    POINT("Point", "点券"),
    ;

    private final String type;
    private final String displayName;

    EconomyType(String type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EconomyType fromType(String type) {
        Validate.notNull(type, "The economy type object is null.");

        switch (type.toLowerCase()) {
            case "money":
                return MONEY;
            case "point":
                return POINT;
            default:
                return null;
        }
    }
}
