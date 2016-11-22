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

import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.renderer.font.FontOptions;
import net.malisis.core.renderer.font.FontOptions.FontOptionsBuilder;
import net.malisis.core.renderer.font.MalisisFont;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.DebugGui;

/**
 * @author Ordinastie
 *
 */
public class InformationComponent extends UIContainer<InformationComponent>
{
	public static FontOptionsBuilder builder = FontOptions.builder();
	protected UILabel label;
	protected UILabel value;

	public InformationComponent(DebugGui gui, IInformation<?> information)
	{
		super(gui);

		int labelColor = 0xAAAAFF;
		int valueColor = 0xFFFFFF;
		Object v = information.getValue();
		if (v instanceof Boolean)
			valueColor = (boolean) v ? 0x33AA33 : 0x993333;

		label = new UILabel(gui, information.getLabel()).setFontOptions(builder.color(labelColor).build());
		value = new UILabel(gui, information.getStringValue()).setFontOptions(builder.color(valueColor).build()).setAnchor(Anchor.RIGHT);
		add(label);
		add(value);

		int h = (int) MalisisFont.minecraftFont.getStringHeight() + 2;
		if (label.getWidth() + value.getWidth() > gui.getWindowWidth())
		{
			value.setPosition(0, h);
			h *= 2;
		}

		setSize(UIComponent.INHERITED, h);
	}
}
