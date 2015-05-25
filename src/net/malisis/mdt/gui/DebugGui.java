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

import java.util.ArrayList;
import java.util.List;

import net.malisis.core.client.gui.MalisisGui;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.KeyBindings;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.gui.component.DebugWindow;
import net.malisis.mdt.gui.component.GroupContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Ordinastie
 *
 */
public class DebugGui extends MalisisGui
{
	private static DebugGui instance;
	private int currentWidth = 0;
	private int currentHeight = 0;

	private List<GroupContainer> groupContainers = new ArrayList();
	private DebugWindow window;

	public DebugGui()
	{
		instance = this;
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

	@Override
	public void setWorldAndResolution(Minecraft minecraft, int width, int height)
	{
		if (width == currentWidth && height == currentHeight)
			return;

		currentWidth = width;
		currentHeight = height;
		super.setWorldAndResolution(minecraft, width, height);
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

		updateDebugWindow();

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

	private void updateDebugWindow()
	{
		DebugTool dt = DebugTool.instance();
		dt.updateInformations();
		boolean changed = dt.categoryChanged();
		if (changed)
		{
			window.removeAll();
			groupContainers.clear();
			for (Group group : DebugTool.instance().listDebugGroups())
			{
				GroupContainer cont = new GroupContainer(this);
				cont.setDebugGroup(this, group);
				groupContainers.add(cont);
				window.add(cont);
			}
		}

		int h = 0;
		for (GroupContainer cont : groupContainers)
		{
			cont.setPosition(0, h);
			h += cont.updateInfos(this);
		}
	}

	@SubscribeEvent
	public void renderDebugWindow(RenderGameOverlayEvent.Post event)
	{
		if (event.type != RenderGameOverlayEvent.ElementType.ALL || Minecraft.getMinecraft().currentScreen instanceof DebugGui)
			return;

		instance.setWorldAndResolution(Minecraft.getMinecraft(), event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
		instance.drawScreen(event.mouseX, event.mouseY, event.partialTicks);
	}

}
