/*
 * Copyright (C) 2016 The MoonLake Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.minecraft.moonlake.economy.api;

import com.minecraft.moonlake.validate.Validate;

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
