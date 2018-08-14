package rdthk.syringe.stubs;

import rdthk.syringe.Inject;

public class AmbiguousConstructorInjectionClass {
    @Inject
    public AmbiguousConstructorInjectionClass(int foo) {

    }

    @Inject
    public AmbiguousConstructorInjectionClass(boolean bar) {

    }

}
