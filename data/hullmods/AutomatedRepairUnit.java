package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AutomatedRepairUnit extends BaseHullMod {

	//public static final float REPAIR_RATE_BONUS = 50f;
	//public static final float CR_RECOVERY_BONUS = 50f;
	public static final float REPAIR_BONUS = 50f;
	public static final float SREPAIR_BONUS = 80f;
        public static final float HULL_DAMAGE_REDUCTION = 10f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("autorepair") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("autorepair"))) {
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - SREPAIR_BONUS * 0.01f);
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - SREPAIR_BONUS * 0.01f);
			stats.getHullDamageTakenMult().modifyMult(id, 1f - HULL_DAMAGE_REDUCTION / 100f);
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
			tooltip.addPara("S-mod Bonus: %s hull damage taken", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-10" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("autorepair")) {
			tooltip.addPara("S-mod Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("S-mod Bonus: %s hull damage taken", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-10" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("autorepair")) {
			tooltip.addPara("Built-in Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("Built-in Bonus: %s hull damage taken", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-10" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Time reduction to repair weapons and engines increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "80" + "%");
			tooltip.addPara("S-mod Bonus: %s hull damage taken", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-10" + "%");
		}
    }

}
