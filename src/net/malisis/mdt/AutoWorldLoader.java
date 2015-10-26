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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import net.malisis.core.MalisisCore;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraftforge.fml.common.StartupQuery;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * @author Ordinastie
 *
 */
public class AutoWorldLoader
{
	private Minecraft mc = Minecraft.getMinecraft();
	private SaveFormatComparator save;

	public AutoWorldLoader()
	{
		//only auto load in dev env
		if (MalisisCore.isObfEnv || GuiScreen.isCtrlKeyDown())
			return;
		boolean b = false;
		if (b)
			return;

		setDisplay();
		getSave();
		loadWorld();
	}

	public void setDisplay()
	{
		try
		{
			int w = 1914;
			int h = 1012;
			Display.update();
			DisplayMode dm = new DisplayMode(w, h);
			Display.setDisplayMode(dm);
			Display.setLocation(0, 0);

			Method resize = Minecraft.class.getDeclaredMethod("resize", Integer.TYPE, Integer.TYPE);
			resize.setAccessible(true);
			resize.invoke(Minecraft.getMinecraft(), w, h);

		}
		catch (ReflectiveOperationException | LWJGLException e)
		{
			MalisisDebugTools.log.error("Failed to set resolution : ", e);
		}
	}

	public void getSave()
	{
		try
		{
			ISaveFormat isaveformat = mc.getSaveLoader();
			List<SaveFormatComparator> saves = isaveformat.getSaveList();
			Collections.sort(saves);
			save = saves.get(0);
		}
		catch (AnvilConverterException e)
		{
			MalisisCore.log.error("Couldn\'t load level list", e);
			this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", e.getMessage()));
			return;
		}
	}

	public void loadWorld()
	{
		String dirName = save.getFileName();
		if (dirName == null)
			dirName = "World" + 0;

		String saveName = save.getDisplayName();
		if (saveName == null)
			saveName = "World" + 0;

		try
		{
			Minecraft.getMinecraft().launchIntegratedServer(dirName, saveName, (WorldSettings) null);
		}
		catch (StartupQuery.AbortedException e)
		{
			MalisisDebugTools.log.error("Failed to load world : ", e);
		}
	}
}
