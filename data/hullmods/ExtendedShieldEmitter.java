package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ExtendedShieldEmitter extends BaseHullMod {

	public static final float SHIELD_ARC_BONUS = 60f;
	public static final float SHIELD_EXCESS_ARC = 360f;
	public static final float SHIELD_EXCESS_ARC_DIVISOR = 10f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("extendedshieldemitter") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("extendedshieldemitter"))) {
			stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS*2f);

            float SHIELD_MODIFIED_ARC = stats.getShieldArcBonus().computeEffective(stats.getVariant().getHullSpec().getShieldSpec().getArc());
            if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*stats.getVariant().getHullSpec().getShieldSpec().getUpkeepCost();
                stats.getFluxCapacity().modifyFlat(id, Math.round(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            }
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
			tooltip.addPara("S-mod Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "60");
			tooltip.addPara("S-mod Bonus: Excessive shield arc beyond 360 degree increase ship's flux capacity", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("extendedshieldemitter")) {
			tooltip.addPara("S-mod Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "60");
			float SHIELD_MODIFIED_ARC = ship.getMutableStats().getShieldArcBonus().computeEffective(ship.getVariant().getHullSpec().getShieldSpec().getArc());
            if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*ship.getMutableStats().getVariant().getHullSpec().getShieldSpec().getUpkeepCost();
				tooltip.addPara("S-mod Bonus: Increased flux capacity from excessive shield arc: %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+Misc.	getRoundedValue(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            } else {
				tooltip.addPara("S-mod Bonus: Excessive shield arc beyond 360 degree increase ship's flux capacity", Misc.getPositiveHighlightColor(), 10f);
			}
			
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("extendedshieldemitter")) {
 			tooltip.addPara("Built-in Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "60");
			float SHIELD_MODIFIED_ARC = ship.getMutableStats().getShieldArcBonus().computeEffective(ship.getVariant().getHullSpec().getShieldSpec().getArc());
            if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*ship.getMutableStats().getVariant().getHullSpec().getShieldSpec().getUpkeepCost();
				tooltip.addPara("Built-in Bonus: Increased flux capacity from excessive shield arc: %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+Misc.	getRoundedValue(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            } else {
				tooltip.addPara("Built-in Bonus: Excessive shield arc beyond 360 degree increase ship's flux capacity", Misc.getPositiveHighlightColor(), 10f);
			}
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "60");
			tooltip.addPara("S-mod Bonus: Excessive shield arc beyond 360 degree increase ship's flux capacity", Misc.getGrayColor(), 10f);
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}
}
