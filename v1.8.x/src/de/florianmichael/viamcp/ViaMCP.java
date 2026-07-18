/*
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
import com.viaversion.viabackwards.protocol.v1_20_3to1_20_2.Protocol1_20_3To1_20_2;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viabackwards.protocol.v1_17to1_16_4.storage.PlayerLastCursorItem;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_9;


import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.gui.AsyncVersionSlider;

import java.io.File;

public class ViaMCP {
    public final static int NATIVE_VERSION = 47;
    public static ViaMCP INSTANCE;
    public UserConnection user;
    
    private static final Queue<PacketWrapper> confirmations = new ConcurrentLinkedQueue<>();
    private static Minecraft mc = Minecraft.getMinecraft();
    
    public static void create() {
        INSTANCE = new ViaMCP();
    }

    private AsyncVersionSlider asyncVersionSlider;

    public ViaMCP() {
        ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File("ViaMCP")).nativeVersion(NATIVE_VERSION).onProtocolReload(protocolVersion -> {
            if (getAsyncVersionSlider() != null) {
                getAsyncVersionSlider().setVersion(protocolVersion.getVersion());
            }
        }).build();
    }
    
    public void applyFix() {
    	fixTransactions();
    	fixHypixelLogin();
    	fix1_17CursorItem();
    	fixTeleportId();
		// fix26_2Attributes();
    }

    private void fixTransactions() {
        // We handle the differences between those versions in the net code, so we can make the Via handlers pass through
        final Protocol1_17To1_16_4 protocol = Via.getManager().getProtocolManager().getProtocol(Protocol1_17To1_16_4.class);
        protocol.registerClientbound(ClientboundPackets1_17.PING, ClientboundPackets1_16_2.CONTAINER_ACK, wrapper -> {}, true);
        protocol.registerServerbound(ServerboundPackets1_16_2.CONTAINER_ACK, ServerboundPackets1_17.PONG, wrapper -> {}, true);
    }
    
    private void fixHypixelLogin() {
    	Protocol1_20_3To1_20_2 protocol1_20_3To1_20_2 = Via.getManager().getProtocolManager().getProtocol(Protocol1_20_3To1_20_2.class);
        protocol1_20_3To1_20_2.registerServerbound(State.LOGIN, ServerboundLoginPackets.LOGIN_ACKNOWLEDGED, packetWrapper -> {
            this.user = packetWrapper.user();
        });
        protocol1_20_3To1_20_2.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO, packetWrapper -> {
        	packetWrapper.cancel();
        	PacketWrapper packet = PacketWrapper.create(ServerboundLoginPackets.HELLO, packetWrapper.user());
        	GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
        	packet.write(Types.STRING, profile.getName());
        	UUID uuid = profile.getId();
        	packet.write(Types.UUID, profile.getId());
            packet.sendToServer(Protocol1_20_3To1_20_2.class);
        });
    }
    
    private void fix1_17CursorItem() {
    	ProtocolManager protocolManager = Via.getManager().getProtocolManager();
    	Protocol1_17To1_16_4 protocol1_17To1_16_4 = protocolManager.getProtocol(Protocol1_17To1_16_4.class);
    	protocol1_17To1_16_4.replaceServerbound(ServerboundPackets1_16_2.CONTAINER_CLICK, new PacketHandlers() {
            @Override
            public void register() {
                map(Types.BYTE);
                handler(wrapper -> {
                    short slot = wrapper.passthrough(Types.SHORT);
                    byte button = wrapper.passthrough(Types.BYTE);
                    wrapper.read(Types.SHORT);
                    int mode = wrapper.passthrough(Types.VAR_INT);

                    Item clicked = protocol1_17To1_16_4.getItemRewriter()
                            .handleItemToServer(wrapper.user(), wrapper.read(Types.ITEM1_13_2));

                    wrapper.write(Types.VAR_INT, 0);

                    PlayerLastCursorItem state = wrapper.user().get(PlayerLastCursorItem.class);
                    if (state == null) {
                        wrapper.write(Types.ITEM1_13_2, clicked);
                        return;
                    }

                    if (mode == 0 && button == 0 && clicked != null) {
                        state.setLastCursorItem(clicked);
                    } else if (mode == 0 && button == 1 && clicked != null) {
                        if (state.isSet()) {
                            state.setLastCursorItem(clicked);
                        } else {
                            state.setLastCursorItem(clicked, (clicked.amount() + 1) / 2);
                        }
                    } else if (!(mode == 5 && (slot == -999 && (button == 0 || button == 4) || (button == 1 || button == 5)))) {
                        state.setLastCursorItem(null);
                    }

                    Item carried = state.getLastCursorItem();
                    wrapper.write(Types.ITEM1_13_2, carried == null ? clicked : carried);
                });
            }
        });
    }
    
    private void fixTeleportId() {
    	ProtocolManager protocolManager = Via.getManager().getProtocolManager();
        Protocol1_9To1_8 protocol1_9To1_8 = protocolManager.getProtocol(Protocol1_9To1_8.class);
        
        if (mc.isSingleplayer() || protocol1_9To1_8 == null || protocol1_17To1_16_4 == null) {
            System.out.println("VIA crash! Protocols not yet initialized.");
            return;
        }

        ClientboundPackets1_9 packetType = ClientboundPackets1_9.PLAYER_POSITION;

        PacketHandlers playerPositionHandler = new PacketHandlers() {
            @Override
            public void register() {
                map(Types.DOUBLE);
                map(Types.DOUBLE);
                map(Types.DOUBLE);
                map(Types.FLOAT);
                map(Types.FLOAT);
                map(Types.BYTE);
                handler(wrapper -> {
                    int id = wrapper.read(Types.VAR_INT);
                    PacketWrapper c = PacketWrapper.create(ServerboundPackets1_9.ACCEPT_TELEPORTATION, wrapper.user());
                    c.write(Types.VAR_INT, id);
                    confirmations.offer(c);

                    PlayerPositionTracker tracker = wrapper.user().get(PlayerPositionTracker.class);
                    if (tracker != null) {
                        tracker.setConfirmId(id);

                        byte flags = wrapper.get(Types.BYTE, 0);
                        double x = wrapper.get(Types.DOUBLE, 0);
                        double y = wrapper.get(Types.DOUBLE, 1);
                        double z = wrapper.get(Types.DOUBLE, 2);
                        float yaw = wrapper.get(Types.FLOAT, 0);
                        float pitch = wrapper.get(Types.FLOAT, 1);

                        wrapper.set(Types.BYTE, 0, (byte) 0);

                        if (flags != 0) {
                            if ((flags & 0x01) != 0) { x += tracker.getPosX(); wrapper.set(Types.DOUBLE, 0, x); }
                            if ((flags & 0x02) != 0) { y += tracker.getPosY(); wrapper.set(Types.DOUBLE, 1, y); }
                            if ((flags & 0x04) != 0) { z += tracker.getPosZ(); wrapper.set(Types.DOUBLE, 2, z); }
                            if ((flags & 0x08) != 0) { yaw += tracker.getYaw(); wrapper.set(Types.FLOAT, 0, yaw); }
                            if ((flags & 0x10) != 0) { pitch += tracker.getPitch(); wrapper.set(Types.FLOAT, 1, pitch); }
                        }

                        tracker.setPos(x, y, z);
                        tracker.setYaw(yaw);
                        tracker.setPitch(pitch);
                    }
                });
            }
        };

        // FIX: Defensively bypass the async registration race condition
        try {
            if (protocol1_9To1_8.hasRegisteredClientbound(packetType)) {
                protocol1_9To1_8.replaceClientbound(packetType, playerPositionHandler);
            } else {
                protocol1_9To1_8.registerClientbound(com.viaversion.viaversion.api.protocol.packet.State.PLAY, packetType, playerPositionHandler);
            }
        } catch (IllegalArgumentException e) {
            // If it was registered by the mapping loader thread in the split second we checked, replace it instead
            protocol1_9To1_8.replaceClientbound(packetType, playerPositionHandler);
        }

        // FIX: Defensively check if serverbound handler exists before attempting replacement
        PacketHandler serverboundHandler = wrapper -> {
            PacketWrapper c = confirmations.poll();
            if (c != null) {
                c.sendToServer(Protocol1_9To1_8.class);
            }

            double x = wrapper.passthrough(Types.DOUBLE);
            double y = wrapper.passthrough(Types.DOUBLE);
            double z = wrapper.passthrough(Types.DOUBLE);
            float yaw = wrapper.passthrough(Types.FLOAT);
            float pitch = wrapper.passthrough(Types.FLOAT);
            boolean onGround = wrapper.passthrough(Types.BOOLEAN);
            PlayerPositionTracker tracker = wrapper.user().get(PlayerPositionTracker.class);
            if (tracker != null) {
                tracker.sendAnimations();
                if (tracker.getConfirmId() != -1) {
                    if (tracker.getPosX() == x &&
                            tracker.getPosY() == y &&
                            tracker.getPosZ() == z &&
                            tracker.getYaw() == yaw &&
                            tracker.getPitch() == pitch) {
                        tracker.setConfirmId(-1);
                    }
                } else {
                    tracker.setPos(x, y, z);
                    tracker.setYaw(yaw);
                    tracker.setPitch(pitch);
                    tracker.setOnGround(onGround);
                    BossBarStorage storage = wrapper.user().get(BossBarStorage.class);
                    if (storage != null) {
                        storage.updateLocation();
                    }
                }
            }
        };

        if (protocol1_9To1_8.hasRegisteredServerbound(ServerboundPackets1_8.MOVE_PLAYER_POS_ROT)) {
            protocol1_9To1_8.replaceServerbound(ServerboundPackets1_8.MOVE_PLAYER_POS_ROT, serverboundHandler);
        } else {
            protocol1_9To1_8.registerServerbound(com.viaversion.viaversion.api.protocol.packet.State.PLAY, ServerboundPackets1_8.MOVE_PLAYER_POS_ROT, serverboundHandler);
        }
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

    public void initAsyncSlider() {
        this.initAsyncSlider(5, 5, 110, 20);
    }

    public void initAsyncSlider(int x, int y, int width, int height) {
        asyncVersionSlider = new AsyncVersionSlider(-1, x, y, Math.max(width, 110), height);
    }

    public AsyncVersionSlider getAsyncVersionSlider() {
        return asyncVersionSlider;
    }
}
