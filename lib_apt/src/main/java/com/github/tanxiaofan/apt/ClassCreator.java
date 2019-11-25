package com.github.tanxiaofan.apt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @Description: 负责生成Java文件
 * @Author: fan.tan
 * @CreateDate: 2019/11/25 15:04
 */
class ClassCreator {

    private static final ClassName VIEW = ClassName.get("android.view", "View");

    private ProcessingEnvironment mEnvironment;
    private TypeElement mTypeElement;

    ClassCreator(ProcessingEnvironment mEnvironment, TypeElement mTypeElement) {
        this.mEnvironment = mEnvironment;
        this.mTypeElement = mTypeElement;
    }

    //id-field
    private Map<Integer, VariableElement> map = new HashMap<>();

    void put(Integer id, VariableElement element) {
        map.put(id, element);
    }

    void generateJavaFile() {
        JavaFile javaFile = JavaFile.builder(packageName(), typeSpec()).build();
        try {
            javaFile.writeTo(mEnvironment.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String packageName() {
        return mEnvironment.getElementUtils().getPackageOf(mTypeElement).getQualifiedName().toString();
    }

    private String className() {
        return mTypeElement.getSimpleName().toString() + "_ViewBinding";
    }

    private TypeSpec typeSpec() {
        return TypeSpec.classBuilder(className())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructorMethod())
                .build();
    }

    private TypeName typeName(Element element) {
        return TypeName.get(element.getEnclosingElement().asType());
    }

    private ClassName className(String name) {
        return ClassName.bestGuess(name);
    }

    private MethodSpec constructorMethod() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(className(mTypeElement.getQualifiedName().toString()), "target")
                .addParameter(VIEW, "source");

        for (Map.Entry<Integer, VariableElement> entry : map.entrySet()) {
            String fieldName = entry.getValue().getSimpleName().toString();
            builder.addStatement("target.$L = ($L)target.findViewById($L)", fieldName, entry.getValue().asType().toString(), entry.getKey());
        }

        return builder.build();
    }
}
