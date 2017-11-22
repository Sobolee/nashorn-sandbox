package sobolee.nashornSandbox;

import java.util.List;
import java.util.Map;

public interface Sandbox {

    Object evaluate(String script, Map<String, Object> args);

    Object invokeFunction(String function, String script, List<Object> args);

    interface SandboxBuilder {

        Sandbox build();
    }
}
