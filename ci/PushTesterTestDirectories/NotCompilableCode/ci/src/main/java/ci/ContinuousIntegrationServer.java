package ci;

/**
 * Not compilable code
 * Misses ; in line 11
 */
public class ContinuousIntegrationServer {

    public static void main(String[] args) throws Exception {
        // Not including ; after print statement causing code to to not compile
        System.out.println()
    }
}
