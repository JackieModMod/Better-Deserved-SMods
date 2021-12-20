package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class HardenedSubsystems extends BaseHullMod {

	public static final float SPEAK_BONUS_FLAT = 30f;
	public static final float PEAK_BONUS_PERCENT = 50f;
	public static final float SDEGRADE_REDUCTION_PERCENT = 33f;
	public static final float DEGRADE_REDUCTION_PERCENT = 25f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("hardened_subsystems") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("hardened_subsystems"))) {
			stats.getPeakCRDuration().modifyFlat(id, SPEAK_BONUS_FLAT);
			stats.getCRLossPerSecondPercent().modifyMult(id, 1f - SDEGRADE_REDUCTION_PERCENT / 100f);
			stats.getPeakCRDuration().modifyPercent(id, PEAK_BONUS_PERCENT);
		} else {
			stats.getCRLossPerSecondPercent().modifyMult(id, 1f - DEGRADE_REDUCTION_PERCENT / 100f);
                        stats.getPeakCRDuration().modifyPercent(id, PEAK_BONUS_PERCENT);
		}
		
	}
	

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) PEAK_BONUS_PERCENT + "%";
		if (index == 1) return "" + (int) DEGRADE_REDUCTION_PERCENT + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s seconds peak operating time.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("S-mod Bonus: Combat Readiness Degrade Reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("hardened_subsystems")) {
			tooltip.addPara("S-mod Bonus: %s seconds peak operating time.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("S-mod Bonus: Combat Readiness Degrade Reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("hardened_subsystems")) {
			tooltip.addPara("Built-in Bonus: %s seconds peak operating time.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("Built-in Bonus: Combat Readiness Degrade Reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s seconds peak operating time.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("S-mod Bonus: Combat Readiness Degrade Reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && (ship.getHullSpec().getNoCRLossTime() < 10000 || ship.getHullSpec().getCRLossPerSecond() > 0); 
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship does not suffer from CR degradation";
	}

}
