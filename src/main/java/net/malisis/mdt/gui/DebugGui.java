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

package net.malisis.mdt.gui;

import java.util.List;

import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.KeyBindings;
import net.malisis.mdt.data.IGroup;
import net.malisis.mdt.data.category.Categories;
import net.malisis.mdt.gui.component.DebugWindow;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Ordinastie
 *
 */
public class DebugGui extends MalisisGui
{
	private DebugWindow window;
	private DebugTool tool;

	public DebugGui(DebugTool tool)
	{
		this.tool = tool;
		guiscreenBackground = false;
		renderer.setIgnoreScale(true);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void construct()
	{
		window = new DebugWindow(this);
		addToScreen(window);
	}

	public int getWindowWidth()
	{
		return window.getWidth();
	}

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		if (getComponentAt(x, y) != null)
			super.mouseClicked(x, y, button);
		else
			close();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof DebugGui))
			return;

		if (!constructed)
		{
			construct();
			constructed = true;
		}

		renderer.enableBlending();
		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	@Override
	protected void keyTyped(char keyChar, int keyCode)
	{
		super.keyTyped(keyChar, keyCode);
		//close GuiScreen (RenderGameOverlayEvent takes over)
		if (keyCode == KeyBindings.kbToggleMouse.getKeyCode())
			close();
	}

	@Override
	public void close()
	{
		setHoveredComponent(null, true);
		super.close();
	}

	@Override
	public void update(int mouseX, int mouseY, float partialTick)
	{
		tool.equippedItem.update();
		tool.rayTraceResult.update();
		if (!tool.shouldRefresh())
			return;

		window.removeAll();
		int h = 10;
		List<IGroup> groups = Categories.generateGroups(tool, tool.getCurrentCategory());
		for (IGroup group : groups)
		{
			UIComponent<?> component = ComponentProviders.getComponent(this, group);
			component.setPosition(0, h);
			h += component.getHeight() + 5;

			window.add(component);
		}
	}
}
