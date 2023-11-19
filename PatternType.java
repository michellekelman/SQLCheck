enum PatternType {
    PATTERN_TYPE_INVALID(0),
    PATTERN_TYPE_PHYSICAL_DATABASE_DESIGN(1),
    PATTERN_TYPE_LOGICAL_DATABASE_DESIGN(2),
    PATTERN_TYPE_QUERY(3),
    PATTERN_TYPE_APPLICATION(4);

    private final int type;

    PatternType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
