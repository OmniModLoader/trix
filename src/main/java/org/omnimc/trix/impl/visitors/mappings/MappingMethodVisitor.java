/*
 * MIT License
 *
 * Copyright (c) 2024-2026 OmniMC
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

package org.omnimc.trix.impl.visitors.mappings;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.api.asm.Compatibility;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MappingMethodVisitor extends MethodVisitor {

    private final String parentClass;
    private final Remapper remapper;

    protected MappingMethodVisitor(String parentClass, Remapper remapper, MethodVisitor methodVisitor) {
        super(Compatibility.ASM_VERSION, methodVisitor);
        this.parentClass = parentClass;
        this.remapper = remapper;
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        if (bootstrapMethodArguments != null) {

            for (int i = 0; i < bootstrapMethodArguments.length; i++) {
                if (bootstrapMethodArguments[i] instanceof Type) { // this is descriptors
                    bootstrapMethodArguments[i] = remapper.mapValue(bootstrapMethodArguments[i]);
                } else if (bootstrapMethodArguments[i] instanceof Handle) {
                    String owner = ((Handle) bootstrapMethodArguments[i]).getOwner();
                    String desc = ((Handle) bootstrapMethodArguments[i]).getDesc();

                    if (desc.contains("(")) {
                        desc = remapper.mapMethodDesc(desc);
                    } else {
                        desc = remapper.mapDesc(desc);
                    }

                    String handleName = desc.contains("(") ? remapper.mapMethodName(owner, ((Handle) bootstrapMethodArguments[i]).getName(), desc) : remapper.mapFieldName(owner, ((Handle) bootstrapMethodArguments[i]).getName(), desc);

                    int tag = ((Handle) bootstrapMethodArguments[i]).getTag();
                    boolean anInterface = ((Handle) bootstrapMethodArguments[i]).isInterface();

                    bootstrapMethodArguments[i] = new Handle(tag, remapper.mapType(owner), handleName, desc, anInterface);
                }
            }
        }

        super.visitInvokeDynamicInsn(name, remapper.mapMethodDesc(descriptor), (Handle) remapper.mapValue(bootstrapMethodHandle), bootstrapMethodArguments);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, remapper.mapDesc(descriptor),
                remapper.mapSignature(signature, true), start, end, index);
    }

    @Override
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(remapper.mapValue(value));
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type,
                numLocal,
                remapFrameTypes(numLocal, local),
                numStack,
                remapFrameTypes(numStack, stack));
    }

    private Object[] remapFrameTypes(final int numTypes, final Object[] frameTypes) {
        if (frameTypes == null) {
            return null;
        }
        Object[] remappedFrameTypes = null;
        for (int i = 0; i < numTypes; ++i) {
            if (frameTypes[i] instanceof String) {
                if (remappedFrameTypes == null) {
                    remappedFrameTypes = new Object[numTypes];
                    System.arraycopy(frameTypes, 0, remappedFrameTypes, 0, numTypes);
                }

                String mapType = remapper.mapType((String) frameTypes[i]);
                if (mapType.contains("[")) {
                    mapType = remapper.mapDesc((String) frameTypes[i]);
                }

                remappedFrameTypes[i] = mapType;
            }
        }
        return remappedFrameTypes == null ? frameTypes : remappedFrameTypes;
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        String remappedType = remapper.mapType(type);
        super.visitTypeInsn(opcode, type.contains("[") ? remapper.mapDesc(remappedType) : remappedType);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        String mappedName = remapper.mapFieldName(owner, name, descriptor);

        super.visitFieldInsn(opcode, remapper.mapType(owner), mappedName, remapper.mapDesc(descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        String mappedDescriptor = remapper.mapMethodDesc(descriptor);
        String ownersName = owner.replace("[", "");

        if (ownersName.startsWith("L") && ownersName.endsWith(";")) {
            ownersName = ownersName.substring(1, ownersName.length() - 1);
        }

        String remapedName = remapper.mapMethodName(ownersName, name, mappedDescriptor);

        if (owner.contains("[")) {
            super.visitMethodInsn(opcode, remapper.mapDesc(owner), remapedName, mappedDescriptor, isInterface);
            return;
        }

        super.visitMethodInsn(opcode, remapper.mapType(ownersName), remapedName, mappedDescriptor, isInterface);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, remapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitInsnAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type == null ? null : remapper.mapType(type));
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return super.visitParameterAnnotation(parameter, remapper.mapDesc(descriptor), visible);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(remapper.mapDesc(descriptor), visible);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        super.visitMultiANewArrayInsn(remapper.mapDesc(descriptor), numDimensions);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTryCatchAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    }

}