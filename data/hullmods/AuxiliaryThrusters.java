package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AuxiliaryThrusters extends BaseHullMod {

	public static final float MANEUVER_BONUS = 50f;
	public static final float SMANEUVER_BONUS = 66f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("auxiliarythrusters") || stats.getVariant().getHullSpec().isBuiltInMod("auxiliarythrusters")) {
			stats.getAcceleration().modifyPercent(id, SMANEUVER_BONUS * 2f);
			stats.getDeceleration().modifyPercent(id, SMANEUVER_BONUS);
			stats.getTurnAcceleration().modifyPercent(id, SMANEUVER_BONUS * 2f);
			stats.getMaxTurnRate().modifyPercent(id, SMANEUVER_BONUS);
			stats.getEngineHealthBonus().modifyPercent(id, MANEUVER_BONUS*0.5f);
		} else {
			stats.getAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
			stats.getDeceleration().modifyPercent(id, MANEUVER_BONUS);
			stats.getTurnAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
			stats.getMaxTurnRate().modifyPercent(id, MANEUVER_BONUS);
		}

	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) MANEUVER_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Maneuverability's bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("S-mod Bonus: Increases the durability of the ship's engines by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("auxiliarythrusters")) {
			tooltip.addPara("S-mod Bonus: Maneuverability's bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("S-mod Bonus: Increases the durability of the ship's engines by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("auxiliarythrusters")) {
			tooltip.addPara("Built-in Bonus: Maneuverability's bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("Built-in Bonus: Increases the durability of the ship's engines by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Maneuverability's bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("S-mod Bonus: Increases the durability of the ship's engines by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
		}
    }
	
}
