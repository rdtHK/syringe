package rdthk.syringe.scopes;

import rdthk.syringe.factories.ObjectFactory;

import java.util.HashMap;
import java.util.Map;

public class SingletonScope implements Scope {
    private final Map<Class, Object> objects;

    public SingletonScope() {
        objects = new HashMap<>();
    }

    @Override
    public synchronized <T> T getInstance(Class<T> clazz, ObjectFactory factory) {
        if (objects.containsKey(clazz)) {
            return (T) objects.get(clazz);
        }

        Object object = factory.newInstance();
        objects.put(clazz, object);
        return (T) object;
    }
}
