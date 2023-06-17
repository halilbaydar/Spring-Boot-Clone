package com.java.myspring.stream

import my.spring.boot.annotation.MyGetMapping
import my.spring.boot.annotation.MyPostMapping
import my.spring.boot.annotation.MyPutMapping
import my.spring.boot.annotation.MyRestController
import my.spring.boot.constant.HttpMethod
import my.spring.boot.context.SpringApplicationContext
import java.lang.reflect.Method
import java.net.URI
import java.util.stream.Stream

fun <T> Stream<T>.findFirst(predicate: (t: T) -> Boolean): T? {
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val t = iterator.next()
        if (predicate(t)) {
            return t
        }
    }
    return null
}

fun findEndpointHandler(controller: Class<*>, uri: URI, requestMethod: String?): Method? {
    return Stream.of(*controller.declaredMethods)
        .findFirst { handler: Method ->
            when (requestMethod) {
                HttpMethod.GET -> {
                    run {
                        if (handler.isAnnotationPresent(MyGetMapping::class.java)) {
                            val mapping = handler.getAnnotation(MyGetMapping::class.java)
                            val path: String = mapping.value
                            return@findFirst path == uri.path
                        }
                    }
                }
                HttpMethod.POST -> {
                    run {
                        if (handler.isAnnotationPresent(MyPostMapping::class.java)) {
                            val mapping = handler.getAnnotation(MyPostMapping::class.java)
                            val path: String = mapping.value
                            return@findFirst path == uri.path
                        }
                    }
                }
                HttpMethod.PUT -> {
                    if (handler.isAnnotationPresent(MyPutMapping::class.java)) {
                        val mapping = handler.getAnnotation(MyPutMapping::class.java)
                        val path: String = mapping.value
                        return@findFirst path == uri.path
                    }
                }
            }
            false
        }
}

fun findController(uri: URI, requestMethod: String?): MutableMap.MutableEntry<Class<*>, Any>? {
    return SpringApplicationContext.getInstanceStream()
        .findFirst { (cls): Map.Entry<Class<*>, Any?> ->
            if (!cls.isAnnotationPresent(MyRestController::class.java)) {
                return@findFirst false
            }
            val handler = findEndpointHandler(cls, uri, requestMethod)
            return@findFirst handler != null
        }
}