package rdthk.syringe.stubs;

import rdthk.syringe.Bean;
import rdthk.syringe.scopes.Singleton;

public class TestModule {
    @Bean
    public NoInjectionClass buildDependency() {
        return new NoInjectionClass("foo");
    }

    @Bean
    @Singleton
    public EmptyClass buildEmptyClass() {
        return new EmptyClass();
    }

    @Bean
    public ConstructorInjectionClass buildStubConstructorInject(EmptyClass dependency) {
        return new ConstructorInjectionClass(dependency);
    }
}
