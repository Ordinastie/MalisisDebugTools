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

import java.lang.reflect.Constructor;

import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.mdt.MalisisDebugTools;
import net.malisis.mdt.data.IInformation;
import net.malisis.mdt.gui.IInfoComponent;
import net.malisis.mdt.gui.component.information.DefaultInfoComp;

/**
 * @author Ordinastie
 *
 */
public class DefaultInformation<T> implements IInformation<T>
{
	private Class<? extends UIComponent> componentClass;
	protected UIComponent<? extends IInfoComponent<T>> component;
	protected String label;
	protected T value;
	private boolean invokeFailed = false;

	public <S extends UIComponent & IInfoComponent<T>> DefaultInformation(String label, Class<S> componentClass)
	{
		this.label = label;
		this.componentClass = componentClass != null ? componentClass : DefaultInfoComp.class;
	}

	public DefaultInformation(String label)
	{
		this(label, null);
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
	public void setValue(T value)
	{
		this.value = value;
	}

	@Override
	public <S extends UIComponent & IInfoComponent> S getComponent(MalisisGui gui)
	{
		if (component == null && !invokeFailed)
		{
			try
			{
				Constructor<? extends UIComponent> ctr = componentClass.getConstructor(MalisisGui.class);
				if (ctr == null)
				{
					invokeFailed = true;
					MalisisDebugTools.log.error("Could not find constructor for {}", componentClass.getSimpleName());
					return null;
				}
				component = ctr.newInstance(gui);
			}
			catch (ReflectiveOperationException e)
			{
				invokeFailed = true;
				e.printStackTrace();
			}
		}

		return (S) component;
	}
}
