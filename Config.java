import java.io.ByteArrayInputStream;

public class Config {
    public boolean colorMode = true;
    public String fileName = "";
    public String delimiter = ";";
    public RiskLevel riskLevel = RiskLevel.RISK_LEVEL_ALL;
    public boolean verbose = false;
    public ByteArrayInputStream testStream;
    public boolean testingMode = false;
    public Checker_Stats checkerStats = new Checker_Stats();
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

    public ByteArrayInputStream getTestStream() {
        return testStream;
    }

    public void setTestStream(ByteArrayInputStream testStream) {
        this.testStream = testStream;
    }

    public boolean isTestingMode() {
        return testingMode;
    }

    public void setTestingMode(boolean testingMode) {
        this.testingMode = testingMode;
    }

    public Checker_Stats getCheckerStats() {
        return checkerStats;
    }

    public void setCheckerStats(Checker_Stats checkerStats) {
        this.checkerStats = checkerStats;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public static RiskLevel intToRiskLevel(int value) {
        switch (value) {
            case 1:
                return RiskLevel.RISK_LEVEL_ALL;
            case 2:
                return RiskLevel.RISK_LEVEL_HIGH;
            case 3:
                return RiskLevel.RISK_LEVEL_MEDIUM;
            case 4:
                return RiskLevel.RISK_LEVEL_LOW;
            case 5:
                return RiskLevel.RISK_LEVEL_NONE;
            case 10:
                return RiskLevel.RISK_LEVEL_INVALID;
            default:
                throw new IllegalArgumentException("Invalid risk level value: " + value);
        }
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
