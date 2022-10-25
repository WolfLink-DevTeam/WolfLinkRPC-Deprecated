package org.wolflink.common.wolflinkrpc.utils

import org.reflections.Reflections
import org.reflections.ReflectionsException
import org.reflections.scanners.ResourcesScanner
import org.reflections.scanners.Scanners
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import org.wolflink.common.wolflinkrpc.RPCCore
import org.wolflink.common.wolflinkrpc.api.annotations.AnalyseFunction
import org.wolflink.common.wolflinkrpc.api.annotations.CommandFunction
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.Scanner
import javax.management.ReflectionException
import kotlin.reflect.KClass

object ReflectionUtil {

    fun getClassesByAnnotation(packageName : String,specifiedAnnotation: Class<out Annotation>) : MutableSet<Class<*>>
    {
//        val reflections = Reflections(
//            ConfigurationBuilder()
//                .setScanners(SubTypesScanner(false), ResourcesScanner())
//                .setUrls(ClasspathHelper.forClassLoader(mainClass.classLoader))
//                .filterInputsBy(FilterBuilder().includePackage(packageName)))
        var classes = mutableSetOf<Class<*>>()
        try {
            val reflections = Reflections(packageName)
            classes = reflections.getTypesAnnotatedWith(specifiedAnnotation)
            RPCCore.logger.info("Scanned ${classes.size} classes.")
        } catch (ignore : ReflectionsException) { }
        return classes
    }
//    fun filterClassesByAnnotation(classes : Set<Class<*>>, specifiedAnnotation: Class<*>) : MutableSet<Class<*>>
//    {
//        val resultClasses = classes.toMutableSet()
//        for (clazz in classes)
//        {
//            for (annotation in clazz.declaredAnnotations)
//            {
//                if(annotation.annotationClass.qualifiedName == specifiedAnnotation.name) continue
//                RPCCore.logger.warn("removed a class because it's annotation doesn't match the specified one.")
//                resultClasses.remove(clazz)
//            }
//            // 如果没有指定的通配符，从集合中删除该元素
//        }
//        return resultClasses
//    }
    fun getMethodsByAnnotation(classes: Set<Class<*>>,specifiedAnnotation: Class<*>) : MutableSet<Method>
    {
        val set = mutableSetOf<Method>()
        for (clazz in classes)
        {
            for (method in clazz.declaredMethods)
            {
                for (annotation in method.annotations)
                {
                    if(annotation.annotationClass.qualifiedName == specifiedAnnotation.name)
                    {
                        set.add(method)
                        break
                    }
                }
            }
        }
        return set
    }
    fun getFieldsByAnnotation(classes: Set<Class<*>>, specifiedAnnotation: Class<*>) : MutableSet<Field>
    {
        val set = mutableSetOf<Field>()
        for (clazz in classes)
        {
            for (field in clazz.declaredFields)
            {
                for (annotation in field.annotations)
                {
                    if(annotation.annotationClass.qualifiedName == specifiedAnnotation.name)
                    {
                        set.add(field)
                        break
                    }
                }
            }
        }
        return set
    }
}