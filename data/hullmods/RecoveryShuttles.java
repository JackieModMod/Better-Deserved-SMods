package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class RecoveryShuttles extends BaseHullMod {

	public static final float CREW_LOSS_MULT = 0.25f;
        public static final float SCREW_LOSS_MULT = 0.1f;
	public static final float RATE_INCREASE_MODIFIER = 30f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		if (stats.getVariant().getSMods().contains("recovery_shuttles") || Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("recovery_shuttles")) {
                    stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, RATE_INCREASE_MODIFIER);
                    stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).modifyMult(id, SCREW_LOSS_MULT);
		} else {
                    stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).modifyMult(id, CREW_LOSS_MULT);
                }
	}
		
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) ((1f - CREW_LOSS_MULT) * 100f) + "%";
		return null;
	}
        
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
                        tooltip.addPara("S-mod Bonus: Reduction of casualties suffered increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "90" + "%");
                        tooltip.addPara("S-mod Bonus: Increases the rate at which fighter replacement rate recovers by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("recovery_shuttles")) {
                        tooltip.addPara("S-mod Bonus: Reduction of casualties suffered increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "90" + "%");
                        tooltip.addPara("S-mod Bonus: Increases the rate at which fighter replacement rate recovers by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("recovery_shuttles")) {
                        tooltip.addPara("Built-in Bonus: Reduction of casualties suffered increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "90" + "%");
                        tooltip.addPara("Built-in Bonus: Increases the rate at which fighter replacement rate recovers by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30" + "%");
                } else if (!isForModSpec) {
                        tooltip.addPara("S-mod Bonus: Reduction of casualties suffered increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "90" + "%");		
                        tooltip.addPara("S-mod Bonus: Increases the rate at which fighter replacement rate recovers by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30" + "%");
		}
    }
	
	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().hasHullMod(HullMods.AUTOMATED)) return false;
		
		//int bays = (int) ship.getMutableStats().getNumFighterBays().getBaseValue();
		int bays = (int) ship.getMutableStats().getNumFighterBays().getModifiedValue();
//		if (ship != null && ship.getVariant().getHullSpec().getBuiltInWings().size() >= bays) {
//			return false;
//		}
		return ship != null && bays > 0; 
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getVariant().hasHullMod(HullMods.AUTOMATED)) {
			return "Can not be installed on automated ships";
		}
		return "Ship does not have fighter bays";
	}
}




