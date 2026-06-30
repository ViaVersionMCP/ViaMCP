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

package de.florianmichael.viamcp.fixes;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;

public class AttackOrder {
	
	private static final Minecraft mc = Minecraft.getInstance();

    public static void sendConditionalSwing(HitResult ray, InteractionHand enumHand) {
        if (ray != null && ray.getType() != HitResult.Type.ENTITY) mc.player.swing(enumHand);
    }

    public static void sendFixedAttack(Player entityIn, Entity target, InteractionHand enumHand) {
        if (ViaLoadingBase.getInstance().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8)) {
        	if(enumHand != null) {
        		mc.player.swing(InteractionHand.MAIN_HAND);
        	}
            mc.gameMode.attack(entityIn, target);
        } else {
            mc.gameMode.attack(entityIn, target);
        	if(enumHand != null) {
        		mc.player.swing(enumHand);
        	}
        }
    }
}
