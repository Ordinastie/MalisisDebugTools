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

import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.util.modmessage.ModMessage;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

/**
 * @author Ordinastie
 *
 */
public class AABBRenderer extends MalisisRenderer
{
	private static AABBRenderer instance;

	private AxisAlignedBB aabb;
	private int color;
	private int delay;
	private Shape cube = new Cube();
	private RenderParameters rp = new RenderParameters();

	public AABBRenderer()
	{
		registerForRenderWorldLast();
		instance = this;
	}

	@Override
	protected void initialize()
	{
		delay = delay++;
	}

	@Override
	public void render()
	{
		next(GL11.GL_LINE_STRIP);
		disableTextures();
		cube.resetState();

		cube.setBounds(aabb);

		rp.colorMultiplier.set(color);
		drawShape(cube, rp);
	}

	@ModMessage("renderAABB")
	public static void set(AxisAlignedBB aabb, int color, int delay)
	{
		instance.aabb = aabb;
		instance.color = color;
		instance.delay = delay;
	}
}
