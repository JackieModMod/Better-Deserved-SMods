package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class InsulatedEngines extends BaseLogisticsHullMod {

	public static final float PROFILE_MULT = 0.5f;
	public static final float HEALTH_BONUS = 100f;
	public static final float HULL_BONUS = 10f;
	public static final float REPAIR_BONUS = 33f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("insulatedengine") || stats.getVariant().getHullSpec().isBuiltInMod("insulatedengine")) {
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
		}
		stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		stats.getSensorProfile().modifyMult(id, PROFILE_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) HULL_BONUS + "%";
		if (index == 2) return "" + (int) ((1f - PROFILE_MULT) * 100f) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Reduces the time required to repair disabled engines by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("insulatedengine")) {
			tooltip.addPara("S-mod Bonus: Reduces the time required to repair disabled engines by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("insulatedengine")) {
			tooltip.addPara("Built-in Bonus: Reduces the time required to repair disabled engines by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Reduces the time required to repair disabled engines by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
		}
    }


}
