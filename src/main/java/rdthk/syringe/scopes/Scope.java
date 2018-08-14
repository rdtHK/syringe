package rdthk.syringe.scopes;

import rdthk.syringe.factories.ObjectFactory;

public interface Scope {
    <T> T getInstance(Class<T> clazz, ObjectFactory factory);
}
