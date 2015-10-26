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

import net.malisis.core.MalisisCore;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.KeyBindings;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.gui.component.DebugWindow;
import net.malisis.mdt.gui.component.GroupContainer;

import org.lwjgl.input.Keyboard;

/**
 * @author Ordinastie
 *
 */
public class DebugGui extends MalisisGui
{

	private List<GroupContainer> groupContainers = new ArrayList();
	private DebugWindow window;

	public DebugGui()
	{
		guiscreenBackground = false;
		renderer.setIgnoreScale(true);
	}

	@Override
	public void construct()
	{
		window = new DebugWindow(this);
		addToScreen(window);
		MalisisCore.message("Constructed");
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
	protected void keyTyped(char keyChar, int keyCode)
	{
		if (keyCode == Keyboard.KEY_R && isCtrlKeyDown())
		{
			DebugTool.reset();
			return;
		}
		super.keyTyped(keyChar, keyCode);
		//close GuiScreen (RenderGameOverlayEvent takes over)
		if (keyCode == KeyBindings.kbToggleMouse.getKeyCode())
			close();
	}

	@Override
	public void update(int mouseX, int mouseY, float partialTick)
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
}
