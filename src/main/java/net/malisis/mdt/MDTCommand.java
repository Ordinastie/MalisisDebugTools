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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.malisis.core.MalisisCore;
import net.malisis.mdt.block.MdtBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * @author Ordinastie
 *
 */
public class MDTCommand extends CommandBase
{
	Set<String> parameters = new HashSet<>();

	public MDTCommand()
	{
		parameters.add("toggle");
		parameters.add("activate");
		parameters.add("deactivate");
		parameters.add("reset");
		parameters.add("texture");
		parameters.add("invisBlock");
	}

	@Override
	public String getCommandName()
	{
		return "mdt";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "mdt.command.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException
	{
		if (params.length == 0)
			throw new WrongUsageException("mdt.commands.usage", new Object[0]);

		if (!parameters.contains(params[0]))
			throw new WrongUsageException("mdt.commands.usage", new Object[0]);

		switch (params[0])
		{
			case "toggle":
				DebugTool.toggle();
				break;
			case "activate":
				DebugTool.activate();
				break;
			case "deactivate":
				DebugTool.deactivate();
				break;
			case "reset":
				DebugTool.reset();
				break;
			case "texture":
				new MDTTextureGui().display(true);
				break;
			case "invisBlock":
				MdtBlock.invisible = !MdtBlock.invisible;
				Minecraft.getMinecraft().renderGlobal.loadRenderers();
				break;
			default:
				MalisisCore.message("Not yet implemented");
				break;
		}

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] params, BlockPos pos)
	{
		if (params.length == 1)
			return getListOfStringsMatchingLastWord(params, parameters);
		else
			return null;
	}

}
