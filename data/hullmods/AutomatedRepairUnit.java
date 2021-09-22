package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AutomatedRepairUnit extends BaseHullMod {

	public static final float REPAIR_RATE_BONUS = 50f;
	public static final float CR_RECOVERY_BONUS = 50f;
	public static final float REPAIR_BONUS = 50f;
	public static final float SREPAIR_BONUS = 80f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("autorepair") || stats.getVariant().getHullSpec().isBuiltInMod("autorepair")) {
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - SREPAIR_BONUS * 0.01f);
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - SREPAIR_BONUS * 0.01f);
			stats.getHullCombatRepairRatePercentPerSecond().modifyFlat(id, 1f);
			stats.getMaxCombatHullRepairFraction().modifyFlat(id, 0.4f);
			//stats.getHullBonus().modifyPercent(id, 5f);
		} else {
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
		}
//		stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
//		stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
//		stats.getSuppliesToRecover().modifyPercent(id, LOGISTICS_PENALTY);
//		stats.getSuppliesPerMonth().modifyPercent(id, LOGISTICS_PENALTY);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) REPAIR_BONUS + "%";
//		if (index == 1) return "" + (int) CR_RECOVERY_BONUS + "%";
		//if (index == 2) return "" + (int) LOGISTICS_PENALTY;
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("S-mod Bonus: When below 40 percent hull integrity, recover %s of your hull every second", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "1" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("autorepair")) {
			tooltip.addPara("S-mod Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("S-mod Bonus: When below 40 percent hull integrity, recover %s of your hull every second", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "1" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("autorepair")) {
			tooltip.addPara("Built-in Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("Built-in Bonus: When below 40 percent hull integrity, recover %s of your hull every second", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "1" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("S-mod Bonus: When below 40 percent hull integrity, recover %s of your hull every second", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "1" + "%");
		}
    }

}
