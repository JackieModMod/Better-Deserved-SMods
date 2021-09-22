package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class StabilizedShieldEmitter extends BaseHullMod {

	public static final float SHIELD_UPKEEP_BONUS = 50f;
	public static float SHIELD_BONUS = 3f;
	public static final float FLUX_SHUNT_DISSIPATION = 5f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {		
		if (stats.getVariant().getSMods().contains("stabilizedshieldemitter") || stats.getVariant().getHullSpec().isBuiltInMod("stabilizedshieldemitter")) {
			stats.getHardFluxDissipationFraction().modifyFlat(id, FLUX_SHUNT_DISSIPATION / 100f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		}
		stats.getShieldUpkeepMult().modifyMult(id, 1f - SHIELD_UPKEEP_BONUS * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_UPKEEP_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s hard flux dissipation while shields are active.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("stabilizedshieldemitter")) {
			tooltip.addPara("S-mod Bonus: %s hard flux dissipation while shields are active.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		} else if (ship.getHullSpec().isBuiltInMod("stabilizedshieldemitter")) {
			tooltip.addPara("Built-in Bonus: %s hard flux dissipation while shields are active.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("Built-in Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s hard flux dissipation while shields are active.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5" + "%");
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
