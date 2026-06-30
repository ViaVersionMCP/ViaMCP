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

public enum ProtocolInfo {
	
    R_26_2("Chaos Cubed", "June 16, 2026", ProtocolVersion.v26_2),
    R_26_1("Tiny Takeover", "March 24, 2026", ProtocolVersion.v26_1),
    R1_21_11("Mounts of Mayhem", "December 9, 2025", ProtocolVersion.v1_21_11),
    R1_21_9("The Copper Age", "September 30, 2025", ProtocolVersion.v1_21_9),
    R1_21_7("Chase the Skies", "June 30, 2025", ProtocolVersion.v1_21_7),
    R1_21_6("Chase the Skies", "June 17, 2025", ProtocolVersion.v1_21_6),
    R1_21_5("Spring to Life", "March 25, 2025", ProtocolVersion.v1_21_5),
    R1_21_4("The Garden Awakens", "December 3, 2024", ProtocolVersion.v1_21_4),
    R1_21_2("Bundles of Bravery", "October 22, 2024", ProtocolVersion.v1_21_2),
    R1_21("Tricky Trials", "June 13, 2024", ProtocolVersion.v1_21),
    R1_20_5("Armored Paws", "April 23, 2024", ProtocolVersion.v1_20_5),
    R1_20_3("Bats and Pots", "December 5, 2023", ProtocolVersion.v1_20_3),
    R1_20_2("Trails & Tales", "September 21, 2023", ProtocolVersion.v1_20_2),
    R1_20("Trails & Tales", "June 7, 2023", ProtocolVersion.v1_20),
    R1_19_4("The Wild Update", "March 14, 2023", ProtocolVersion.v1_19_4),
    R1_19_3("The Wild Update", "December 7, 2022", ProtocolVersion.v1_19_3),
    R1_19_1("The Wild Update", "July 27, 2022", ProtocolVersion.v1_19_1),
    R1_19("The Wild Update", "June 7, 2022", ProtocolVersion.v1_19),
    R1_18_2("Caves & Cliffs: Part II", "February 28, 2022", ProtocolVersion.v1_18_2),
    R1_18("Caves & Cliffs: Part II", "November 30, 2021", ProtocolVersion.v1_18),
    R1_17_1("Caves & Cliffs: Part I", "July 6, 2021", ProtocolVersion.v1_17_1),
    R1_17("Caves & Cliffs: Part I", "June 8, 2021", ProtocolVersion.v1_17),
    R1_16_4("Nether Update", "November 2, 2020", ProtocolVersion.v1_16_4),
    R1_16_3("Nether Update", "September 7, 2020", ProtocolVersion.v1_16_3),
    R1_16_2("Nether Update", "August 11, 2020", ProtocolVersion.v1_16_2),
    R1_16_1("Nether Update", "June 24, 2020", ProtocolVersion.v1_16_1),
    R1_16("Nether Update", "June 23, 2020", ProtocolVersion.v1_16),
    R1_15_2("Buzzy Bees", "January 21, 2020", ProtocolVersion.v1_15_2),
    R1_15_1("Buzzy Bees", "December 17, 2019", ProtocolVersion.v1_15_1),
    R1_15("Buzzy Bees", "December 10, 2019", ProtocolVersion.v1_15),
    R1_14_4("Village & Pillage", "July 19, 2019", ProtocolVersion.v1_14_4),
    R1_14_3("Village & Pillage", "June 24, 2019", ProtocolVersion.v1_14_3),
    R1_14_2("Village & Pillage", "May 27, 2019", ProtocolVersion.v1_14_2),
    R1_14_1("Village & Pillage", "May 13, 2019", ProtocolVersion.v1_14_1),
    R1_14("Village & Pillage", "April 23, 2019", ProtocolVersion.v1_14),
    R1_13_2("Update Aquatic", "October 22, 2018", ProtocolVersion.v1_13_2),
    R1_13_1("Update Aquatic", "August 22, 2018", ProtocolVersion.v1_13_1),
    R1_13("Update Aquatic", "July 18, 2018", ProtocolVersion.v1_13),
    R1_12_2("World of Color Update", "September 18, 2017", ProtocolVersion.v1_12_2),
    R1_12_1("World of Color Update", "August 3, 2017", ProtocolVersion.v1_12_1),
    R1_12("World of Color Update", "June 7, 2017", ProtocolVersion.v1_12),
    R1_11_1("Exploration Update", "December 20, 2016", ProtocolVersion.v1_11_1),
    R1_11("Exploration Update", "November 14, 2016", ProtocolVersion.v1_11),
    R1_10("Frostburn Update", "June 8, 2016", ProtocolVersion.v1_10),
    R1_9_3("Combat Update", "May 10, 2016", ProtocolVersion.v1_9_3),
    R1_9_2("Combat Update", "March 30, 2016", ProtocolVersion.v1_9_2),
    R1_9_1("Combat Update", "March 30, 2016", ProtocolVersion.v1_9_1),
    R1_9("Combat Update", "February 29, 2016", ProtocolVersion.v1_9),
    R1_8("Bountiful Update", "September 2, 2014", ProtocolVersion.v1_8),
    R1_7_6("The Update that Changed the World", "April 9, 2014", ProtocolVersion.v1_7_6),
    R1_7_2("The Update that Changed the World", "October 25, 2013", ProtocolVersion.v1_7_2);

    private final String name;
    private final String releaseDate;
    private final ProtocolVersion protocolVersion;

    private ProtocolInfo(final String name, final String releaseDate, final ProtocolVersion protocolVersion) {
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
        for (ProtocolInfo protocolInfo : ProtocolInfo.values()) {
            if (protocolInfo.getProtocolVersion().getName().equals(protocolVersion.getName())) {
                return protocolInfo;
            }
        }
        return null;
    }
}
