package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AcceleratedShieldEmitter extends BaseHullMod {

	public static final float SHIELD_BONUS_TURN = 100f;
	public static final float SHIELD_BONUS_UNFOLD = 100f;
	public static float SHIELD_BONUS = 3f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("advancedshieldemitter") || stats.getVariant().getHullSpec().isBuiltInMod("advancedshieldemitter")) {
			stats.getShieldTurnRateMult().modifyPercent(id, SHIELD_BONUS_TURN*2f);
			stats.getShieldUnfoldRateMult().modifyPercent(id, SHIELD_BONUS_UNFOLD*2f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		} else {
			stats.getShieldTurnRateMult().modifyPercent(id, SHIELD_BONUS_TURN);
			stats.getShieldUnfoldRateMult().modifyPercent(id, SHIELD_BONUS_UNFOLD);	
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_BONUS_TURN + "%";
		if (index == 1) return "" + (int) SHIELD_BONUS_UNFOLD + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the turn rate of the ship's shields and the rate at which the shields are raised by an additional %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("advancedshieldemitter")) {
			tooltip.addPara("S-mod Bonus: Increases the turn rate of the ship's shields and the rate at which the shields are raised by an additional %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		} else if (ship.getHullSpec().isBuiltInMod("advancedshieldemitter")) {
			tooltip.addPara("Built-in Bonus: Increases the turn rate of the ship's shields and the rate at which the shields are raised by an additional %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100" + "%");
			tooltip.addPara("Built-in Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the turn rate of the ship's shields and the rate at which the shields are raised by an additional %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100" + "%");
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
