public class ColorModifier {

    private final ColorCode colorCode;
    private final boolean enableColor;
    private final boolean enableBold;

    public ColorModifier(ColorCode colorCode, boolean enableColor, boolean enableBold) {
        this.colorCode = colorCode;
        this.enableColor = enableColor;
        this.enableBold = enableBold;
    }

    @Override
    public String toString() {
        if (enableBold) {
            if (enableColor) {
                return "\033[1m" + "\033[" + colorCode.getCode() + "m";
            } else {
                return "";
            }
        } else {
            if (enableColor) {
                return "\033[0m" + "\033[" + colorCode.getCode() + "m";
            } else {
                return "";
            }
        }
    }
}