/*
 * This file is part of ViaMCP - https://github.com/FlorianMichael/ViaMCP
 * Copyright (C) 2020-2024 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
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

package de.florianmichael.viamcp.protocolinfo;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.Arrays;
import java.util.List;

public class ProtocolInfo {

    public static ProtocolInfo R1_21_4 = new ProtocolInfo("The Garden Awakens", "December 3, 2024", ProtocolVersion.v1_21_4);
    public static ProtocolInfo R1_21_2 = new ProtocolInfo("Bundles of Bravery", "October 22, 2024", ProtocolVersion.v1_21_2);
    public static ProtocolInfo R1_21 = new ProtocolInfo("Tricky Trials", "June 13, 2024", ProtocolVersion.v1_21);
    public static ProtocolInfo R1_20_5 = new ProtocolInfo("Trails & Tales", "April 23, 2024", ProtocolVersion.v1_20_5);
    public static ProtocolInfo R1_20_3 = new ProtocolInfo("Trails & Tales", "December 5, 2023", ProtocolVersion.v1_20_3);
    public static ProtocolInfo R1_20_2 = new ProtocolInfo("Trails & Tales", "September 21, 2023", ProtocolVersion.v1_20_2);
    public static ProtocolInfo R1_20 = new ProtocolInfo("Trails & Tales", "16 May, 2023", ProtocolVersion.v1_20);
    public static ProtocolInfo R1_19_4 = new ProtocolInfo("The Wild Update", "14 March, 2022", ProtocolVersion.v1_19_4);
    public static ProtocolInfo R1_19_3 = new ProtocolInfo("The Wild Update", "August 5, 2022 - December 17, 2022", ProtocolVersion.v1_19_3);
    public static ProtocolInfo R1_19_1 = new ProtocolInfo("The Wild Update", "July 27, 2022 - August 5, 2022", ProtocolVersion.v1_19_1);
    public static ProtocolInfo R1_19 = new ProtocolInfo("The Wild Update", "June 7, 2022", ProtocolVersion.v1_19);
    public static ProtocolInfo R1_18_2 = new ProtocolInfo("Caves & Cliffs: Part II", "February 28, 2022", ProtocolVersion.v1_18_2);
    public static ProtocolInfo R1_18 = new ProtocolInfo("Caves & Cliffs: Part II", "November 30, 2021 - December 10, 2021", ProtocolVersion.v1_18);
    public static ProtocolInfo R1_17_1 = new ProtocolInfo("Caves & Cliffs: Part I", "July 6, 2021", ProtocolVersion.v1_17_1);
    public static ProtocolInfo R1_17 = new ProtocolInfo("Caves & Cliffs: Part I", "June 8, 2021", ProtocolVersion.v1_17);
    public static ProtocolInfo R1_16_4 = new ProtocolInfo("Nether Update", "November 2, 2020 - January 13, 2021", ProtocolVersion.v1_16_4);
    public static ProtocolInfo R1_16_3 = new ProtocolInfo("Nether Update", "September 7, 2020", ProtocolVersion.v1_16_3);
    public static ProtocolInfo R1_16_2 = new ProtocolInfo("Nether Update", "August 11, 2020", ProtocolVersion.v1_16_2);
    public static ProtocolInfo R1_16_1 = new ProtocolInfo("Nether Update", "June 24, 2020", ProtocolVersion.v1_16_1);
    public static ProtocolInfo R1_16 = new ProtocolInfo("Nether Update", "June 23, 2020", ProtocolVersion.v1_16);
    public static ProtocolInfo R1_15_2 = new ProtocolInfo("Buzzy Bees", "January 21, 2020", ProtocolVersion.v1_15_2);
    public static ProtocolInfo R1_15_1 = new ProtocolInfo("Buzzy Bees", "December 17, 2019", ProtocolVersion.v1_15_1);
    public static ProtocolInfo R1_15 = new ProtocolInfo("Buzzy Bees", "December 10, 2019", ProtocolVersion.v1_15);
    public static ProtocolInfo R1_14_4 = new ProtocolInfo("Village & Pillage", "July 19, 2019", ProtocolVersion.v1_14_4);
    public static ProtocolInfo R1_14_3 = new ProtocolInfo("Village & Pillage", "June 24, 2019", ProtocolVersion.v1_14_3);
    public static ProtocolInfo R1_14_2 = new ProtocolInfo("Village & Pillage", "May 27, 2019", ProtocolVersion.v1_14_2);
    public static ProtocolInfo R1_14_1 = new ProtocolInfo("Village & Pillage", "May 13, 2019", ProtocolVersion.v1_14_1);
    public static ProtocolInfo R1_14 = new ProtocolInfo("Village & Pillage", "April 23, 2019", ProtocolVersion.v1_14);
    public static ProtocolInfo R1_13_2 = new ProtocolInfo("Update Aquatic", "October 22, 2018", ProtocolVersion.v1_13_2);
    public static ProtocolInfo R1_13_1 = new ProtocolInfo("Update Aquatic", "August 22, 2018", ProtocolVersion.v1_13_1);
    public static ProtocolInfo R1_13 = new ProtocolInfo("Update Aquatic", "July 18, 2018", ProtocolVersion.v1_13);
    public static ProtocolInfo R1_12_2 = new ProtocolInfo("World of Color Update", "September 18, 2017", ProtocolVersion.v1_12_2);
    public static ProtocolInfo R1_12_1 = new ProtocolInfo("World of Color Update", "August 3, 2017", ProtocolVersion.v1_12_1);
    public static ProtocolInfo R1_12 = new ProtocolInfo("World of Color Update", "June 7, 2017", ProtocolVersion.v1_12);
    public static ProtocolInfo R1_11_1 = new ProtocolInfo("Exploration Update", "December 20, 2016 - December 21, 2016", ProtocolVersion.v1_11_1);
    public static ProtocolInfo R1_11 = new ProtocolInfo("Exploration Update", "November 14, 2016", ProtocolVersion.v1_11);
    public static ProtocolInfo R1_10 = new ProtocolInfo("Frostburn Update", "June 8, 2016 - June 23, 2016", ProtocolVersion.v1_10);
    public static ProtocolInfo R1_9_3 = new ProtocolInfo("Combat Update", "May 10, 2016", ProtocolVersion.v1_9_3);
    public static ProtocolInfo R1_9_2 = new ProtocolInfo("Combat Update", "March 30, 2016", ProtocolVersion.v1_9_2);
    public static ProtocolInfo R1_9_1 = new ProtocolInfo("Combat Update", "March 30, 2016", ProtocolVersion.v1_9_1);
    public static ProtocolInfo R1_9 = new ProtocolInfo("Combat Update", "February 29, 2016", ProtocolVersion.v1_9);
    public static ProtocolInfo R1_8 = new ProtocolInfo("Bountiful Update", "September 2, 2014 - December 9, 2015", ProtocolVersion.v1_8);
    public static ProtocolInfo R1_7_6 = new ProtocolInfo("The Update that Changed the World", "April 9, 2014 - June 26, 2014", ProtocolVersion.v1_7_6);
    public static ProtocolInfo R1_7 = new ProtocolInfo("The Update that Changed the World", "October 22, 2013 - February 26, 2014", ProtocolVersion.v1_7_2);

    private final static List<ProtocolInfo> PROTOCOL_INFOS = Arrays.asList(R1_7, R1_7_6, R1_8, R1_9, R1_9_1, R1_9_2, R1_9_3, R1_10, R1_11, R1_11_1, R1_12, R1_12_1, R1_12_2, R1_13, R1_13_1, R1_13_2, R1_14, R1_14_1, R1_14_2, R1_14_3, R1_14_4,
            R1_15, R1_15_1, R1_15_2, R1_16, R1_16_1, R1_16_2, R1_16_3, R1_16_4, R1_17, R1_17_1, R1_18, R1_18_2, R1_19, R1_19_1, R1_19_3, R1_19_4, R1_20, R1_20_2, R1_20_3, R1_20_5, R1_21, R1_21_2, R1_21_4);

    private final String name;
    private final String releaseDate;
    private final ProtocolVersion protocolVersion;

    public ProtocolInfo(final String name, final String releaseDate, final ProtocolVersion protocolVersion) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.protocolVersion = protocolVersion;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public static ProtocolInfo fromProtocolVersion(final ProtocolVersion protocolVersion) {
        for (ProtocolInfo protocolInfo : PROTOCOL_INFOS) {
            if (protocolInfo.getProtocolVersion().getName().equals(protocolVersion.getName())) {
                return protocolInfo;
            }
        }
        return null;
    }
}
