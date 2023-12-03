import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {
    static boolean check(Config state) {
        boolean hasIssues = false;
        BufferedReader input = null;

        // Set up stream
        try {
            if (state.isTestingMode()) {
                input = new BufferedReader(new InputStreamReader(state.getTestStream()));
            } else if (state.getFileName().isEmpty()) {
                input = new BufferedReader(new InputStreamReader(System.in));
            } else {
                input = new BufferedReader(new FileReader(state.getFileName()));
            }

            StringBuilder sqlStatement = new StringBuilder();
            state.setLineNumber(1);

            System.out.println("==================== Results ===================");

            // Go over the input stream
            String line;
            while ((line = input.readLine()) != null) {

                // Get a statement from the input stream
                String[] fragments = line.split(String.valueOf(state.getDelimiter()));

                // Append fragment to statement
                for (String fragment : fragments) {
                    if (!fragment.isEmpty()) {
                        sqlStatement.append(fragment).append(" ");
                    }
                }

                // Check the statement
                checkStatement(state, sqlStatement.toString());

                // Reset statement
                sqlStatement.setLength(0);
            }

            // Print summary
            if (state.getCheckerStats().getAllLevelCounter() == 0) {
                System.out.println("No issues found.");
            } else {
                System.out.println("\n==================== Summary ===================");
                System.out.println("All Anti-Patterns and Hints  :: " + state.getCheckerStats().getAllLevelCounter());
                System.out.println(">  High Risk   :: " + state.getCheckerStats().getWarnLevelCounter());
                System.out.println(">  Medium Risk :: " + state.getCheckerStats().getInfoLevelCounter());
                System.out.println(">  Low Risk    :: " + state.getCheckerStats().getDebugLevelCounter());
                System.out.println(">  Hints       :: " + state.getCheckerStats().getTraceLevelCounter());
                hasIssues = true;
            }

            // Skip destroying System.in
            if (state.getFileName().isEmpty()) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return hasIssues;
    }

    public static String wrapText(String text) {
        int lineLength = 80;
        StringBuilder wrapped = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(text);

        boolean newline = false;
        boolean newPara = false;

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();

            if (wrapped.length() == 0) {
                wrapped.append(word);
            } else {
                if (word.equals("●")) {
                    wrapped.append("\n\n");
                    newPara = true;
                } else {
                    newPara = false;
                }

                int spaceLeft = lineLength - wrapped.length();
                if (spaceLeft < word.length() + 1 || newline) {
                    wrapped.append('\n').append(word);
                    spaceLeft = lineLength - word.length();
                } else {
                    if (!newPara) {
                        wrapped.append(' ').append(word);
                    } else {
                        wrapped.append(word);
                    }
                    spaceLeft -= word.length() + 1;
                }

                newline = word.endsWith(":");
            }
        }

        return wrapped.toString();
    }

    public static void printMessage(Config state, String sqlStatement, boolean printStatement,
                                    RiskLevel patternRiskLevel, PatternType patternType,
                                    String title, String message) {

        ColorModifier red = new ColorModifier(ColorCode.FG_RED, state.isColorMode(), true);
        ColorModifier green = new ColorModifier(ColorCode.FG_GREEN, state.isColorMode(), true);
        ColorModifier blue = new ColorModifier(ColorCode.FG_BLUE, state.isColorMode(), true);
        ColorModifier regular = new ColorModifier(ColorCode.FG_DEFAULT, state.isColorMode(), false);

        if (printStatement) {
            System.out.println("\n-------------------------------------------------");
            if (state.isColorMode()) {
                System.out.println("SQL Statement at line " + state.getLineNumber() + ": " +
                        red + wrapText(sqlStatement) + state.getDelimiter() + regular);
            } else {
                System.out.println("SQL Statement at line " + state.getLineNumber() + ": " +
                        wrapText(sqlStatement) + state.getDelimiter());
            }
        }

        if (state.isColorMode()) {
            if (!state.getFileName().isEmpty()) {
                System.out.print("[" + state.getFileName() + "]: ");
            }
            System.out.println("(" + green + patternRiskLevel + regular + ") " +
                    blue + title + regular);
        } else {
            if (!state.getFileName().isEmpty()) {
                System.out.print("[" + state.getFileName() + "]: ");
            }
            System.out.println("(" + patternRiskLevel + ") " +
                    "(" + patternType + ") " + title);
        }

        // Print detailed message only in verbose mode
        if (state.isVerbose()) {
            System.out.println(wrapText(message));
        }

        // Update checker stats
        switch (patternRiskLevel) {
            case RISK_LEVEL_HIGH:
                state.checkerStats.setWarnLevelCounter(state.checkerStats.getWarnLevelCounter()+1);
            case RISK_LEVEL_MEDIUM:
                state.checkerStats.setInfoLevelCounter(state.checkerStats.getInfoLevelCounter()+1);
            case RISK_LEVEL_LOW:
                state.checkerStats.setDebugLevelCounter(state.checkerStats.getDebugLevelCounter()+1);
            case RISK_LEVEL_NONE:
                state.checkerStats.setTraceLevelCounter(state.checkerStats.getTraceLevelCounter()+1);
            case RISK_LEVEL_INVALID:
                state.checkerStats.setErrorLevelCounter(state.checkerStats.getErrorLevelCounter()+1);
        }
        state.checkerStats.setAllLevelCounter(state.checkerStats.getAllLevelCounter()+1);
    }

    public static void noMinCheckPattern(Config state, String sqlStatement, boolean printStatement,
                                    Pattern antiPattern, RiskLevel patternRiskLevel, PatternType patternType,
                                    String title, String message, boolean exists) {

        // Check log level
        if (patternRiskLevel.getLevel() < state.getRiskLevel().getLevel()) {
            return;
        }

        boolean found = false;
        Matcher matcher = antiPattern.matcher(sqlStatement);
        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<String> violations = new ArrayList<>();

        try {
            while (matcher.find()) {
                found = true;
                positions.add(matcher.start());
                violations.add(matcher.group(1));
            }

            if (found == exists) {

                // Print message
                printMessage(state, sqlStatement, printStatement, patternRiskLevel, patternType, title, message);

                if (exists) {
                    String matchingExpression = violations.get(0);

                    if (state.isColorMode()) {
                        System.out.print("[Matching Expression: \033[34m" + wrapText(matchingExpression) +
                                "\033[0m" + "]");
                    } else {
                        System.out.print("[Matching Expression: " + wrapText(matchingExpression) + "]");
                    }
                    System.out.println("\n\n");

                    // TOGGLE PRINT STATEMENT
                    printStatement = false;
                }
            }
        } catch (Exception e) {
            // Syntax error in the regular expression
            System.out.println(e);
        }
    }

    public static void checkPattern(Config state, String sqlStatement, boolean printStatement,
                             Pattern antiPattern, RiskLevel patternRiskLevel, PatternType patternType,
                             String title, String message, boolean exists, int minCount) {

        // Check log level
        if (patternRiskLevel.getLevel() < state.getRiskLevel().getLevel()) {
            return;
        }

        boolean found = false;
        Matcher matcher = antiPattern.matcher(sqlStatement);
        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<String> violations = new ArrayList<>();
        int count = 0;

        try {
            while (matcher.find()) {
                found = true;
                positions.add(matcher.start());
                violations.add(matcher.group(1));
            }

            if (found == exists && count > minCount) {

                // Print message
                printMessage(state, sqlStatement, printStatement, patternRiskLevel, patternType, title, message);

                if (exists) {
                    String matchingExpression = violations.get(0);

                    if (state.isColorMode()) {
                        System.out.print("[Matching Expression: \033[34m" + wrapText(matchingExpression) +
                                "\033[0m" + "]");
                    } else {
                        System.out.print("[Matching Expression: " + wrapText(matchingExpression) + "]");
                    }
                    System.out.println("\n\n");

                    // TOGGLE PRINT STATEMENT
                    printStatement = false;
                }
            }
        } catch (Exception e) {
            // Syntax error in the regular expression
        }
    }

    static void checkStatement(Config state, String sqlStatement) {
        // TRANSFORM TO LOWER CASE
        String statement = sqlStatement.toLowerCase();

        // REMOVE SPACE
        statement = statement.replaceAll("^ +| +$|( ) +", "$1");

        // CHECK FOR LEADING NEWLINE
        if (!statement.isEmpty() && statement.charAt(0) == '\n') {
            statement = statement.substring(1);
            state.setLineNumber(state.getLineNumber() + 1);
        }

        // RESET
        boolean print_statement = true;

        // LOGICAL DATABASE DESIGN
        AP_Checker.checkMultiValuedAttribute(state, statement, print_statement);
        AP_Checker.checkRecursiveDependency(state, statement, print_statement);
        AP_Checker.checkPrimaryKeyExists(state, statement, print_statement);
        AP_Checker.checkGenericPrimaryKey(state, statement, print_statement);
        AP_Checker.checkForeignKeyExists(state, statement, print_statement);
        AP_Checker.checkVariableAttribute(state, statement, print_statement);
        AP_Checker.checkMetadataTribbles(state, statement, print_statement);

        // PHYSICAL DATABASE DESIGN
        AP_Checker.checkFloat(state, statement, print_statement);
        AP_Checker.checkValuesInDefinition(state, statement, print_statement);
        AP_Checker.checkExternalFiles(state, statement, print_statement);
        AP_Checker.checkIndexCount(state, statement, print_statement);
        AP_Checker.checkIndexAttributeOrder(state, statement, print_statement);

        // QUERY
        AP_Checker.checkSelectStar(state, statement, print_statement);
        AP_Checker.checkJoinWithoutEquality(state, statement, print_statement);
        AP_Checker.checkNullUsage(state, statement, print_statement);
        AP_Checker.checkNotNullUsage(state, statement, print_statement);
        AP_Checker.checkConcatenation(state, statement, print_statement);
        AP_Checker.checkGroupByUsage(state, statement, print_statement);
        AP_Checker.checkOrderByRand(state, statement, print_statement);
        AP_Checker.checkPatternMatching(state, statement, print_statement);
        AP_Checker.checkSpaghettiQuery(state, statement, print_statement);
        AP_Checker.checkJoinCount(state, statement, print_statement);
        AP_Checker.checkDistinctCount(state, statement, print_statement);
        AP_Checker.checkImplicitColumns(state, statement, print_statement);
        AP_Checker.checkHaving(state, statement, print_statement);
        AP_Checker.checkNesting(state, statement, print_statement);
        AP_Checker.checkOr(state, statement, print_statement);
        AP_Checker.checkUnion(state, statement, print_statement);
        AP_Checker.checkDistinctJoin(state, statement, print_statement);

        // APPLICATION
        AP_Checker.checkReadablePasswords(state, statement, print_statement);


        // update state.line_number with number of line breaks in the statement that was just checked
        Pattern pattern = Pattern.compile("\n");
        Matcher matcher = pattern.matcher(statement);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        state.setLineNumber(state.getLineNumber() + count);
    }

    private static ArrayList<Integer> convertToLineNumbers(String sqlStatement, ArrayList<Integer> positions, int lineNumber) {
        ArrayList<Integer> lineLocations = new ArrayList<>();
        int statementChar = 0;
        int positionChecker = 0;
        int numLines = lineNumber;

        if (!positions.isEmpty()) {
            while (statementChar < sqlStatement.length()) {
                if (positions.get(positionChecker) == statementChar) {
                    lineLocations.add(numLines);
                    positionChecker++;
                }
                if (sqlStatement.charAt(statementChar) == '\n') {
                    numLines++;
                }
                statementChar++;
            }
        }
        return lineLocations;
    }

    private static String buildLineLocationString(ArrayList<Integer> lineLocations) {
        StringBuilder lineLocationString = new StringBuilder();
        lineLocationString.append(lineLocations.size() > 1 ? " at lines " : " at line ");

        for (int i = 0; i < lineLocations.size(); i++) {
            lineLocationString.append(lineLocations.get(i));
            if (i < lineLocations.size() - 1) {
                lineLocationString.append(", ");
            }
        }
        return lineLocationString.toString();
    }

}