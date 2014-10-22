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

import java.util.ArrayList;
import java.util.List;

import net.malisis.core.client.gui.MalisisGui;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.data.ICategory;
import net.malisis.mdt.data.category.BlockCaterogy;
import net.malisis.mdt.data.category.ItemCategory;
import net.malisis.mdt.gui.DebugGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * @author Ordinastie
 *
 */
public class DebugTool
{
	private static DebugTool instance;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static World world = mc.theWorld;
	public static EntityClientPlayerMP player = mc.thePlayer;
	private DebugGui debugGui;

	private List<ICategory> debugCategories = new ArrayList<>();
	private ICategory currentCategory;
	private boolean categoryChanged = false;

	private int mouseLastPosX = Display.getWidth() / 2;
	private int mouseLastPosY = Display.getHeight() / 2;

	private boolean active = false;

	private DebugTool()
	{
		ICategory cat = new BlockCaterogy();
		registerCategory(cat);
		registerCategory(new ItemCategory());

		setCurrentCategory(cat);
	}

	public boolean categoryChanged()
	{
		if (categoryChanged)
		{
			categoryChanged = false;
			return true;
		}

		return false;
	}

	private void setActive(boolean active)
	{
		if (active)
		{
			if (debugGui != null)
				MinecraftForge.EVENT_BUS.unregister(debugGui);
			debugGui = new DebugGui();
			MinecraftForge.EVENT_BUS.register(debugGui);
			categoryChanged = true;
		}
		else
		{
			if (debugGui != null)
			{
				MinecraftForge.EVENT_BUS.unregister(debugGui);
				if (MalisisGui.currentGui() == debugGui)
					debugGui.close();
				debugGui = null;
				mc.mouseHelper.grabMouseCursor();
			}
		}
		this.active = active;
	}

	public void registerCategory(ICategory category)
	{
		debugCategories.add(category);
	}

	public void updateInformations()
	{
		if (currentCategory != null)
			currentCategory.updateGroups();
	}

	public void setCurrentCategory(ICategory category)
	{
		currentCategory = category;
		categoryChanged = true;

	}

	public List<Group> listDebugGroups()
	{
		if (currentCategory == null)
			return null;

		return currentCategory.listGroups();
	}

	public List<ICategory> listDebugCategories()
	{
		return debugCategories;
	}

	public void toggleMouseControl()
	{
		if (Mouse.isGrabbed() && mc.currentScreen == null)
		{
			if (!active)
			{
				setActive(true);
				return;
			}
			mc.displayGuiScreen(debugGui);
			Mouse.setCursorPosition(mouseLastPosX, mouseLastPosY);
		}
		else if (!Mouse.isGrabbed())
		{
			mouseLastPosX = Mouse.getX();
			mouseLastPosY = Mouse.getY();
			MalisisGui.setHoveredComponent(null, false);
			mc.displayGuiScreen(null);
		}

	}

	public static DebugTool instance()
	{
		if (instance == null)
			instance = new DebugTool();
		return instance;
	}

	public static void toggle()
	{
		instance().setActive(!instance.active);
	}

	public static void activate()
	{
		instance().setActive(true);
	}

	public static void deactivate()
	{
		instance().setActive(false);
	}

	public static void reset()
	{
		instance().setActive(false);
		instance = null;
		instance().setActive(true);

	}

}
