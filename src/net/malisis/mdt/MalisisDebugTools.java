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

import net.malisis.core.IMalisisMod;
import net.malisis.core.MalisisCore;
import net.malisis.core.configuration.Settings;
import net.malisis.core.util.modmessage.ModMessageManager;
import net.malisis.mdt.block.MdtBlock;
import net.malisis.mdt.renderer.AABBRenderer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

/**
 * @author Ordinastie
 *
 */
@Mod(modid = MalisisDebugTools.modid, name = MalisisDebugTools.modname, version = MalisisDebugTools.version, dependencies = "required-after:malisiscore")
public class MalisisDebugTools implements IMalisisMod
{
	public static final String modid = "mdt";
	public static final String modname = "Malisis Debug Tools";
	public static final String version = "${version}";

	public static MalisisDebugTools instance;
	public static Logger log;

	public MalisisDebugTools()
	{
		instance = this;
		MalisisCore.registerMod(this);
	}

	//#region IMalisisMod
	@Override
	public String getModId()
	{
		return modid;
	}

	@Override
	public String getName()
	{
		return modname;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public Settings getSettings()
	{
		return null;
	}

	//#end IMalisisMod

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		new MdtBlock().register();
		new KeyBindings();
		log = event.getModLog();
		ModMessageManager.register(instance, new MdtModMessage());
		ModMessageManager.register(instance, new AABBRenderer());
	}

	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		ClientCommandHandler.instance.registerCommand(new MDTCommand());
	}

}
