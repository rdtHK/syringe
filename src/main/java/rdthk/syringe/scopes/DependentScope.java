package rdthk.syringe.scopes;

import rdthk.syringe.factories.ObjectFactory;

public class DependentScope implements Scope{
    @Override
    public <T> T getInstance(Class<T> clazz, ObjectFactory factory) {
        return (T) factory.newInstance();
    }
}
