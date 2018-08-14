package rdthk.syringe.factories;

import rdthk.syringe.Syringe;
import rdthk.syringe.exceptions.SyringeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanObjectFactory implements ObjectFactory {
    private final Syringe syringe;
    private final Object module;
    private final Method method;

    public BeanObjectFactory(Syringe syringe, Object module, Method method) {
        this.syringe = syringe;
        this.method = method;
        this.module = module;
    }

    @Override
    public Object newInstance() {
        Class[] parameterTypes = method.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            arguments[i] = syringe.get(parameterTypes[i]);
        }

        try {
            return method.invoke(module, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SyringeException(e);
        }
    }
}
