package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ExtendedShieldEmitter extends BaseHullMod {

	public static final float SHIELD_ARC_BONUS = 60f;
	public static float SHIELD_BONUS = 2f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("extendedshieldemitter") || stats.getVariant().getHullSpec().isBuiltInMod("extendedshieldemitter")) {
			stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS*1.75f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		} else {
			stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_ARC_BONUS;
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("extendedshieldemitter")) {
			tooltip.addPara("S-mod Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		} else if (ship.getHullSpec().isBuiltInMod("extendedshieldemitter")) {
 			tooltip.addPara("Built-in Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("Built-in Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}
}
