package rdthk.syringe.stubs;

import rdthk.syringe.Inject;

public class ConstructorInjectionClass {
    public EmptyClass dependency;

    @Inject
    public ConstructorInjectionClass(EmptyClass dependency) {
        this.dependency = dependency;
    }
}
