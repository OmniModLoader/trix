/*
 * MIT License
 *
 * Copyright (c) 2026 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.omnimc.trix.impl.visitors.hierarchy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.lumina.data.types.ClassData;
import org.omnimc.trix.api.asm.Compatibility;
import org.omnimc.trix.impl.hierarchy.HierarchyManager;

import java.lang.reflect.Modifier;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class HierarchyClassVisitor extends ClassVisitor {

    private final HierarchyManager hierarchyManager;
    private final Remapper remapper;

    private String obfuscatedClassName;
    private ClassData classData;

    public HierarchyClassVisitor(ClassVisitor classVisitor, Remapper remapper, HierarchyManager hierarchyManager) {
        super(Compatibility.ASM_VERSION, classVisitor);
        this.remapper = remapper;
        this.hierarchyManager = hierarchyManager;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        obfuscatedClassName = name;
        this.classData = new ClassData(remapper.map(name));

        classData.addDependentClass(superName);

        if (interfaces != null) {
            for (String anInterface : interfaces) {
                classData.addDependentClass(anInterface);
            }
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        String mappedDescriptor = remapper.mapDesc(descriptor);


        if (Modifier.isPrivate(access)) {
            this.classData.addPrivateField(name, remapper.mapFieldName(obfuscatedClassName, name, mappedDescriptor), mappedDescriptor);
            return super.visitField(access, name, descriptor, signature, value);
        }

        this.classData.addField(name, remapper.mapFieldName(obfuscatedClassName, name, mappedDescriptor), mappedDescriptor);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String mappedDescriptor = remapper.mapMethodDesc(descriptor);

        if (Modifier.isPrivate(access)) {
            this.classData.addPrivateMethod(name, remapper.mapMethodName(obfuscatedClassName, name, mappedDescriptor), mappedDescriptor);
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        this.classData.addMethod(name, remapper.mapMethodName(obfuscatedClassName, name, mappedDescriptor), mappedDescriptor);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        hierarchyManager.addClass(obfuscatedClassName, this.classData);
        super.visitEnd();
    }
}