package rdthk.syringe.factories;

import rdthk.syringe.Inject;
import rdthk.syringe.Syringe;
import rdthk.syringe.exceptions.AmbiguousConstructorInjectionException;
import rdthk.syringe.exceptions.MissingValidConstructorException;
import rdthk.syringe.exceptions.SyringeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleObjectFactory implements ObjectFactory {
    private final Syringe syringe;
    private final Class clazz;

    public SimpleObjectFactory(Syringe syringe, Class clazz) {
        this.syringe = syringe;
        this.clazz = clazz;
    }

    @Override
    public Object newInstance() {
        Object object = instantiate(clazz);
        injectPublicMethods(object);
        injectPublicFields(object);
        return object;
    }

    private void injectPublicFields(Object object) {
        Field[] fields = clazz.getFields();

        for (Field field: fields) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            try {
                field.set(object, syringe.get(field.getType()));
            } catch (IllegalAccessException e) {
                throw new SyringeException(e);
            }
        }
    }

    private void injectPublicMethods(Object object) {
        Method[] methods = clazz.getMethods();

        for (Method method: methods) {
            if (!method.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class[] parameterTypes = method.getParameterTypes();
            Object[] arguments = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                arguments[i] = syringe.get(parameterTypes[i]);
            }

            try {
                method.invoke(object, arguments);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new SyringeException(e);
            }
        }
    }

    private Object instantiate(Class clazz) {
        Constructor constructor = findAnnotatedConstructor(clazz);

        if (constructor == null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                throw new MissingValidConstructorException(e);
            }
        }

        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = syringe.get(parameterTypes[i]);
        }

        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SyringeException(e);
        }
    }

    private Constructor findAnnotatedConstructor(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        Constructor annotatedConstructor = null;

        for (Constructor c: constructors) {
            if (c.isAnnotationPresent(Inject.class)) {
                if (annotatedConstructor != null) {
                    throw new AmbiguousConstructorInjectionException("On class '" + clazz.getName() + "'.");
                }
                annotatedConstructor = c;
            }
        }

        return annotatedConstructor;
    }
}
