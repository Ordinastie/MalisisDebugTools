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
import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.IInfoComponent;

/**
 * @author Ordinastie
 *
 */
public class DefaultInfoComp<T extends IInformation> extends UIContainer<DefaultInfoComp<T>> implements IInfoComponent<T>
{
	protected UILabel label;
	protected UILabel value;
	protected int baseColor = 0xAAAAFF;

	public DefaultInfoComp()
	{
		label = new UILabel().setColor(baseColor);
		value = new UILabel().setColor(baseColor).setAnchor(Anchor.RIGHT);
		add(label);
		add(value);

		setSize(UIComponent.INHERITED, GuiRenderer.FONT_HEIGHT + 2);
	}

	@Override
	public void updateInformation(T info)
	{
		if (info == null)
			return;

		label.setText(info.getLabel());
		value.setColor(baseColor);

		Object v = info.getValue();
		if (v == null)
		{
			value.setText(" - ");
			return;
		}

		if (v instanceof Boolean)
			value.setColor((boolean) v ? 0x33AA33 : 0x993333);
		value.setText(v.toString());
	}
}
