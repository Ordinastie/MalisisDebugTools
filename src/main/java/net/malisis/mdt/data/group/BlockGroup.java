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

import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.util.MBlockState;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.data.information.DefaultInformation;
import net.malisis.mdt.gui.component.information.IconInfoComp;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author Ordinastie
 *
 */
public class BlockGroup extends Group
{
	protected DefaultInformation<String> name = new DefaultInformation<>("mdt.block.name");
	protected DefaultInformation<Integer> id = new DefaultInformation<>("mdt.block.id");
	protected DefaultInformation<String> unlocname = new DefaultInformation<>("mdt.block.unlocname");
	protected DefaultInformation<Integer> metadata = new DefaultInformation<>("mdt.block.metadata");
	protected DefaultInformation<String> position = new DefaultInformation<>("mdt.block.position");
	protected DefaultInformation<Icon[]> icon = new DefaultInformation("mdt.block.icon", IconInfoComp.class);

	protected BlockPos pos;
	protected BlockPos lastPos = new BlockPos(0, 999, 0);

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
		RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
		if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
			return false;

		pos = result.getBlockPos();
		if (lastPos.equals(pos))
			return false;

		lastPos = pos;

		MBlockState mstate = new MBlockState(DebugTool.world, pos);
		ItemStack is = mstate.getBlock().getPickBlock(mstate.getBlockState(), result, DebugTool.world, pos, DebugTool.player);

		name.setValue(is.getDisplayName());
		unlocname.setValue(mstate.getBlock().getUnlocalizedName());
		metadata.setValue(mstate.getBlock().getMetaFromState(mstate.getBlockState()));
		position.setValue("[" + mstate.getX() + ", " + mstate.getY() + ", " + mstate.getZ() + "]");

		//		Set<MalisisIcon> icons = new HashSet<>();
		//		for (int i = 0; i < 6; i++)
		//			icons.add(block.getIcon(DebugTool.world, x, y, z, i));
		//
		icon.setValue(new Icon[0]);

		empty = false;
		return true;
	}
}
