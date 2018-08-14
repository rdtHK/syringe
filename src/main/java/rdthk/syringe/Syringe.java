package rdthk.syringe;

import rdthk.syringe.factories.BeanObjectFactory;
import rdthk.syringe.factories.ObjectFactory;
import rdthk.syringe.factories.SimpleObjectFactory;
import rdthk.syringe.scopes.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Syringe {
    private final Map<Class, Binding> bindings;
    private final Map<Class<? extends Annotation>, Scope> scopes;
    private Class<? extends Annotation> defaultScope;

    private static class Binding {
        private final Class clazz;
        private final Scope scope;
        private final ObjectFactory factory;

        public Binding(Class clazz, Scope scope, ObjectFactory factory) {
            this.clazz = clazz;
            this.scope = scope;
            this.factory = factory;
        }

        public Object getInstance() {
            return scope.getInstance(clazz, factory);
        }
    }

    public Syringe(Object... modules) {
        bindings = new HashMap<>();
        scopes = new HashMap<>();

        putScope(Dependent.class, new DependentScope());
        putScope(Singleton.class, new SingletonScope());
        setDefaultScope(Dependent.class);
        
        for (Object module: modules) {
            bindModule(module);
        }
    }

    public void putScope(Class<? extends Annotation> annotation, Scope scope) {
        scopes.put(annotation, scope);
    }

    public void setDefaultScope(Class<? extends Annotation> scopeAnnotation) {
        defaultScope = scopeAnnotation;
    }

    public <T> void bind(Class<T> clazz, Class<? extends T> to) {

        ObjectFactory factory = new SimpleObjectFactory(this, to);
        Binding binding = new Binding(to, findScope(to), factory);

        bindings.put(clazz, binding);
    }

    public <T> T get(Class<T> clazz) {
        Binding binding;

        if (bindings.containsKey(clazz)) {
            binding = bindings.get(clazz);
        } else {
            ObjectFactory factory = new SimpleObjectFactory(this, clazz);
            binding = new Binding(clazz, findScope(clazz), factory);
        }

        return (T) binding.getInstance();
    }

    private void bindModule(Object module) {
        Class moduleClass = module.getClass();

        for (Method method: moduleClass.getMethods()) {
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }

            Class clazz = method.getReturnType();
            Scope scope = findScope(method);
            ObjectFactory factory = new BeanObjectFactory(this, module, method);
            Binding binding = new Binding(clazz, scope, factory);
            bindings.put(clazz, binding);
        }
    }

    private Scope findScope(AnnotatedElement clazz) {
        Scope scope = null;

        for (Class<? extends Annotation> annotation: scopes.keySet()) {
            if (clazz.isAnnotationPresent(annotation)) {
                scope = scopes.get(annotation);
            }
        }

        if (scope == null) {
            scope = scopes.get(defaultScope);
        }

        return scope;
    }

}
