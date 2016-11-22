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
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UISeparator;
import net.malisis.core.renderer.font.FontOptions;
import net.malisis.mdt.data.IGroup;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.ComponentProviders;
import net.malisis.mdt.gui.DebugGui;

import org.lwjgl.opengl.GL11;

/**
 * @author Ordinastie
 *
 */
public class GroupContainer extends UIContainer<GroupContainer>
{
	private IGroup group;
	private FontOptions fontOptions = FontOptions.builder().scale(2).color(0xFFFFFF).shadow().build();

	public GroupContainer(DebugGui gui, IGroup group)
	{
		super(gui);
		this.group = group;
		clipContent = false;
		add(new UISeparator(gui).setPosition(0, 20).setColor(0xFFFFFF));
		int h = 30;
		for (IInformation<?> info : group)
		{
			UIComponent<?> component = ComponentProviders.getComponent(gui, info);
			component.setPosition(0, h);
			h += component.getHeight();
			add(component);
		}

		setSize(INHERITED, h + 5);
	}

	@Override
	public void drawBackground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick)
	{
		renderer.drawText(null, group.getName(), 5, 0, 0, fontOptions);

		renderer.next(GL11.GL_QUADS);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		shape.setSize(getWidth(), 2, 0).translate(0, 20, 0);
		rp.colorMultiplier.set(0xFFFFFF);

		renderer.drawShape(shape, rp);
		renderer.next();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
