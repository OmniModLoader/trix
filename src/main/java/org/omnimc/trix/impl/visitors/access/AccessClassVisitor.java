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

package org.omnimc.trix.impl.visitors.access;

import org.objectweb.asm.*;
import org.omnimc.lumina.data.types.ClassData;
import org.omnimc.lumina.mcmap.McMap;
import org.omnimc.lumina.namespace.Namespace;
import org.omnimc.trix.api.access.Access;
import org.omnimc.trix.api.access.info.ClassAccess;
import org.omnimc.trix.api.access.info.FieldAccess;
import org.omnimc.trix.api.access.info.MethodAccess;
import org.omnimc.trix.api.asm.Compatibility;
import org.omnimc.trix.api.visitor.ChainedClassVisitor;
import org.omnimc.trix.impl.access.AccessManager;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class AccessClassVisitor extends ClassVisitor implements ChainedClassVisitor {

    private final AccessManager accessManager;

    private ClassAccess classAccess;

    public AccessClassVisitor() {
        super(Compatibility.ASM_VERSION);
        this.accessManager = AccessManager.getInstance();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ClassAccess classAccess = accessManager.getClassAccess(name);
        if (classAccess == null) {
            super.visit(version, access, name, signature, superName, interfaces);
            return;
        }

        this.classAccess = classAccess;

        if (classAccess.getAccess() != Access.DEFAULT_ACCESS) {
            access = classAccess.getAccess();
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        ClassAccess classAccess = accessManager.getClassAccess(name);

        if (classAccess.getAccess() != Access.DEFAULT_ACCESS) {
            access = classAccess.getAccess();
        }

        return super.visitModule(name, access, version);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldAccess fieldAccess = classAccess.getFieldAccess(name, descriptor);
        int chosenAccess = fieldAccess.getAccess();

        if (chosenAccess != Access.DEFAULT_ACCESS) {
            access = chosenAccess;
        }

        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodAccess methodAccess = classAccess.getMethodAccess(name, descriptor);
        int chosenAccess = methodAccess.getAccess();

        if (chosenAccess != Access.DEFAULT_ACCESS) {
            access = chosenAccess;
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        accessManager.removeAccess(classAccess);
    }

    @Override
    public ClassVisitor withNext(ClassVisitor next) {
        this.cv = next;
        return this;
    }
}