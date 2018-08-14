package rdthk.syringe.stubs;

import rdthk.syringe.factories.ObjectFactory;
import rdthk.syringe.scopes.Scope;

public class TestScopeImpl implements Scope {

    private TestScopeClass object;

    public TestScopeImpl(TestScopeClass object) {
        this.object = object;
    }

    @Override
    public <T> T getInstance(Class<T> clazz, ObjectFactory factory) {
        return (T) object;
    }
}
