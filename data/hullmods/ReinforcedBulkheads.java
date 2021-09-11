package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class ReinforcedBulkheads extends BaseHullMod {
	
	public static final float HULL_BONUS = 40f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("reinforcedhull") || stats.getVariant().getHullSpec().isBuiltInMod("reinforcedhull")) {
			stats.getHullBonus().modifyPercent(id, HULL_BONUS+5f);
		} else {
			stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		}
		stats.getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).modifyFlat(id, 1000f);
		stats.getBreakProb().modifyMult(id, 0f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HULL_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Hull Integrity Bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "45" + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("reinforcedhull")) {
			tooltip.addPara("S-mod Bonus: Hull Integrity Bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "45" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("reinforcedhull")) {
			tooltip.addPara("Built-in Bonus: Hull Integrity Bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "45" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Hull Integrity Bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "45" + "%");
		}
    }
}



