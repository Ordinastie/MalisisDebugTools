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

package net.malisis.mdt.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.malisis.mdt.data.information.DefaultInformation;

/**
 * @author Ordinastie
 *
 */
public abstract class Group implements Iterable<IInformation>
{
	private Map<String, IInformation> infos = new LinkedHashMap<>();
	protected String name;
	protected boolean updated = false;
	protected boolean empty = true;

	public Group(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setUpdated(boolean updated)
	{
		this.updated = updated;
	}

	public boolean isUpdated()
	{
		return updated;
	}

	public boolean isEmpty()
	{
		return empty;
	}

	public void update()
	{
		updated = updateInformations();
	}

	public void clear()
	{
		empty = true;
	}

	public abstract boolean updateInformations();

	protected void add(String label)
	{
		add(new DefaultInformation(label));
	}

	public void add(IInformation<?> info)
	{
		infos.put(info.getLabel(), info);
	}

	protected void set(String key, String value)
	{
		IInformation di = infos.get(key);
		if (di == null)
			throw new IllegalArgumentException("No information found with key " + key + ", information need to be added first.");

		di.setValue(value);
		empty = false;
	}

	@Override
	public Iterator<IInformation> iterator()
	{
		return infos.values().iterator();
	}
}
