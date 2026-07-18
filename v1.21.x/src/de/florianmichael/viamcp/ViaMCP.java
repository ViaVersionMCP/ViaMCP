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

package de.florianmichael.viamcp;

import com.viaversion.viabackwards.protocol.v1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.v1_16_1to1_16_2.packet.ClientboundPackets1_16_2;
import com.viaversion.viaversion.protocols.v1_16_1to1_16_2.packet.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.packet.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.packet.ServerboundPackets1_17;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

import java.io.File;

public class ViaMCP {
	
    public final static int NATIVE_VERSION = SharedConstants.getProtocolVersion();
    public static ViaMCP INSTANCE;

    public static void create() {
        INSTANCE = new ViaMCP();
    }

    public ViaMCP() {
        ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File("ViaMCP")).nativeVersion(NATIVE_VERSION).forceNativeVersionCondition(() -> Minecraft.getInstance().hasSingleplayerServer()).onProtocolReload(protocolVersion -> {
     
        }).build();

        // Add this line if you implement the transaction fixes into the game code
        // fixTransactions();
    }

    private void fixTransactions() {
        // We handle the differences between those versions in the net code, so we can make the Via handlers pass through
        final Protocol1_17To1_16_4 protocol = Via.getManager().getProtocolManager().getProtocol(Protocol1_17To1_16_4.class);
        protocol.registerClientbound(ClientboundPackets1_17.PING, ClientboundPackets1_16_2.CONTAINER_ACK, wrapper -> {}, true);
        protocol.registerServerbound(ServerboundPackets1_16_2.CONTAINER_ACK, ServerboundPackets1_17.PONG, wrapper -> {}, true);
    }
	
	private void fix26_2Attributes() {
    	Protocol26_2To26_1 protocol26_2To26_1 = Via.getManager().getProtocolManager().getProtocol(Protocol26_2To26_1.class);
    	protocol26_2To26_1.registerClientbound(ClientboundPackets26_1.UPDATE_ATTRIBUTES, ClientboundPackets26_1.UPDATE_ATTRIBUTES, handler -> {
    		int entityId = handler.passthrough(Types.VAR_INT);
    		int size = handler.passthrough(Types.VAR_INT);
    		int newSize = size;

			// You should make a tool to save new attributes according to entityId
			// For example:
			// NewAttributes data = NewAttributes.get(entityId);
			
    		for (int i = 0; i < size; i++) {
    			int attributeId = handler.read(Types.VAR_INT);
    			int mappedId = protocol26_2To26_1.getMappingData().getNewAttributeId(attributeId);
    			String attributeKey = protocol26_2To26_1.getMappingData().getAttributeMappings().identifier(attributeId);
    			
    			double base = handler.read(Types.DOUBLE);
    			int modifierSize = handler.read(Types.VAR_INT);

    			double addValue = 0.0D;
    			double addMultipliedBase = 0.0D;
    			double multipliedTotal = 1.0D;

    			if (mappedId == -1) {
    				newSize--;
    			} else {
    				handler.write(Types.VAR_INT, mappedId);
    				handler.write(Types.DOUBLE, base);
    				handler.write(Types.VAR_INT, modifierSize);
    			}

    			for (int j = 0; j < modifierSize; j++) {
    				String modifierId = handler.read(Types.STRING);
    				double amount = handler.read(Types.DOUBLE);
    				byte operation = handler.read(Types.BYTE);

    				if (operation == 0) {
    					addValue += amount;
    				} else if (operation == 1) {
    					addMultipliedBase += amount;
    				} else if (operation == 2) {
    					multipliedTotal *= 1.0D + amount;
    				}

    				if (mappedId != -1) {
    					handler.write(Types.STRING, modifierId);
    					handler.write(Types.DOUBLE, amount);
    					handler.write(Types.BYTE, operation);
    				}
    			}

    			double finalValue = (base + addValue + base * addMultipliedBase) * multipliedTotal;

    			if ("minecraft:air_drag_modifier".equals(attributeKey)) {
    				// data.airDrag = MathUtil.clamp(finalValue, 0.0D, 2048.0D); // air drag, same as block friction but in air.
    			} else if ("minecraft:bounciness".equals(attributeKey)) {
    				// data.bounciness = MathUtil.clamp(finalValue, 0.0D, 1.0D); // block bounciness
    			} else if ("minecraft:friction_modifier".equals(attributeKey)) {
    				// data.friction = MathUtil.clamp(finalValue, 0.0D, 2048.0D); // block friction, explains how fast player can move on blocks.
    			}
    		}

    		if (size != newSize) {
    			handler.set(Types.VAR_INT, 1, newSize);
    		}
    	}, true);
    }
}
