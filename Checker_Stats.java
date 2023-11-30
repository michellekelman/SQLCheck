public class Checker_Stats {
    private int errorLevelCounter;
    private int warnLevelCounter;
    private int infoLevelCounter;
    private int debugLevelCounter;
    private int traceLevelCounter;
    private int allLevelCounter;

    // Constructors
    public Checker_Stats() {
        this.errorLevelCounter = 0;
        this.warnLevelCounter = 0;
        this.infoLevelCounter = 0;
        this.debugLevelCounter = 0;
        this.traceLevelCounter = 0;
        this.allLevelCounter = 0;
    }

    public Checker_Stats(int errorLevelCounter, int warnLevelCounter, int infoLevelCounter,
                        int debugLevelCounter, int traceLevelCounter, int allLevelCounter) {
        this.errorLevelCounter = errorLevelCounter;
        this.warnLevelCounter = warnLevelCounter;
        this.infoLevelCounter = infoLevelCounter;
        this.debugLevelCounter = debugLevelCounter;
        this.traceLevelCounter = traceLevelCounter;
        this.allLevelCounter = allLevelCounter;
    }

    // Getters and Setters
    public int getErrorLevelCounter() {
        return errorLevelCounter;
    }

    public void setErrorLevelCounter(int errorLevelCounter) {
        this.errorLevelCounter = errorLevelCounter;
    }

    public int getWarnLevelCounter() {
        return warnLevelCounter;
    }

    public void setWarnLevelCounter(int warnLevelCounter) {
        this.warnLevelCounter = warnLevelCounter;
    }

    public int getInfoLevelCounter() {
        return infoLevelCounter;
    }

    public void setInfoLevelCounter(int infoLevelCounter) {
        this.infoLevelCounter = infoLevelCounter;
    }

    public int getDebugLevelCounter() {
        return debugLevelCounter;
    }

    public void setDebugLevelCounter(int debugLevelCounter) {
        this.debugLevelCounter = debugLevelCounter;
    }

    public int getTraceLevelCounter() {
        return traceLevelCounter;
    }

    public void setTraceLevelCounter(int traceLevelCounter) {
        this.traceLevelCounter = traceLevelCounter;
    }

    public int getAllLevelCounter() {
        return allLevelCounter;
    }

    public void setAllLevelCounter(int allLevelCounter) {
        this.allLevelCounter = allLevelCounter;
    }
}