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

package de.florianmichael.viamcp.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.joml.Matrix3x2fStack;

import com.mojang.blaze3d.platform.InputConstants;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.protocolinfo.ProtocolInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class GuiProtocolSelector extends Screen {
	
    private final Screen parent;
    public SlotList list;

    public GuiProtocolSelector(Screen parent) {
    	super(CommonComponents.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init() {
        addRenderableWidget(Button.builder(Component.literal("Back"), (action) -> {
            minecraft.setScreen(parent);
        }).bounds(width / 2 - 100, height - 25, 200, 20).build());
        list = new SlotList(minecraft, width, height - 96, 64, 21);
        List<Object> protocols = Arrays.asList(ProtocolVersion.getProtocols().toArray());
        Collections.reverse(protocols);
        for(Object obj: protocols) {
        	if(obj instanceof ProtocolVersion version) {
        		list.addEntry(new Protocol(version));
        	}
        }
        list.setSelected(list.children().get(list.children().indexOf(new Protocol(ViaLoadingBase.getInstance().getTargetVersion()))));
        addWidget(list);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    	Matrix3x2fStack pose = graphics.pose();
        list.render(graphics, mouseX, mouseY, partialTicks);
        pose.pushMatrix();
        pose.scale(2.0f, 2.0f);
        String title = ChatFormatting.BOLD + "ViaMCP";
        graphics.drawString(font, title, (this.width - (this.font.width(title) * 2)) / 4, 5, -1);
        pose.popMatrix();
        
        graphics.drawString(this.font, "by EnZaXD/Flori2007", 1, 1, -1);
        graphics.drawString(this.font, "Discord: EnZaXD#6257", 1, 11, -1);

        ProtocolInfo protocolInfo = ProtocolInfo.fromProtocolVersion(ViaLoadingBase.getInstance().getTargetVersion());

        final String versionTitle = "Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName() + " - " + protocolInfo.getName();
        final String versionReleased = "Released: " + protocolInfo.getReleaseDate();

        final int fixedHeight = ((5 + Font.LINE_HEIGHT) * 2) + 2;

        graphics.drawString(this.font, ChatFormatting.GRAY + (ChatFormatting.BOLD + "Version Information"), (width - this.font.width("Version Information")) / 2, fixedHeight, -1);
        graphics.drawString(this.font, versionTitle, (width - this.font.width(versionTitle)) / 2, fixedHeight + Font.LINE_HEIGHT, -1);
        graphics.drawString(this.font, versionReleased, (width - this.font.width(versionReleased)) / 2, fixedHeight + Font.LINE_HEIGHT * 2, -1);
    }

    class SlotList extends ObjectSelectionList<Protocol> {
    	
        public SlotList(Minecraft mc, int width, int height, int top, int itemHeight) {
            super(mc, width, height, top, itemHeight);
        }
        
        @Override
        protected int addEntry(Protocol pEntry) {
        	return super.addEntry(pEntry);
    	}
        
        private void selectVersion(ProtocolVersion version) {
			ViaLoadingBase.getInstance().reload(version);
			minecraft.options.versionSlider.set(ProtocolVersion.getProtocols().indexOf(version));
        }
        
        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean isDouble) {
        	// TODO Auto-generated method stub
        	for(Protocol entry: children()) {
        		if(entry.isMouseOver(event.x(), event.y())) {
        			setSelected(entry);
        			if(isDouble) {
        				selectVersion(entry.version);
        			}
        			break;
        		}
        	}
        	return super.mouseClicked(event, isDouble);
        }
        
        @Override
        public boolean keyPressed(KeyEvent event) {
        	// TODO Auto-generated method stub
        	if(event.key() == InputConstants.KEY_NUMPADENTER || event.key() == InputConstants.KEY_RETURN) {
        		selectVersion(getSelected().version);
        	}
        	return super.keyPressed(event);
        }
        
        @Nullable
    	@Override
    	public ComponentPath nextFocusPath(FocusNavigationEvent p_265150_)
    	{
    		if (this.getItemCount() == 0)
    		{
    			return null;
    		}
    		else if (this.isFocused() && p_265150_ instanceof FocusNavigationEvent.ArrowNavigation focusnavigationevent$arrownavigation)
    		{
    			Protocol e1 = this.nextEntry(focusnavigationevent$arrownavigation.direction());
    			if (e1 != null)
    			{
    				return ComponentPath.path(this, ComponentPath.leaf(e1));
    			}
    			else
    			{
    				return null;
    			}
    		}
    		else if (!this.isFocused())
    		{
    			Protocol e = this.getSelected();
    			if (e == null)
    			{
    				e = this.nextEntry(p_265150_.getVerticalDirectionForInitialFocus());
    			}

    			return e == null ? null : ComponentPath.path(this, ComponentPath.leaf(e));
    		}
    		else
    		{
    			return null;
    		}
    	}
    }
    
    private class SlotEntry extends ObjectSelectionList.Entry<Protocol> {

		@Override
		public Component getNarration() {
			// TODO Auto-generated method stub
			return CommonComponents.EMPTY;
		}

		@Override
		public void renderContent(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, boolean pIsHovering, float pPartialTick) {
			// TODO Auto-generated method stub
			Matrix3x2fStack pose = pGuiGraphics.pose();
			Protocol protocol = new Protocol(ViaLoadingBase.getInstance().getTargetVersion());
			pGuiGraphics.drawCenteredString(font, (list.children().indexOf(protocol) == getIndex() ? ChatFormatting.GREEN.toString() + ChatFormatting.BOLD : ChatFormatting.GRAY.toString()) + list.children().get(getIndex()).version.getName(), width / 2, this.getContentY() + 2, -1);
			pose.pushMatrix();
			pose.scale(0.5f, 0.5f);
			pGuiGraphics.drawCenteredString(font, "PVN: " + list.children().get(getIndex()).version.getVersion(), width, (this.getContentY() + 2) * 2 + 20, -1);
			pose.popMatrix();
		}
    }
    
    class Protocol extends SlotEntry {
    	
    	private final ProtocolVersion version;
    	
    	public Protocol(ProtocolVersion version) {
    		this.version = version;
			// TODO Auto-generated constructor stub
		}
    	
    	@Override
    	public boolean equals(Object obj) {
    		// TODO Auto-generated method stub
    		return obj instanceof Protocol p && version.equals(p.version);
    	}
    }
}
