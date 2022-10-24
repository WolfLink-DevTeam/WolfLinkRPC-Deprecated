package org.wolflink.common.wolflinkrpc.utils

import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.scanners.Scanners
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import org.wolflink.common.wolflinkrpc.RPCCore
import kotlin.reflect.KClass

object ReflectionUtil {

    fun getClasses(mainClass : Class<*>,packageName : String) : MutableSet<Class<*>>
    {
        val reflections = Reflections(
            ConfigurationBuilder()
                .setScanners(SubTypesScanner(false), ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(mainClass.classLoader))
                .filterInputsBy(FilterBuilder().includePackage(packageName)))
        return reflections.getSubTypesOf(Any::class.java)
    }
    fun filterClassesByAnnotation(classes : Set<Class<*>>, specifiedAnnotation: Class<*>) : MutableSet<Class<*>>
    {
        val resultClasses = classes.toMutableSet()
        for (clazz in classes)
        {
            for (annotation in clazz.declaredAnnotations)
            {
                if(annotation.annotationClass.qualifiedName == specifiedAnnotation.name) continue
                RPCCore.logger.warn("removed a class because it's annotation doesn't match the specified one.")
                resultClasses.remove(clazz)
            }
            // 如果没有指定的通配符，从集合中删除该元素
        }
        return resultClasses
    }
}