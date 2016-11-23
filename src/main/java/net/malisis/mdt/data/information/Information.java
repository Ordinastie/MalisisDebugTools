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

package net.malisis.mdt.data.information;

import net.malisis.mdt.data.IInformation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * @author Ordinastie
 *
 */
public class Information<T> implements IInformation<T>
{
	protected String label;
	protected T value;
	protected Object key;

	protected Information(String label, T value, Object key)
	{
		this.label = label;
		this.value = value;
		this.key = key;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public T getValue()
	{
		return value;
	}

	@Override
	public Object getComponentKey()
	{
		return key;
	}

	public static <T> Information<T> of(String label, T value)
	{
		return new Information<>(label, value, Information.class);
	}

	public static Information<BlockPos> of(String label, BlockPos pos)
	{
		return new Information<>(label, pos, BlockPos.class);
	}

	public static Information<IBlockState> of(String label, IBlockState state)
	{
		return new Information<>(label, state, BlockPos.class);
	}
}
