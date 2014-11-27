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

package net.malisis.mdt.data.group;

import java.util.HashSet;
import java.util.Set;

import net.malisis.mdt.DebugTool;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.data.information.DefaultInformation;
import net.malisis.mdt.gui.component.information.IconInfoComp;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

/**
 * @author Ordinastie
 *
 */
public class BlockGroup extends Group
{
	protected DefaultInformation<String> name = new DefaultInformation("mdt.block.name");
	protected DefaultInformation<Integer> id = new DefaultInformation("mdt.block.id");
	protected DefaultInformation<String> unlocname = new DefaultInformation("mdt.block.unlocname");
	protected DefaultInformation<Integer> metadata = new DefaultInformation("mdt.block.metadata");
	protected DefaultInformation<String> position = new DefaultInformation("mdt.block.position");
	protected DefaultInformation<IIcon[]> icon = new DefaultInformation("mdt.block.icon", IconInfoComp.class);

	protected int x;
	protected int y;
	protected int z;

	protected int lastX = 0;
	protected int lastY = 999;
	protected int lastZ = 0;

	public BlockGroup()
	{
		super("mdt.block.groupname");
		add(name);
		add(id);
		add(unlocname);
		add(metadata);
		add(position);
		add(icon);

	}

	@Override
	public boolean updateInformations()
	{
		MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
		if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK)
			return false;

		x = mop.blockX;
		y = mop.blockY;
		z = mop.blockZ;

		if (x == lastX && y == lastY && z == lastZ)
			return false;

		lastX = x;
		lastY = y;
		lastZ = z;

		Block block = DebugTool.world.getBlock(x, y, z);
		int meta = DebugTool.world.getBlockMetadata(x, y, z);
		ItemStack is = block.getPickBlock(mop, DebugTool.world, x, y, z);

		name.setValue(is.getDisplayName());
		unlocname.setValue(block.getUnlocalizedName());
		metadata.setValue(meta);
		position.setValue("[" + x + ", " + y + ", " + z + "]");

		Set<IIcon> icons = new HashSet<>();
		for (int i = 0; i < 6; i++)
			icons.add(block.getIcon(DebugTool.world, x, y, z, i));

		icon.setValue(icons.toArray(new IIcon[0]));

		empty = false;
		return true;
	}
}
