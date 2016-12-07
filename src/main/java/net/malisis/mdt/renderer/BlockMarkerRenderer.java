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

package net.malisis.mdt.renderer;

import java.util.Set;
import java.util.function.Function;

import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.util.AABBUtils;
import net.malisis.core.util.modmessage.ModMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

/**
 * @author Ordinastie
 *
 */
public class BlockMarkerRenderer extends MalisisRenderer<TileEntity>
{
	private Shape cube = new Cube();
	private RenderParameters rp = new RenderParameters();

	private Set<BlockPos> positions;
	private int color;
	private Function<IBlockState, Integer> colorGetter;

	public BlockMarkerRenderer()
	{
		registerForRenderWorldLast();
	}

	@Override
	public boolean shouldRender(RenderWorldLastEvent event, IBlockAccess world)
	{
		return positions != null && positions.size() > 0;
	}

	@Override
	public void render()
	{
		next(GL11.GL_LINE_LOOP);
		disableTextures();
		GlStateManager.disableDepth();

		if (colorGetter == null)
			rp.colorMultiplier.set(color);

		ICamera camera = new Frustum();

		positions.stream().filter(p -> camera.isBoundingBoxInFrustum(AABBUtils.identity(p))).forEach(p -> {
			if (colorGetter != null)
				rp.colorMultiplier.set(colorGetter.apply(world.getBlockState(p)));
			cube.resetState();
			cube.translate(p.getX(), p.getY(), p.getZ());
			drawShape(cube, rp);
		});

		next();
		GlStateManager.enableDepth();
	}

	@ModMessage("markBlocks")
	public void set(Set<BlockPos> positions, int color)
	{
		this.positions = positions;
		this.color = color;
		this.colorGetter = null;
	}

	@ModMessage("markBlocks")
	public void set(Set<BlockPos> positions, Function<IBlockState, Integer> colorGetter)
	{
		this.positions = positions;
		this.colorGetter = colorGetter;
	}

}
