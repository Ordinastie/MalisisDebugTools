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
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UIImage;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.client.gui.event.component.StateChangeEvent.HoveredStateChange;
import net.malisis.core.renderer.font.FontRenderOptions;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.IInfoComponent;

import com.google.common.eventbus.Subscribe;

/**
 * @author Ordinastie
 *
 */
public class IconInfoComp extends UIContainer<IconInfoComp> implements IInfoComponent<IInformation<Icon[]>>
{
	protected UILabel label;
	protected UIImage[] icons = new UIImage[6];
	protected UIImage zoom;
	protected int baseColor = 0xAAAAFF;
	protected FontRenderOptions fro;

	public IconInfoComp(MalisisGui gui)
	{
		super(gui);
		fro = new FontRenderOptions();
		fro.color = baseColor;
		label = new UILabel(gui).setFontRenderOptions(fro);
		add(label);
		UIImage img;
		for (int i = 0; i < 6; i++)
		{
			img = new UIImage(gui, MalisisGui.BLOCK_TEXTURE, null).setPosition(-18 * i, 0, Anchor.RIGHT);
			img.register(this);
			add(img);
			icons[i] = img;
		}

		zoom = new UIImage(gui, MalisisGui.BLOCK_TEXTURE, null);
		zoom.setPosition(0, 18, Anchor.RIGHT).setSize(64, 64).setZIndex(1).setVisible(false);
		add(zoom);

		setSize(UIComponent.INHERITED, 18);
		clipContent = false;
	}

	@Override
	public void updateInformation(IInformation<Icon[]> info)
	{
		if (info == null)
			return;

		label.setText(info.getLabel());
		Icon[] infoIcons = info.getValue();
		for (int i = 0; i < 6; i++)
		{
			if (i < infoIcons.length)
			{
				icons[i].setIcon(infoIcons[i]);
				icons[i].setVisible(true);
			}
			else
				icons[i].setVisible(false);
		}
	}

	@Subscribe
	public void hoveredStateChanged(HoveredStateChange<UIImage> event)
	{
		if (!(event.getComponent() instanceof UIImage))
			return;

		UIImage comp = event.getComponent();
		zoom.setPosition(comp.getX(), 18).setVisible(event.getState()).setIcon(comp.getIcon());
	}

}