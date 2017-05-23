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

package net.malisis.mdt.gui.component.information;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.renderer.font.FontOptions;
import net.malisis.core.renderer.font.FontOptions.FontOptionsBuilder;
import net.malisis.core.renderer.font.MalisisFont;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.DebugGui;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Ordinastie
 *
 */
public class BlockStateComponent extends UIContainer<BlockStateComponent>
{
	public static FontOptionsBuilder builder = FontOptions.builder();
	protected UILabel label;
	protected List<UILabel> values = Lists.newArrayList();

	public BlockStateComponent(DebugGui gui, IInformation<IBlockState> information)
	{
		super(gui);

		int labelColor = 0xAAAAFF;
		int valueColor = 0xFFFFFF;
		label = new UILabel(gui, information.getLabel()).setFontOptions(builder.color(labelColor).build());
		add(label);

		IBlockState state = information.getValue();
		int y = 0;
		boolean first = true;
		int h = (int) MalisisFont.minecraftFont.getStringHeight() + 2;
		for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet())
		{
			String s = getPropertyString(entry.getKey(), entry.getValue());
			UILabel value = new UILabel(gui, s).setFontOptions(builder.color(valueColor).build());
			if (first && label.getWidth() + value.getWidth() > gui.getWindowWidth())
				y += h;
			value.setPosition(0, y, Anchor.RIGHT);
			add(value);
			first = false;
			y += h;
		}

		setSize(UIComponent.INHERITED, y);
	}

	@SuppressWarnings("unchecked")
	private <T extends Comparable<T>> String getPropertyString(IProperty<T> property, Comparable<?> value)
	{
		String s = property.getName((T) value);
		if (value instanceof Boolean)
			s = ((Boolean) value ? TextFormatting.GREEN : TextFormatting.RED) + s;
		return property.getName() + " = " + s;
	}
}
