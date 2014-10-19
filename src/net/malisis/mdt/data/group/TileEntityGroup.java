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

import net.malisis.core.inventory.IInventoryProvider;
import net.malisis.mdt.DebugTool;
import net.malisis.mdt.data.Group;
import net.malisis.mdt.data.information.DefaultInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

/**
 * @author Ordinastie
 *
 */
public class TileEntityGroup extends Group
{
	private DefaultInformation<String> type = new DefaultInformation<>("mdt.te.type");
	private DefaultInformation<Boolean> update = new DefaultInformation<>("mdt.te.update");
	private DefaultInformation<Boolean> inventory = new DefaultInformation<>("mdt.te.inventory");
	private DefaultInformation<String> nbt = new DefaultInformation<>("mdt.te.nbt");

	protected int x;
	protected int y;
	protected int z;

	protected int lastX = 0;
	protected int lastY = 999;
	protected int lastZ = 0;

	public TileEntityGroup()
	{
		super("mdt.te.groupname");
		add(type);
		add(update);
		add(inventory);
		add(nbt);
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

		TileEntity te = DebugTool.world.getTileEntity(x, y, z);
		if (te == null)
		{
			clear();
			return true;
		}

		type.setValue(te.getClass().getSimpleName());
		update.setValue(te.canUpdate());
		inventory.setValue(te instanceof IInventory || te instanceof IInventoryProvider);
		nbt.setValue("NBT");

		empty = false;
		return true;
	}
}
