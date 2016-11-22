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

import net.malisis.mdt.data.ICategory;
import net.malisis.mdt.data.cacheddata.CachedEquippedItem;
import net.malisis.mdt.data.cacheddata.CachedRayTraceResult;
import net.malisis.mdt.data.category.Categories;
import net.malisis.mdt.gui.DebugGui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * @author Ordinastie
 *
 */
public class DebugTool
{
	private static DebugTool instance = new DebugTool();
	private DebugGui debugGui;

	public CachedRayTraceResult rayTraceResult = new CachedRayTraceResult();
	public CachedEquippedItem equippedItem = new CachedEquippedItem();

	private List<ICategory> categories = new ArrayList<>();
	private ICategory currentCategory;
	private boolean categoryChanged = false;

	private int mouseLastPosX = Display.getWidth() / 2;
	private int mouseLastPosY = Display.getHeight() / 2;

	private boolean active = false;

	private DebugTool()
	{
		debugGui = new DebugGui(this);
		setCurrentCategory(Categories.BLOCK_CATEGORY);

	}

	public ICategory getCurrentCategory()
	{
		return currentCategory;
	}

	public boolean shouldRefresh()
	{
		categoryChanged = false;
		return categoryChanged || currentCategory.shouldRefresh(this);
	}

	private void setActive(boolean active)
	{
		if (active)
			debugGui.displayOverlay();
		else
			debugGui.closeOverlay();

		this.active = active;
	}

	public void setCurrentCategory(ICategory category)
	{
		currentCategory = category;
		categoryChanged = true;
	}

	public void toggleMouseControl()
	{
		if (!active)
			setActive(true);
		else
		{
			debugGui.display();
			Mouse.setCursorPosition(mouseLastPosX, mouseLastPosY);
		}
	}

	public static DebugTool get()
	{
		return instance;
	}

	public static void toggle()
	{
		instance.setActive(!instance.active);
	}

	public static void activate()
	{
		instance.setActive(true);
	}

	public static void deactivate()
	{
		instance.setActive(false);
	}

	public static void reset()
	{
		instance.setActive(false);
		instance = new DebugTool();
		instance.setActive(true);
	}

}
