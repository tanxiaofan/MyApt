package com.github.tanxiaofan.apt;

import com.github.tanxiaofan.annotation.BindView;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @Description: 注解处理器demo，生成辅助类，完成绑定控件的操作
 * @Author: fan.tan
 * @CreateDate: 2019/11/25 14:17
 */
@AutoService(Processor.class)
public class BindViewApt extends AbstractProcessor {

    private static final Class<? extends Annotation> TARGET_CLASS = BindView.class;
    private ProcessingEnvironment mEnvironment;
    private Map<String, ClassCreator> map = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mEnvironment = processingEnvironment;
    }

    /**
     * @return Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * @return 要处理的注解集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(TARGET_CLASS.getName());
    }

    /**
     * 处理注解
     *
     * @return 返回true就代表改变或者生成语法树中的内容;返回false就是没有修改或者生成，通知编译器这个Round中的代码未发生变化
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processAnnotation(roundEnvironment);
        generateJavaFile();

        //返回true就代表改变或者生成语法树中的内容；
        //返回false就是没有修改或者生成，通知编译器这个Round中的代码未发生变化
        return true;
    }

    /**
     * 处理所有被注解标注的对象
     */
    private void processAnnotation(RoundEnvironment roundEnvironment) {
        //所有被注解的对象
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TARGET_CLASS);
        for (Element element : elements) {

            //成员变量
            VariableElement variableElement = (VariableElement) element;

            //包含该变量的类
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();

            //完整类名
            String fullClassName = typeElement.getQualifiedName().toString();

            //对应class的ClassCreator
            ClassCreator classCreator = map.get(fullClassName);
            if (classCreator == null) {
                classCreator = new ClassCreator(mEnvironment, typeElement);
                map.put(fullClassName, classCreator);
            }

            //id
            BindView bindView = (BindView) variableElement.getAnnotation(TARGET_CLASS);
            int id = bindView.value();

            classCreator.put(id, variableElement);

        }
    }

    /**
     * 生成Java文件
     */
    private void generateJavaFile() {
        for (ClassCreator classCreator : map.values()) {
            classCreator.generateJavaFile();
        }
    }
}
