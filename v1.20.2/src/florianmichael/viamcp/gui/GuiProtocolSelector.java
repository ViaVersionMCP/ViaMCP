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

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.protocolinfo.ProtocolInfo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import slice.Slice;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class GuiProtocolSelector extends Screen {
    private final Screen parent;
    private Button back;
    public SlotList list;

    public GuiProtocolSelector(Screen parent) {
        super(Component.text("ViaMCP Protocol Selector"));

        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(this.back = Button.builder(Component.text("Back"), button -> {
                    assert this.minecraft != null;
                    this.minecraft.setScreen(parent);
                })
                .bounds(width / 2 - 100, height - 25, 200, 20).build());
        list = new SlotList(Minecraft.getInstance(), width - 20, height, 57, height - 32, 20);
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        if(back.isHovered()) {
            return super.mouseClicked(p_94695_, p_94696_, p_94697_);
        }

        list.mouseClicked(p_94695_, p_94696_, p_94697_);
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_, double p_299502_) {
        if(back.isHovered()) {
            return super.mouseScrolled(p_94686_, p_94687_, p_94688_, p_299502_);
        }

        list.mouseScrolled(p_94686_, p_94687_, p_94688_, p_299502_);
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_, p_299502_);
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        if(back.isHovered()) {
            return super.mouseReleased(p_94722_, p_94723_, p_94724_);
        }

        list.mouseReleased(p_94722_, p_94723_, p_94724_);
        return super.mouseReleased(p_94722_, p_94723_, p_94724_);
    }

    @Override
    public boolean keyPressed(int p_96552_, int p_96553_, int p_96554_) {
        long window = Minecraft.getInstance().getWindow().getWindow();

        if(InputConstants.isKeyDown(window, InputConstants.KEY_UP)) {
            list.setScrollAmount(list.getScrollAmount() - 20);
            return true;
        } else if(InputConstants.isKeyDown(window, InputConstants.KEY_DOWN)) {
            list.setScrollAmount(list.getScrollAmount() + 20);
            return true;
        } else if(InputConstants.isKeyDown(window, InputConstants.KEY_TAB)) {
            int index = ViaLoadingBase.PROTOCOLS.indexOf(ViaLoadingBase.getInstance().getTargetVersion());

            if(InputConstants.isKeyDown(window, InputConstants.KEY_LSHIFT)) {
                if(index <= 0) index = ViaLoadingBase.PROTOCOLS.size() - 1;
                else index--;
            } else {
                if(index >= ViaLoadingBase.PROTOCOLS.size() - 1) index = 0;
                else index++;
            }

            final ProtocolVersion version = ViaLoadingBase.PROTOCOLS.get(index);
            ViaLoadingBase.getInstance().reload(version);

            list.setScrollAmount(index * 20);
            return true;
        }
        return super.keyPressed(p_96552_, p_96553_, p_96554_);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int p_281550_, int p_282878_, float p_282465_) {
        graphics.pose().pushPose();
        graphics.pose().scale(2.0F, 2.0F, 2.0F);

        String title = "§lViaMCP";
        Font font = Minecraft.getInstance().font;

        graphics.drawString(font, title, (this.width - (font.width(title) * 2)) / 4, 5, -1);
        graphics.pose().popPose();

        graphics.drawString(font, "by EnZaXD/Flori2007", 1, 1, -1);
        graphics.drawString(font, "Discord: @florianmichael", 1, 11, -1);

        graphics.drawString(font, "Updated by NickRest", 1, 21, -1);
        graphics.drawString(font, "Discord: @nickrest", 1, 31, -1);

        final ProtocolInfo protocolInfo = ProtocolInfo.fromProtocolVersion(ViaLoadingBase.getInstance().getTargetVersion());

        final String versionTitle = "Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName() + " - " + protocolInfo.getName();
        final String versionReleased = "Released: " + protocolInfo.getReleaseDate();

        final int fixedHeight = ((5 + font.lineHeight) * 2) + 2;

        graphics.drawString(font, "§7§lVersion Information", (width - font.width("Version Information")) / 2, fixedHeight, -1);
        graphics.drawString(font, versionTitle, (width - font.width(versionTitle)) / 2, fixedHeight + font.lineHeight, -1);
        graphics.drawString(font, versionReleased, (width - font.width(versionReleased)) / 2, fixedHeight + font.lineHeight * 2, -1);

        list.render(graphics, p_281550_, p_282878_, p_282465_);
        super.render(graphics, p_281550_, p_282878_, p_282465_);
    }

    abstract static class VersionEntry extends ContainerObjectSelectionList.Entry<GuiProtocolSelector.VersionEntry>{};

    static class SlotList extends ContainerObjectSelectionList<GuiProtocolSelector.VersionEntry> {

        public SlotList(Minecraft p_94010_, int p_94011_, int p_94012_, int p_94013_, int p_94014_, int p_94015_) {
            super(p_94010_, p_94011_, p_94012_, p_94013_, p_94014_, p_94015_);
        }

        @Override
        protected int getItemCount() {
            return ViaLoadingBase.PROTOCOLS.size();
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public void setRenderBackground(boolean p_93489_) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.minecraft.getTextureManager().bindForSetup(EditGameRulesScreen.BACKGROUND_LOCATION);
            blit(0, 0, 0, 0, this.width, this.height);
        }

        @Override
        public void render(GuiGraphics p_282708_, int p_283242_, int p_282891_, float p_283683_) {
            if (this.renderBackground) {
                p_282708_.setColor(0.125F, 0.125F, 0.125F, 1.0F);
                int i = 32;
                p_282708_.blit(Screen.BACKGROUND_LOCATION, this.x0, this.y0, (float)this.x1, (float)(this.y1 + (int)this.getScrollAmount()), this.x1 - this.x0, this.y1 - this.y0, 32, 32);
                p_282708_.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            this.enableScissor(p_282708_);
            if (this.renderHeader) {
                int i1 = this.getRowLeft();
                int j = this.y0 + 4 - (int)this.getScrollAmount();
                this.renderHeader(p_282708_, i1, j);
            }

            this.renderList(p_282708_, p_283242_, p_282891_, p_283683_);
            p_282708_.disableScissor();
            if (this.renderBackground) {
                int j1 = 4;
                p_282708_.fillGradient(RenderType.guiOverlay(), this.x0, this.y0, this.x1, this.y0 + 4, -16777216, 0, 0);
                p_282708_.fillGradient(RenderType.guiOverlay(), this.x0, this.y1 - 4, this.x1, this.y1, 0, -16777216, 0);
            }

            int k1 = this.getMaxScroll();
            if (k1 > 0) {
                int l1 = this.getScrollbarPosition();
                int k = (int)((float)((this.y1 - this.y0) * (this.y1 - this.y0)) / (float)this.getMaxPosition());
                k = Mth.clamp(k, 32, this.y1 - this.y0 - 8);
                int l = (int)this.getScrollAmount() * (this.y1 - this.y0 - k) / k1 + this.y0;
                if (l < this.y0) {
                    l = this.y0;
                }

                p_282708_.fill(l1, this.y0, l1 + 6, this.y1, -16777216);
                p_282708_.blitSprite(new ResourceLocation("widget/scroller"), l1, l, 6, k);
            }

            this.renderDecorations(p_282708_, p_283242_, p_282891_);
            RenderSystem.disableBlend();
        }

        @Override
        protected void renderItem(@NotNull GuiGraphics graphics, int p_238966_, int p_238967_, float p_238968_, int idkWhatThisIs, int p_238970_, int p_238971_, int p_238972_, int p_238973_) {

            Font font = Minecraft.getInstance().font;
            try {
                graphics.drawString(font, (ViaLoadingBase.PROTOCOLS.indexOf(ViaLoadingBase.getInstance().getTargetVersion()) == idkWhatThisIs ? "§a§l" : "§7") + ViaLoadingBase.PROTOCOLS.get(idkWhatThisIs).getName(), width / 2, p_238971_ + 2, -1);

                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();
                poseStack.scale(0.5F, 0.5F, 0.5F);
                graphics.drawString(font, "PVN: " + ViaLoadingBase.PROTOCOLS.get(idkWhatThisIs).getVersion(), width, (p_238971_ + 2) * 2 + 20, -1);
                poseStack.popPose();
            } catch (Exception e) {
                e.printStackTrace(); // don't crash just tell me please
            }
        }

        @Override
        public boolean mouseClicked(double p_93420_, double p_93421_, int p_93422_) {
            final ProtocolVersion version = getVersionFromFound(p_93420_, p_93421_);
            ViaLoadingBase.getInstance().reload(version);

            return false; // if I return super it crashses
        }

        public ProtocolVersion getVersionFromFound(double x, double y) {
            int scroll = (int) getScrollAmount();

            for (int i = 0; i < getItemCount(); i++) {
                int y1 = y0 + 4 + (i * 20) - scroll;
                int y2 = y0 + 4 + (i * 20) + 20 - scroll;

                if (y >= y1 && y <= y2) {
                    return ViaLoadingBase.PROTOCOLS.get(i);
                }
            }

            return null;
        }

        public void blit(int p_94017_, int p_94018_, int p_94019_, int p_94020_, int p_94021_, int p_94022_) {
            float f = 0.00390625F;
            float f1 = 0.00390625F;
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(p_94017_, p_94018_ + p_94022_, 0.0D).uv(p_94019_ * f, (p_94020_ + p_94022_) * f1).endVertex();
            bufferbuilder.vertex(p_94017_ + p_94021_, p_94018_ + p_94022_, 0.0D).uv((p_94019_ + p_94021_) * f, (p_94020_ + p_94022_) * f1).endVertex();
            bufferbuilder.vertex(p_94017_ + p_94021_, p_94018_, 0.0D).uv((p_94019_ + p_94021_) * f, p_94020_ * f1).endVertex();
            bufferbuilder.vertex(p_94017_, p_94018_, 0.0D).uv(p_94019_ * f, p_94020_ * f1).endVertex();
            bufferbuilder.end();
            BufferUploader.reset();
        }
    }


}
