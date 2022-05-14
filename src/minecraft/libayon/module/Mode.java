package libayon.module;

public enum Mode {

    VANILLA(true), NCP(true), ACENTRAMC(true), SPARTAN(true), VERUS(true), AAC(true), HYPIXEL(false), VULCAN(true), LUNAR(true), NONE(true), LUCKYNETWORK(true), RGB(false), RANDOMLIGHT(false), BATTLEASYAKITPVP(true), BATTLEASYA(true), BATTLEASYAMOTION(true), BATTLEASYABEST(true), BATTLEASYASAFE(true), PACKET(true), TEST(true);

    private final boolean capital;

    Mode(boolean capital) {
        this.capital = capital;
    }

    public boolean isCapital() {
        return capital;
    }
}