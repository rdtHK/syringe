package rdthk.syringe;

import rdthk.syringe.exceptions.AmbiguousConstructorInjectionException;
import rdthk.syringe.exceptions.MissingValidConstructorException;
import rdthk.syringe.scopes.Singleton;
import org.junit.Before;
import rdthk.syringe.stubs.*;

import static org.junit.Assert.*;

public class SyringeTest {
    private Syringe syringe;

    @Before
    public void runBeforeTest() {
        syringe = new Syringe();
    }

    @org.junit.Test
    public void test_get_class_with_a_default_constructor_no_injects() {
        EmptyClass object = syringe.get(EmptyClass.class);
        assertSame(EmptyClass.class, object.getClass());
    }

    @org.junit.Test
    public void test_get_class_with_constructor_injection() {
        ConstructorInjectionClass object = syringe.get(ConstructorInjectionClass.class);
        assertSame(EmptyClass.class, object.dependency.getClass());
    }

    @org.junit.Test
    public void test_get_class_with_field_injection() {
        FieldInjectionClass object = syringe.get(FieldInjectionClass.class);
        assertSame(EmptyClass.class, object.dependency.getClass());
    }

    @org.junit.Test
    public void test_get_class_with_method_injection() {
        MethodInjectionClass object = syringe.get(MethodInjectionClass.class);
        assertSame(EmptyClass.class, object.dependency.getClass());
    }

    @org.junit.Test
    public void test_get_class_with_singleton_scope() {
        EmptySingleton objectA = syringe.get(EmptySingleton.class);
        EmptySingleton objectB = syringe.get(EmptySingleton.class);
        assertSame(objectA, objectB);
    }

    @org.junit.Test
    public void test_get_class_with_dependent_scope() {
        EmptyDependent objectA = syringe.get(EmptyDependent.class);
        EmptyDependent objectB = syringe.get(EmptyDependent.class);
        assertNotSame(objectA, objectB);
    }

    @org.junit.Test
    public void test_dependent_scope_is_the_default() {
        EmptyClass objectA = syringe.get(EmptyClass.class);
        EmptyClass objectB = syringe.get(EmptyClass.class);
        assertNotSame(objectA, objectB);
    }

    @org.junit.Test
    public void test_bind_to_interface() {
        syringe.bind(EmptyInterface.class, EmptyConcrete.class);
        EmptyInterface object = syringe.get(EmptyInterface.class);
        assertTrue(object instanceof EmptyConcrete);
    }

    @org.junit.Test
    public void test_set_scope() {
        TestScopeClass testValue = new TestScopeClass();
        syringe.putScope(TestScope.class, new TestScopeImpl(testValue));
        assertSame(testValue, syringe.get(TestScopeClass.class));
    }

    @org.junit.Test
    public void test_set_default_scope() {
        syringe.setDefaultScope(Singleton.class);
        EmptyClass a = syringe.get(EmptyClass.class);
        EmptyClass b = syringe.get(EmptyClass.class);
        assertSame(a, b);
    }

    @org.junit.Test
    public void test_modules() {
        Syringe syringe = new Syringe(new TestModule());
        NoInjectionClass obj = syringe.get(NoInjectionClass.class);
        assertEquals("foo", obj.str);
    }

    @org.junit.Test
    public void test_singleton_bean() {
        Syringe syringe = new Syringe(new TestModule());
        EmptyClass a = syringe.get(EmptyClass.class);
        EmptyClass b = syringe.get(EmptyClass.class);
        assertSame(a, b);
    }

    @org.junit.Test
    public void test_bean_parameter_injection() {
        Syringe syringe = new Syringe(new TestModule());
        ConstructorInjectionClass object = syringe.get(ConstructorInjectionClass.class);
        assertSame(EmptyClass.class, object.dependency.getClass());
    }

    @org.junit.Test(expected = MissingValidConstructorException.class)
    public void test_get_without_a_valid_constructor() {
        syringe.get(NoValidConstructorClass.class);
    }

    @org.junit.Test(expected = AmbiguousConstructorInjectionException.class)
    public void test_inject_annotation_in_two_constructors() {
        syringe.get(AmbiguousConstructorInjectionClass.class);
    }
}