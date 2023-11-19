import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Config {
    public boolean colorMode = true;
    public String fileName = "";
    public String delimiter = ";";
    public RiskLevel riskLevel = RiskLevel.RISK_LEVEL_ALL;
    public boolean verbose = false;
    public Optional<String> testStream = Optional.empty();
    public boolean testingMode = false;
    public Map<Integer, Integer> checkerStats = new HashMap<>();
    public int lineNumber;

    public boolean isColorMode() {
        return colorMode;
    }

    public void setColorMode(boolean colorMode) {
        this.colorMode = colorMode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Optional<String> getTestStream() {
        return testStream;
    }

    public void setTestStream(Optional<String> testStream) {
        this.testStream = testStream;
    }

    public boolean isTestingMode() {
        return testingMode;
    }

    public void setTestingMode(boolean testingMode) {
        this.testingMode = testingMode;
    }

    public Map<Integer, Integer> getCheckerStats() {
        return checkerStats;
    }

    public void setCheckerStats(Map<Integer, Integer> checkerStats) {
        this.checkerStats = checkerStats;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public static String riskLevelToString(RiskLevel riskLevel) {
        switch (riskLevel) {
            case RISK_LEVEL_HIGH:
                return "High";
            case RISK_LEVEL_MEDIUM:
                return "Medium";
            case RISK_LEVEL_LOW:
                return "Low";
            case RISK_LEVEL_NONE:
                return "None";
            case RISK_LEVEL_ALL:
                return "All";
            default:
                return "Invalid";
        }
    }

    public static String riskLevelToDetailedString(RiskLevel riskLevel) {
        switch (riskLevel) {
            case RISK_LEVEL_HIGH:
                return "ONLY HIGH RISK ANTI-PATTERNS";
            case RISK_LEVEL_MEDIUM:
                return "ONLY MEDIUM AND HIGH RISK ANTI-PATTERNS";
            case RISK_LEVEL_LOW:
                return "ONLY ANTI-PATTERNS";
            case RISK_LEVEL_NONE:
                return "ALL ANTI-PATTERNS AND HINTS";
            case RISK_LEVEL_ALL:
                return "ALL ANTI-PATTERNS";
            default:
                return "INVALID";
        }
    }

    public static String patternTypeToString(PatternType patternType) {
        switch (patternType) {
            case PATTERN_TYPE_PHYSICAL_DATABASE_DESIGN:
                return "Physical Database Design";
            case PATTERN_TYPE_LOGICAL_DATABASE_DESIGN:
                return "Logical Database Design";
            case PATTERN_TYPE_QUERY:
                return "Query";
            case PATTERN_TYPE_APPLICATION:
                return "Application";
            default:
                return "Invalid";
        }
    }

    public static String getBooleanString(boolean status) {
        return status ? "ENABLED" : "DISABLED";
    }

    public static void validateRiskLevel(Config state) {
        if (state.getRiskLevel().ordinal() < RiskLevel.RISK_LEVEL_ALL.ordinal() || state.getRiskLevel().ordinal() > RiskLevel.RISK_LEVEL_HIGH.ordinal()) {
            System.out.println("INVALID RISK LEVEL :: " + state.getRiskLevel());
            System.exit(1);
        } else {
            System.out.println("> RISK LEVEL    :: " + riskLevelToDetailedString(state.getRiskLevel()));
        }
    }

    public static void validateFileName(Config state) {
        if (!state.getFileName().isEmpty()) {
            System.out.println("> SQL FILE NAME :: " + state.getFileName());
        }
    }

    public static void validateColorMode(Config state) {
        System.out.println("> COLOR MODE    :: " + getBooleanString(state.isColorMode()));
    }

    public static void validateVerbose(Config state) {
        System.out.println("> VERBOSE MODE  :: " + getBooleanString(state.isVerbose()));
    }

    public static void validateDelimiter(Config state) {
        System.out.println("> DELIMITER     :: " + state.getDelimiter());
    }
}