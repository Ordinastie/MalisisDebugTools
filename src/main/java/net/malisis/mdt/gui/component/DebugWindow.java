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

package net.malisis.mdt.gui.component;

import org.lwjgl.opengl.GL11;

import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.control.UICloseHandle;
import net.malisis.core.client.gui.component.control.UIResizeHandle;
import net.malisis.core.client.gui.element.SimpleGuiShape;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.mdt.DebugTool;

/**
 * @author Ordinastie
 *
 */
public class DebugWindow extends UIContainer<DebugWindow>
{
	public DebugWindow(MalisisGui gui)
	{
		super(gui);
		setSize(250, 500);
		setPosition(50, 250);
		setPadding(5, 5);

		shape = new SimpleGuiShape();
		rp = new RenderParameters();

		new UIResizeHandle(gui, this);
		new UICloseHandle(gui, this);

		clipContent = false;
	}

	@Override
	public void onClose()
	{
		DebugTool.deactivate();
	}

	@Override
	public void drawBackground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		renderer.enableBlending();

		rp.colorMultiplier.set(0); // black
		rp.alpha.set(200);
		renderer.drawShape(shape, rp);

		renderer.next(GL11.GL_LINE_STRIP);
		GL11.glLineWidth(2F);
		shape.resetState();
		shape.setSize(getWidth(), getHeight());
		//rp.alpha.set(255);
		renderer.drawShape(shape, rp);

		renderer.next(GL11.GL_QUADS);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void drawForeground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		super.drawForeground(renderer, mouseX, mouseY, partialTick);
		//renderer.drawText("Test");
	}
}
