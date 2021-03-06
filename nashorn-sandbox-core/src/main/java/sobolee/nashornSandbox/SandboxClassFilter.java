package sobolee.nashornSandbox;

import jdk.nashorn.api.scripting.ClassFilter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SandboxClassFilter implements ClassFilter, Serializable {
    private final Set<String> allowedClasses = new HashSet<>();

    @Override
    public boolean exposeToScripts(String className) {
        return allowedClasses.contains(className);
    }

    public void add(String className) {
        allowedClasses.add(className);
    }

    public void remove(String className) {
        allowedClasses.remove(className);
    }
}
