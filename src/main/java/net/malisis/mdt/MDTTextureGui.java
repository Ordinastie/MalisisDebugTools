/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
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

package net.malisis.mdt;

import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.renderer.font.FontRenderOptions;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.util.MouseButton;

import org.lwjgl.opengl.Display;

/**
 * @author Ordinastie
 *
 */
public class MDTTextureGui extends MalisisGui
{
	@Override
	public void construct()
	{
		addToScreen(new TextureDebug(this));
	}

	public static class TextureDebug extends UIComponent<TextureDebug>
	{
		private int size = -1;
		private float factor = 1;
		private int px = 0;
		private int py = 0;
		private float atlasWidth = 1;
		private float atlasHeight = 1;

		public TextureDebug(MalisisGui gui)
		{
			super(gui);
			atlasWidth = -1;
			atlasHeight = -1;

		}

		@Override
		public void drawBackground(net.malisis.core.client.gui.GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
		{};

		@Override
		public void drawForeground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
		{
			if (size == -1)
				size = Display.getHeight() / getGui().getRenderer().getScaleFactor();

			float atlasFactor = Icon.BLOCK_TEXTURE_WIDTH / Icon.BLOCK_TEXTURE_HEIGHT;

			renderer.disableTextures();
			shape.resetState();
			shape.setSize((int) (size * factor * atlasFactor), (int) (size * factor));
			//shape.setSize(150, 150);
			shape.setPosition(px, py);
			renderer.drawShape(shape, rp);
			renderer.next();
			renderer.enableTextures();

			renderer.bindTexture(BLOCK_TEXTURE);
			shape.resetState();
			shape.setSize((int) (size * factor * atlasFactor), (int) (size * factor));
			//shape.setSize(150, 150);
			shape.setPosition(px, py);
			renderer.drawShape(shape, rp);

			FontRenderOptions fro = new FontRenderOptions();
			fro.color = 0xFFFFFF;
			fro.shadow = true;

			float u = (mouseX - px) / 1024F * getGui().getRenderer().getScaleFactor() / factor;
			float v = (mouseY - py) / 1024F * getGui().getRenderer().getScaleFactor() / factor;

			renderer.drawText(null, "Atlas : " + atlasWidth + "x" + atlasHeight, 10, 10, 0, fro);
			renderer.drawText(null, "U : " + u, 10, 20, 0, fro);
			renderer.drawText(null, "V : " + v, 10, 30, 0, fro);
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
				factor = 1;

			return true;
		}
	}

}
