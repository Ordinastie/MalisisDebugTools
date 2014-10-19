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

package net.malisis.mdt.asm;

import static org.objectweb.asm.Opcodes.*;
import net.malisis.core.asm.AsmHook;
import net.malisis.core.asm.MalisisClassTransformer;
import net.malisis.core.asm.mappings.McpMethodMapping;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author Ordinastie
 *
 */
public class MDTTransformer extends MalisisClassTransformer
{

	@Override
	public void registerHooks()
	{
		register(autoWorldLoadHook());

	}

	private AsmHook autoWorldLoadHook()
	{
		McpMethodMapping startMethod = new McpMethodMapping("startGame", "func_71384_a", "net.minecraft.client.Minecraft", "()V");

		AsmHook ah = new AsmHook(startMethod);

		//NEW AutoWorldLoader
		//INVOKESPECIAL AutoWorldLoader.<init> () : void
		InsnList insert = new InsnList();
		insert.add(new TypeInsnNode(NEW, "net/malisis/mdt/AutoWorldLoader"));
		insert.add(new MethodInsnNode(INVOKESPECIAL, "net/malisis/mdt/AutoWorldLoader", "<init>", "()V"));

		ah.jumpToEnd().jump(-2).insert(insert);

		return ah;
	}

}
