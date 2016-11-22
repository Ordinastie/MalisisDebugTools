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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.malisis.mdt.DebugTool;
import net.malisis.mdt.data.ICategory;
import net.malisis.mdt.data.IGroup;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * @author Ordinastie
 *
 */
public class Categories
{
	private static Set<ICategory> categories = Sets.newHashSet();
	private static Multimap<ICategory, Function<DebugTool, IGroup>> factories = ArrayListMultimap.create();

	public static final ICategory BLOCK_CATEGORY = new BlockCategory();
	public static final ICategory ITEM_CATEGORY = new ItemCategory();

	static
	{}

	public static void registerCategory(ICategory category)
	{
		categories.add(category);
	}

	public static void registerFactory(ICategory category, Function<DebugTool, IGroup> factory)
	{
		factories.put(category, factory);
	}

	public static List<IGroup> generateGroups(DebugTool debugTool, ICategory category)
	{
		return factories.get(category).stream().map(f -> f.apply(debugTool)).filter(Objects::nonNull).collect(Collectors.toList());
	}
}
