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

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.malisis.core.renderer.IAnimatedRenderable;
import net.malisis.core.renderer.component.AnimatedModelComponent;
import net.malisis.core.util.TileEntityUtils;
import net.malisis.core.util.Timer;
import net.malisis.core.util.Utils;
import net.malisis.core.util.blockdata.BlockDataHandler;
import net.malisis.core.util.multiblock.MultiBlock;
import net.malisis.core.util.multiblock.MultiBlockComponent;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.MDTRegistry;
import net.malisis.mdt.data.ICategory;
import net.malisis.mdt.data.IGroup;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.data.group.Group;
import net.malisis.mdt.data.information.Action;
import net.malisis.mdt.data.information.Information;
import net.malisis.mdt.renderer.MultiBlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author Ordinastie
 *
 */
public class BlockCategory implements ICategory
{
	public BlockCategory()
	{
		MDTRegistry.registerFactory(this, this::createBlockGroup);
		MDTRegistry.registerFactory(this, this::createTileEntityGroup);
		MDTRegistry.registerFactory(this, this::createBlockDataGroup);
		MDTRegistry.registerFactory(this, this::createAnimationGroup);
		MDTRegistry.registerFactory(this, this::createMultiBlockGroup);
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
		IBlockState state = Utils.getClientWorld().getBlockState(pos).getActualState(Utils.getClientWorld(), pos);

		ItemStack is = state.getBlock().getPickBlock(state, result, Utils.getClientWorld(), pos, Utils.getClientPlayer());

		Set<IInformation<?>> set = ImmutableSet.of(	Information.of("mdt.block.name", is != null ? is.getDisplayName() : " - "),
													Information.of("mdt.block.id", Block.getIdFromBlock(state.getBlock())),
													Information.of(	"mdt.block.registryname",
																	Block.REGISTRY.getNameForObject(state.getBlock())),
													Information.of("mdt.block.unlocname", state.getBlock().getUnlocalizedName()),
													Information.of("mdt.block.position", pos),
													Information.of("mdt.block.blockstate", state),
													Information.of("mdt.block.metadata", state.getBlock().getMetaFromState(state)));
		return new Group("mdt.block.groupname", set);
	}

	private IGroup createTileEntityGroup(DebugTool tool)
	{
		RayTraceResult result = tool.rayTraceResult.get();
		TileEntity te = TileEntityUtils.getTileEntity(TileEntity.class, Utils.getClientWorld(), result.getBlockPos());
		if (te == null)
			return null;

		Set<IInformation<?>> set = Sets.newLinkedHashSet();
		set.add(Information.of("mdt.te.type", te.getClass().getSimpleName()));
		set.add(Information.of("mdt.te.update", te instanceof ITickable));
		set.add(Information.of(	"mdt.te.inventory",
								te instanceof IInventory || te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)));

		NBTTagCompound tag = te.writeToNBT(new NBTTagCompound());
		if (tag.getSize() > 0)
			set.add(Information.of("mdt.te.nbt", tag));

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
				set.add(Information.of(identifier, data));
		}

		if (set.size() == 0)
			return null;

		return new Group("mdt.blockdata.groupname", set);
	}

	private IGroup createAnimationGroup(DebugTool tool)
	{
		BlockPos pos = tool.rayTraceResult.get().getBlockPos();
		IBlockState state = Utils.getClientWorld().getBlockState(pos).getActualState(Utils.getClientWorld(), pos);
		AnimatedModelComponent amc = AnimatedModelComponent.get(state.getBlock());
		if (amc == null)
			return null;
		Optional<IAnimatedRenderable> o = amc.getRenderable(pos);
		if (!o.isPresent())
			return null;

		Set<IInformation<?>> set = Sets.newLinkedHashSet();
		for (Entry<String, Timer> entry : o.get().getTimers().entrySet())
		{
			Timer t = entry.getValue();
			set.add(Information.of(entry.getKey(), (t.elapsedTime() > 0 ? TextFormatting.GREEN : TextFormatting.RED) + t.toString()));
		}

		if (set.size() == 0)
			return null;

		return new Group("mdt.animation.groupname", set);
	}

	private IGroup createMultiBlockGroup(DebugTool tool)
	{
		BlockPos pos = tool.rayTraceResult.get().getBlockPos();
		IBlockState state = Utils.getClientWorld().getBlockState(pos).getActualState(Utils.getClientWorld(), pos);
		MultiBlock mb = MultiBlockComponent.getMultiBlock(Utils.getClientWorld(), pos, state, null);
		if (mb == null)
			return null;

		BlockPos origin = MultiBlock.getOrigin(Utils.getClientWorld(), pos);
		Action<MultiBlock> action = Action.of("mdt.multiblock.render", mb, m -> MultiBlockRenderer.toggle(m, origin));
		return new Group("mdt.multiblock.groupname", ImmutableSet.of(action));
	}
}
