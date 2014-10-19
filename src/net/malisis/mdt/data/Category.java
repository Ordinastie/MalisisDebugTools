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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ordinastie
 *
 */
public class Category implements ICategory
{
	private final String name;
	private final List<Group> groups = new ArrayList<>();

	public Category(String name)
	{
		this.name = name;
	}

	public Category(Group group)
	{
		this.name = group.getName();
		addGroup(group);
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void addGroup(Group group)
	{
		groups.add(group);
	}

	public void updateGroups()
	{
		for (Group group : groups)
			group.update();
	}

	@Override
	public List<Group> listGroups()
	{
		return groups;
	}
}
