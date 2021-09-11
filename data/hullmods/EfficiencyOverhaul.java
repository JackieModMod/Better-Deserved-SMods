package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class EfficiencyOverhaul extends BaseLogisticsHullMod {
	public static final float MAINTENANCE_MULT = 0.8f;
	public static final float MAX_CR_BONUS = 10f;
	
	public static final float REPAIR_RATE_BONUS = 50f;
	public static final float CR_RECOVERY_BONUS = 50f;
	public static final float REPAIR_BONUS = 50f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("efficiency_overhaul") || stats.getVariant().getHullSpec().isBuiltInMod("efficiency_overhaul")) {
			stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_BONUS * 0.01f, "Efficiency Overhaul");
		}
        
		stats.getMinCrewMod().modifyMult(id, MAINTENANCE_MULT);
		stats.getSuppliesPerMonth().modifyMult(id, MAINTENANCE_MULT);
		stats.getFuelUseMod().modifyMult(id, MAINTENANCE_MULT);
		
		stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
		stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round((1f - MAINTENANCE_MULT) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round(CR_RECOVERY_BONUS) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s maximum combat readiness.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+10" + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("efficiency_overhaul")) {
			tooltip.addPara("S-mod Bonus: %s maximum combat readiness.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+10" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("efficiency_overhaul")) {
			tooltip.addPara("Built-in Bonus: %s maximum combat readiness.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+10" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s maximum combat readiness.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+10" + "%");
		}
    }

	
}







