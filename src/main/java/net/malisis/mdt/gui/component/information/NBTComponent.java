/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Ordinastie
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

package net.malisis.mdt.gui.component.information;

import static com.google.common.base.Preconditions.*;

import java.util.function.Consumer;

import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.client.gui.render.ColoredBackground;
import net.malisis.core.renderer.font.FontOptions;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.DebugGui;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Ordinastie
 *
 */
public class NBTComponent extends UIContainer<NBTComponent>
{
	protected UILabel label;
	private FontOptions labelOptions = FontOptions.builder().color(0xAAAAFF).build();

	public NBTComponent(DebugGui gui, IInformation<NBTTagCompound> information)
	{
		super(gui);

		label = new UILabel(gui, information.getLabel()).setFontOptions(labelOptions);
		add(label);

		NBTTagCompound tag = information.getValue();
		UIContainer<?> cont = createTagContainer(gui, tag, this);
		UILabel l = new ClickLabel(gui, "[" + tag.getKeySet().size() + " tags]", cont::setVisible);
		l.setPosition(0, 0, Anchor.RIGHT);
		add(l);

		clipContent = false;
	}

	private UIContainer<?> createTagContainer(DebugGui gui, NBTTagCompound tag, UIContainer<?> parent)
	{
		UIContainer<?> cont = new UIContainer<>(gui);
		cont.setBackground(new ColoredBackground(0x222277));
		cont.setPosition(5, 10);
		cont.setPadding(5, 5);
		cont.setSize(-5, tag.getKeySet().size() * 10 + 10);
		cont.setZIndex(parent.getZIndex() + 5);
		cont.setVisible(false);

		int y = 0;
		for (String key : tag.getKeySet())
		{
			UILabel l = new UILabel(gui, key).setFontOptions(labelOptions);
			l.setPosition(0, y);
			cont.add(l);

			l = new UILabel(gui, tag.getTag(key).toString()).setFontOptions(FontOptions.builder().color(0xFFFFFF).build());
			l.setPosition(0, y, Anchor.RIGHT);
			cont.add(l);
			y += 10;
		}

		parent.add(cont);
		return cont;
	}

	private static class ClickLabel extends UILabel
	{
		boolean active = false;
		Consumer<Boolean> onClick = null;

		public ClickLabel(DebugGui gui, String label, Consumer<Boolean> onClick)
		{
			super(gui, label);
			this.onClick = checkNotNull(onClick);
			setFontOptions(FontOptions.builder().color(0xFFFFFF).build());
		}

		@Override
		public UIComponent<?> getComponentAt(int x, int y)
		{
			return isInsideBounds(x, y) ? this : null;
		}

		@Override
		public boolean onClick(int x, int y)
		{
			active = !active;
			onClick.accept(active);
			return true;
		}
	}
}
