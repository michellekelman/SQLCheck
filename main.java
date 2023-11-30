import java.util.HashMap;
import java.util.Map;

public class main {

    public static void configureChecker(Config state, String[] args) {
        Map<String, String> options = parseArgs(args);

        // Default Values
        state.riskLevel = RiskLevel.RISK_LEVEL_ALL;
        state.fileName = "";
        state.delimiter = ";";
        state.testingMode = false;
        state.verbose = false;
        state.colorMode = false;
        state.lineNumber = 1;

        // Configure checker
        state.colorMode = options.containsKey("c") || options.containsKey(("color_mode"));
        state.verbose = options.containsKey("v") || options.containsKey(("verbose"));

        if (options.containsKey("f")) {
            state.fileName = options.get("f");
        }
        if (options.containsKey("file_name")) {
            state.fileName = options.get("file_name");
        }
        if (options.containsKey("d")) {
            state.delimiter = options.get("d");
        }
        if (options.containsKey("delimiter")) {
            state.delimiter = options.get("delimiter");
        }
        if (options.containsKey("r")) {
            state.riskLevel = state.intToRiskLevel(Integer.parseInt(options.get("r")));
        }
        if (options.containsKey("risk_level")) {
            state.riskLevel = state.intToRiskLevel(Integer.parseInt(options.get("risk_level")));
        }

        // Run validators
        System.out.println("+-------------------------------------------------+");
        System.out.println("|                   SQLCHECK                      |");
        System.out.println("+-------------------------------------------------+");

        state.validateRiskLevel(state);
        state.validateFileName(state);
        state.validateColorMode(state);
        state.validateVerbose(state);
        state.validateDelimiter(state);

        System.out.println("-------------------------------------------------");
    }

    public static void usage() {
        System.out.println(
                "Command line options : sqlcheck <options>\n" +
                        "   -f -file_name          :  SQL file name\n" +
                        "   -r -risk_level         :  Set of anti-patterns to check\n" +
                        "                          :  1 (all anti-patterns, default) \n" +
                        "                          :  2 (only medium and high risk anti-patterns) \n" +
                        "                          :  3 (only high risk anti-patterns) \n" +
                        "   -c -color_mode         :  Display warnings in color mode \n" +
                        "   -v -verbose            :  Display verbose warnings \n" +
                        "   -d -delimiter          :  Query delimiter string (; by default) \n" +
                        "   -h -help               :  Print help message \n"
        );
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            options.put(args[i], args[i + 1]);
        }
        return options;
    }

    public static void main(String[] args) {
        Config state = new Config();
        boolean hasIssues = false;

        try {
            if (args.length == 0 || args[0].equals("-h") || args[0].equals("-help")) {
                usage();
                return;
            }

            configureChecker(state, args);

            // Invoke the checker
            hasIssues = Checker.check(state);

        } catch (Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        System.exit(hasIssues ? 1 : 0);
    }
}