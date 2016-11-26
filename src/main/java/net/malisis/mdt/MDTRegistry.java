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

package net.malisis.mdt;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import net.malisis.core.registry.AutoLoad;
import net.malisis.core.util.InheritanceClassMap;
import net.malisis.mdt.data.ICategory;
import net.malisis.mdt.data.IGroup;
import net.malisis.mdt.data.IInformation;
import net.minecraft.util.math.BlockPos;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * @author Ordinastie
 *
 */
@AutoLoad
public class MDTRegistry
{
	private static Set<ICategory> categories = Sets.newHashSet();
	private static Multimap<ICategory, Function<DebugTool, IGroup>> groupFactories = ArrayListMultimap.create();
	private static Map<Class<?>, Function<Object, String>> informationOutput = new InheritanceClassMap<>(Maps.newHashMap());

	static
	{
		registerInformationOutput(BlockPos.class, p -> {
			return p.getX() + ", " + p.getY() + ", " + p.getZ();
		});
	}

	public static void registerCategory(ICategory category)
	{
		categories.add(category);
	}

	public static Set<ICategory> getCaterogies()
	{
		return ImmutableSet.copyOf(categories);
	}

	public static void registerFactory(ICategory category, Function<DebugTool, IGroup> factory)
	{
		groupFactories.put(category, factory);
	}

	public static Collection<Function<DebugTool, IGroup>> getFactories(ICategory category)
	{
		return groupFactories.get(category);
	}

	@SuppressWarnings("unchecked")
	public static <T> void registerInformationOutput(Class<? extends T> clazz, Function<T, String> toString)
	{
		informationOutput.put(clazz, (Function<Object, String>) toString);
	}

	public static Function<Object, String> getInformationOutput(IInformation<?> information)
	{
		return informationOutput.getOrDefault(information.getValue().getClass(), o -> Objects.toString(o, " - "));
	}
}
