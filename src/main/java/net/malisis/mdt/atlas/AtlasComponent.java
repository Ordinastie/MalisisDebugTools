/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.mdt.atlas;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.logging.log4j.core.helpers.Strings;
import org.lwjgl.opengl.GL11;

import net.malisis.core.MalisisCore;
import net.malisis.core.asm.AsmUtils;
import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.util.MouseButton;
import net.malisis.core.util.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * @author Ordinastie
 *
 */
@SuppressWarnings("unchecked")
public class AtlasComponent extends UIContainer<AtlasComponent>
{
	private static Map<String, TextureAtlasSprite> ICONS;

	static
	{
		try
		{
			Field field = AsmUtils.changeFieldAccess(TextureMap.class, "mapRegisteredSprites", "field_110574_e");
			TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();

			ICONS = (Map<String, TextureAtlasSprite>) field.get(map);
		}
		catch (ReflectiveOperationException e)
		{
			MalisisCore.log.error("Failed to get TextureMap.mapRegisteredSprites field : ", e);
		}
	}

	private float factor = -1;
	private int px = 0;
	private int py = 0;

	private float u;
	private float v;
	private TextureAtlasSprite icon;

	private String filter = "";

	public AtlasComponent(MalisisGui gui)
	{
		super(gui);
		setClipContent(true);
	}

	private void updateHoveredData(int x, int y)
	{
		float rw = Icon.BLOCK_TEXTURE_WIDTH * factor;
		float rh = Icon.BLOCK_TEXTURE_HEIGHT * factor;
		u = (relativeX(x) - px) / rw;
		v = (relativeY(y) - py) / rh;
		icon = updateHoveredIcon();
	}

	public TextureAtlasSprite updateHoveredIcon()
	{
		if (ICONS == null)
			return null;
		if (u < 0 || u > 1 || v < 0 || v > 1)
			return null;

		for (TextureAtlasSprite icon : ICONS.values())
		{
			if (icon.getMinU() < u && icon.getMaxU() > u && icon.getMinV() < v && icon.getMaxV() > v)
				return icon;
		}

		return null;
	}

	public Point getHoveredUVs()
	{
		if (!isHovered())
			return null;
		if (u < 0 || u > 1 || v < 0 || v > 1)
			return null;
		return new Point(u, v, 0);
	}

	public TextureAtlasSprite getHoveredIcon()
	{
		if (!isHovered())
			return null;
		return icon;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}

	private void setupShape(TextureAtlasSprite icon, int w, int h)
	{
		float rw = Icon.BLOCK_TEXTURE_WIDTH * factor;
		float rh = Icon.BLOCK_TEXTURE_HEIGHT * factor;
		float minX = icon.getMinU() * rw + px;
		float maxX = icon.getMaxU() * rw + px;
		float minY = icon.getMinV() * rh + py;
		float maxY = icon.getMaxV() * rh + py;

		shape.resetState();
		shape.getFaces()[0].scale(maxX - minX, maxY - minY, 0);
		shape.getFaces()[0].translate(minX, minY, 0);

	}

	@Override
	public void drawBackground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		renderer.disableTextures();

		rp.colorMultiplier.set(0x666666);
		shape.resetState();
		shape.setSize(getWidth(), getHeight());
		renderer.drawShape(shape, rp);
	}

	@Override
	public void drawForeground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		int w = Icon.BLOCK_TEXTURE_WIDTH;
		int h = Icon.BLOCK_TEXTURE_HEIGHT;
		if (factor == -1)
		{
			//set factor to fit the altas in the component
			factor = h > w ? getHeight() / (float) h : getWidth() / (float) w;
		}

		w *= factor;
		h *= factor;

		drawAtlas(renderer, w, h);
		drawHoveredIcon(renderer, w, h);
		drawFilter(renderer, w, h);
	}

	private void drawAtlas(GuiRenderer renderer, int w, int h)
	{
		rp.colorMultiplier.reset();

		shape.resetState();
		shape.setSize(w, h);
		//shape.setSize(150, 150);
		shape.setPosition(px, py);
		renderer.drawShape(shape, rp);
		renderer.next();
		renderer.enableTextures();

		renderer.bindTexture(MalisisGui.BLOCK_TEXTURE);
		shape.resetState();
		shape.setSize(w, h);
		shape.setPosition(px, py);
		renderer.drawShape(shape, rp);
		renderer.next();
	}

	private void drawHoveredIcon(GuiRenderer renderer, int w, int h)
	{
		if (icon == null)
			return;
		renderer.next(GL11.GL_LINE_LOOP);
		renderer.disableTextures();
		GL11.glLineWidth(2);
		rp.colorMultiplier.set(0x33AA33);

		setupShape(icon, w, h);
		renderer.drawShape(shape, rp);

		renderer.next();
		renderer.enableTextures();
		renderer.next(GL11.GL_QUADS);
	}

	private void drawFilter(GuiRenderer renderer, int w, int h)
	{
		if (ICONS == null)
			return;
		if (Strings.isEmpty(filter))
			return;

		renderer.next();
		renderer.disableTextures();
		rp.colorMultiplier.set(0xFFFFFF);
		rp.alpha.set(200);
		for (TextureAtlasSprite icon : ICONS.values())
		{
			if (!icon.getIconName().contains(filter))
			{
				setupShape(icon, w, h);
				renderer.drawShape(shape, rp);
			}
		}

		renderer.next();
		renderer.enableTextures();
	}

	@Override
	public boolean onScrollWheel(int x, int y, int delta)
	{
		if (delta < 0)
			factor *= .9F;
		else
			factor *= 1.1F;

		return true;
	}

	@Override
	public boolean onDrag(int lastX, int lastY, int x, int y, MouseButton button)
	{
		if (button != MouseButton.LEFT)
			return false;

		px += x - lastX;
		py += y - lastY;

		return true;
	}

	@Override
	public boolean onButtonRelease(int x, int y, MouseButton button)
	{
		if (button == MouseButton.MIDDLE)
		{
			factor = -1;
			px = 0;
			py = 0;
		}

		return true;
	}

	@Override
	public boolean onMouseMove(int lastX, int lastY, int x, int y)
	{
		updateHoveredData(x, y);
		return true;
	}

}
