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

import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.IInfoComponent;

import org.lwjgl.opengl.GL11;

/**
 * @author Ordinastie
 *
 */
public class GroupContainer extends UIContainer
{
	private Group group;

	public GroupContainer(MalisisGui gui)
	{
		super(gui);
		clipContent = false;
	}

	public void setDebugGroup(MalisisGui gui, Group group)
	{
		this.group = group;
		for (IInformation di : group)
		{
			UIComponent comp = di.getComponent(gui);
			add(comp);
		}
	}

	public int updateInfos(MalisisGui gui)
	{
		if (group.isEmpty())
		{
			this.setVisible(false);
			return 0;
		}

		setVisible(true);

		if (!group.isUpdated())
			return getHeight();

		int h = 30;
		UIComponent comp;
		for (IInformation di : group)
		{
			comp = di.getComponent(gui);
			if (comp != null)
			{
				comp.setPosition(0, h);
				h += comp.getHeight();
				((IInfoComponent) comp).updateInformation(di);
			}
		}

		setSize(UIComponent.INHERITED, h + 5);
		return getHeight();
	}

	@Override
	public void drawBackground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		renderer.setFontScale(2);
		renderer.drawText(group.getName(), 5, 0, 0xFFFFFF, true);

		renderer.next(GL11.GL_QUADS);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		shape.setSize(getWidth(), 2, 0).translate(0, 20, 0);
		rp.colorMultiplier.set(0xFFFFFF);

		renderer.drawShape(shape, rp);
		renderer.next();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		renderer.setFontScale(1);
	}
}
