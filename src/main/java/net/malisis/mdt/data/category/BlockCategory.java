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

package net.malisis.mdt.data.category;

import java.util.Set;

import net.malisis.core.util.TileEntityUtils;
import net.malisis.core.util.Utils;
import net.malisis.core.util.blockdata.BlockDataHandler;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.data.ICategory;
import net.malisis.mdt.data.IGroup;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.data.group.Group;
import net.malisis.mdt.data.information.Information;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.items.CapabilityItemHandler;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * @author Ordinastie
 *
 */
public class BlockCategory implements ICategory
{
	public BlockCategory()
	{
		Categories.registerCategory(this);
		Categories.registerFactory(this, this::createBlockGroup);
		Categories.registerFactory(this, this::createTileEntityGroup);
		Categories.registerFactory(this, this::createBlockDataGroup);
	}

	@Override
	public String getName()
	{
		return "mdt.block.category";
	}

	@Override
	public boolean shouldRefresh(DebugTool debugTool)
	{
		return debugTool.rayTraceResult.get().typeOfHit == Type.BLOCK && debugTool.rayTraceResult.hasChanged();
	}

	private IGroup createBlockGroup(DebugTool tool)
	{
		RayTraceResult result = tool.rayTraceResult.get();
		BlockPos pos = result.getBlockPos();
		IBlockState state = Utils.getClientWorld().getBlockState(pos);

		ItemStack is = state.getBlock().getPickBlock(state, result, Utils.getClientWorld(), pos, Utils.getClientPlayer());

		Set<IInformation<?>> set = ImmutableSet.of(new Information<>("mdt.block.name", is.getDisplayName()), new Information<>(
				"mdt.block.id", Block.getIdFromBlock(state.getBlock())), new Information<>("mdt.block.unlocname",
				state.getBlock().getUnlocalizedName()), new Information<>("mdt.block.blockstate", state), new Information<>(
				"mdt.block.metadata", state.getBlock().getMetaFromState(state)), new Information<>("mdt.block.position", pos.getX() + ", "
				+ pos.getY() + ", " + pos.getZ()));
		return new Group("mdt.block.groupname", set);
	}

	private IGroup createTileEntityGroup(DebugTool tool)
	{
		RayTraceResult result = tool.rayTraceResult.get();
		TileEntity te = TileEntityUtils.getTileEntity(TileEntity.class, Utils.getClientWorld(), result.getBlockPos());
		if (te == null)
			return null;

		Set<IInformation<?>> set = ImmutableSet.of(new Information<>("mdt.te.type", te.getClass().getSimpleName()),
				new Information<>("mdt.te.update", te instanceof ITickable),
				new Information<>("mdt.te.inventory", te instanceof IInventory
						|| te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)),
				new Information<>("mdt.te.nbt", "NBT"));

		return new Group("mdt.te.groupname", set);
	}

	private IGroup createBlockDataGroup(DebugTool tool)
	{
		RayTraceResult result = tool.rayTraceResult.get();
		BlockPos pos = result.getBlockPos();

		Set<IInformation<?>> set = Sets.newLinkedHashSet();
		for (String identifier : BlockDataHandler.get().getHandlerInfos().keySet())
		{
			Object data = BlockDataHandler.getData(identifier, Utils.getClientWorld(), pos);
			if (data != null)
				set.add(new Information<>(identifier, data.toString()));
		}

		if (set.size() == 0)
			return null;

		return new Group("mdt.blockdata.groupname", set);
	}
}
