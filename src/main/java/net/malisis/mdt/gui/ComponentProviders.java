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

package net.malisis.mdt.gui;

import static com.google.common.base.Preconditions.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.util.InheritanceClassMap;
import net.malisis.mdt.data.IGroup;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.component.GroupContainer;
import net.malisis.mdt.gui.component.information.BlockStateComponent;
import net.malisis.mdt.gui.component.information.InformationComponent;
import net.minecraft.block.state.IBlockState;

import com.google.common.collect.Maps;

/**
 * @author Ordinastie
 *
 */
public class ComponentProviders
{
	private static Map<String, BiFunction<DebugGui, IGroup, UIComponent<?>>> groupProviders = Maps.newHashMap();
	private static Map<Class<?>, BiFunction<DebugGui, ? extends IInformation<?>, UIComponent<?>>> infoProviders = new InheritanceClassMap<>(
			Maps.newHashMap());

	static
	{
		registerInformationProvider(IBlockState.class, BlockStateComponent::new);
	}

	public static void registerGroupProvider(String name, BiFunction<DebugGui, IGroup, UIComponent<?>> provider)
	{
		groupProviders.put(checkNotNull(name), checkNotNull(provider));
	}

	public static <T> void registerInformationProvider(Class<?> key, BiFunction<DebugGui, ? extends IInformation<T>, UIComponent<?>> provider)
	{
		infoProviders.put(checkNotNull(key), checkNotNull(provider));
	}

	public static UIComponent<?> getComponent(DebugGui gui, IGroup group)
	{
		return Optional.ofNullable(groupProviders.get(group.getName())).orElse(GroupContainer::new).apply(gui, group);
	}

	@SuppressWarnings("unchecked")
	private static <T> BiFunction<DebugGui, ? extends IInformation<T>, UIComponent<?>> getInformationProvider(IInformation<T> information)
	{
		return (BiFunction<DebugGui, ? extends IInformation<T>, UIComponent<?>>) infoProviders.get(information.getComponentKey());
	}

	public static <T> UIComponent<?> getComponent(DebugGui gui, IInformation<T> information)
	{
		Optional<BiFunction<DebugGui, IInformation<T>, UIComponent<?>>> o = Optional.ofNullable(getInformationProvider(information));
		return o.orElse(InformationComponent<T>::new).apply(gui, information);
	}
}
