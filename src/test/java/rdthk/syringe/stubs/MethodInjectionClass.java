package rdthk.syringe.stubs;

import rdthk.syringe.Inject;

public class MethodInjectionClass {
    public EmptyClass dependency;

    @Inject
    public void setDependency(EmptyClass dependency) {
        this.dependency = dependency;
    }
}
