/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Ordinastie
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

package net.malisis.mdt.renderer;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.malisis.core.registry.AutoLoad;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.util.MBlockState;
import net.malisis.core.util.modmessage.ModMessage;
import net.malisis.core.util.multiblock.MultiBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * @author Ordinastie
 *
 */
@AutoLoad(true)
public class MultiBlockRenderer extends MalisisRenderer<TileEntity>
{
	private static MultiBlock mb;
	private static BlockPos origin;
	private Shape cube = new Cube();
	private RenderParameters rp = new RenderParameters();

	public MultiBlockRenderer()
	{
		registerForRenderWorldLast();
	}

	@Override
	public boolean shouldRender(RenderWorldLastEvent event, IBlockAccess world)
	{
		return mb != null;
	}

	@Override
	public void render()
	{
		next(GL11.GL_LINE_LOOP);
		disableTextures();

		Iterator<MBlockState> it = mb.iteratorInWorld(origin);
		while (it.hasNext())
		{
			MBlockState state = it.next();
			cube.resetState();
			cube.translate(state.getX(), state.getY(), state.getZ());
			rp.colorMultiplier.set(MultiBlock.isOrigin(world, state.getPos()) ? 0x44CC66 : 0x6677AA);
			drawShape(cube, rp);
		}
		next();
		enableTextures();
	}

	@ModMessage
	public static void toggle(MultiBlock multiblock, BlockPos originPos)
	{
		if (originPos.equals(origin))
		{
			mb = null;
			origin = null;
		}
		else
		{
			mb = multiblock;
			origin = originPos;
		}

	}
}
