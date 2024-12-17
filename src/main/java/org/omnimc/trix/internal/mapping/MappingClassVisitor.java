/*
 * MIT License
 *
 * Copyright (c) 2024-2025 OmniMC
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
 */

package org.omnimc.trix.internal.mapping;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.chain.ChainedClassVisitor;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingClassVisitor extends ClassVisitor implements ChainedClassVisitor {

    private final Remapper remapper;

    private String currentClass;

    public MappingClassVisitor(ClassVisitor classVisitor, Remapper remapper) {
        super(Opcodes.ASM9, classVisitor);
        this.remapper = remapper;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.currentClass = name;
        super.visit(version, access, remapper.mapType(name), remapper.mapSignature(signature, false),
                remapper.mapType(superName), interfaces == null ? null : remapper.mapTypes(interfaces));
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        String methodDesc = descriptor == null ? null : remapper.mapMethodDesc(descriptor);
        super.visitOuterClass(remapper.mapType(owner), remapper.mapMethodName(owner, name, methodDesc), methodDesc);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(remapper.mapType(name),
                outerName == null ? null : remapper.mapType(outerName),
                innerName == null ? null : remapper.mapType(innerName), access);
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        super.visitPermittedSubclass(remapper.mapType(permittedSubclass));
    }

    @Override
    public void visitNestHost(String nestHost) {
        super.visitNestHost(remapper.mapType(nestHost));
    }

    @Override
    public void visitNestMember(String nestMember) {
        super.visitNestMember(remapper.mapType(nestMember));
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        return super.visitRecordComponent(
                name,
                remapper.mapDesc(descriptor),
                remapper.mapSignature(signature, true));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access,
                remapper.mapFieldName(currentClass, name, descriptor),
                remapper.mapDesc(descriptor),
                remapper.mapSignature(signature, true), remapper.mapValue(value));
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String mappedMethodDesc = remapper.mapMethodDesc(descriptor);
        MethodVisitor methodVisitor = super.visitMethod(access,
                remapper.mapMethodName(currentClass, name, mappedMethodDesc),
                mappedMethodDesc,
                remapper.mapSignature(signature, false),
                exceptions == null ? null : remapper.mapTypes(exceptions));

        return new MappingMethodVisitor(currentClass, remapper, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(remapper.mapDesc(descriptor), visible);
    }

    @Override
    public ClassVisitor withNext(ClassVisitor next) {
        this.cv = next;
        return this;
    }
}