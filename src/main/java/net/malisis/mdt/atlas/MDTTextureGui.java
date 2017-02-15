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

package net.malisis.mdt.atlas;

import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIBackgroundContainer;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.client.gui.component.decoration.UILabel;
import net.malisis.core.client.gui.component.interaction.UITextField;
import net.malisis.core.renderer.font.FontOptions;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.util.Point;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * @author Ordinastie
 *
 */
public class MDTTextureGui extends MalisisGui
{
	UILabel uv;
	UILabel iconName;
	UILabel iconU;
	UILabel iconV;
	UITextField search;
	AtlasComponent ac;

	public MDTTextureGui()
	{
		guiscreenBackground = true;
	}

	@Override
	public void construct()
	{
		FontOptions options = FontOptions.builder().color(0xFFFFFF).shadow().build();
		int y = 0;
		//label for size
		UILabel sizeLabel = new UILabel(this, "Size : " + Icon.BLOCK_TEXTURE_WIDTH + "x" + Icon.BLOCK_TEXTURE_HEIGHT);
		sizeLabel.setPosition(0, y);
		sizeLabel.setFontOptions(options);
		y += 10;

		//label for UV
		uv = new UILabel(this);
		uv.setPosition(0, y);
		uv.setFontOptions(options);
		y += 10;

		//current hovered icon label
		iconName = new UILabel(this);
		iconName.setPosition(0, y);
		iconName.setFontOptions(options);
		y += 10;

		iconU = new UILabel(this);
		iconU.setPosition(0, y);
		iconU.setFontOptions(options);
		y += 10;

		iconV = new UILabel(this);
		iconV.setPosition(0, y);
		iconV.setFontOptions(options);
		y += 10;

		//search textbox
		search = new UITextField(this, false);
		search.setPosition(0, y, Anchor.CENTER);
		search.setSize(100, 12);
		y += 12;

		UIBackgroundContainer leftPanel = new UIBackgroundContainer(this);
		leftPanel.setBorder(0xFFFFFF, 2, 255);
		leftPanel.setBackgroundAlpha(0);
		leftPanel.setSize(250, UIComponent.INHERITED);
		leftPanel.setPadding(5, 5);

		leftPanel.add(sizeLabel);
		leftPanel.add(uv);
		leftPanel.add(iconName);
		leftPanel.add(iconU);
		leftPanel.add(iconV);
		leftPanel.add(search);

		ac = new AtlasComponent(this);
		ac.setPosition(0, 0, Anchor.RIGHT);
		ac.setSize(388, UIComponent.INHERITED);

		UIContainer<?> cont = new UIContainer<>(this);
		cont.add(leftPanel);
		cont.add(ac);

		addToScreen(cont);
	}

	@Override
	public void update(int mouseX, int mouseY, float partialTick)
	{
		Point p = ac.getHoveredUVs();
		uv.setText(p != null ? String.format("UV : %.4f/%.4f", p.x, p.y) : "UV : -");

		TextureAtlasSprite icon = ac.getHoveredIcon();
		iconName.setText(icon != null ? "Icon : " + icon.getIconName() : "Icon : -");
		iconU.setText(icon != null ? String.format("Icon U : %.4f/%.4f", icon.getMinU(), icon.getMaxU()) : "Icon U : -");
		iconV.setText(icon != null ? String.format("Icon V : %.4f/%.4f", icon.getMinV(), icon.getMaxV()) : "Icon V : -");

		ac.setFilter(search.getText());
	}
}
