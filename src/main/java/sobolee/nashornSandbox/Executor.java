package sobolee.nashornSandbox;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;

public class Executor {
    static Registry reg;
    static int val = 1;

    public static void main(String[] args){
        new Executor().run();
    }

    public void run(){
        try {
            reg = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = (NashornExecutorImpl.class).getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);

        Process process = null;
        NashornExecutor np = null;

        String killJs = "var fun1 = function(a) {\n" +
                "\tfun1(a+1);\n" +
                "    return \"greetings from javascript\";\n" +
                "};";

        try {
            process = builder.start();
            np = getRemoteObject();
            np.execute(killJs, Collections.emptyMap());
            np.close();
        }
        catch(ServerError e){
            System.out.println("Provided Javascript killed the JVM!");
            process.destroyForcibly();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Process returned: "+process.exitValue());
    }

    private NashornExecutor getRemoteObject() throws RemoteException {
        boolean isBound = false;
        NashornExecutor np = null;
        while(!isBound){
            try {
                np = (NashornExecutor) reg.lookup("NashornExecutorImpl");
                isBound = true;
            }
            catch(NotBoundException e){}
        }
        return np;
    }
}
