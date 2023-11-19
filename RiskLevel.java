enum RiskLevel {
    RISK_LEVEL_INVALID(10),
    RISK_LEVEL_HIGH(4),
    RISK_LEVEL_MEDIUM(3),
    RISK_LEVEL_LOW(2),
    RISK_LEVEL_NONE(1),
    RISK_LEVEL_ALL(0);

    private final int level;

    RiskLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
