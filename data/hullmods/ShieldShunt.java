package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ShieldShunt extends BaseHullMod {

	public static float EMP_RESISTANCE = 50f;
	//public static float VENT_RATE_BONUS = 50f;
	public static float ARMOR_BONUS = 25f;
        public static float HULL_BONUS = 15f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getVentRateMult().modifyPercent(id, VENT_RATE_BONUS);
		stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
		if (stats.getVariant().getSMods().contains("shield_shunt") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("shield_shunt"))) {
                    stats.getEmpDamageTakenMult().modifyMult(id, 1f - EMP_RESISTANCE * 0.01f);
                    stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		}
		
	}
	
	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.setShield(ShieldType.NONE, 0f, 1f, 1f);
	}


	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) EMP_RESISTANCE + "%";
		//if (index == 0) return "" + (int) VENT_RATE_BONUS + "%";
		if (index == 0) return "" + (int) ARMOR_BONUS + "%";
		return null;
	}
        
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the ship's hull integrity by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15%");
			tooltip.addPara("S-mod Bonus: Decreases the amount of damage taken from EMP weapons (such as Ion Cannons) by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50%");
			return;
		} else if (ship.getVariant().getSMods().contains("shield_shunt")) {
			tooltip.addPara("S-mod Bonus: Increases the ship's hull integrity by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15%");
			tooltip.addPara("S-mod Bonus: Decreases the amount of damage taken from EMP weapons (such as Ion Cannons) by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("shield_shunt")) {
			tooltip.addPara("Built-in Bonus: Increases the ship's hull integrity by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15%");
			tooltip.addPara("Built-in Bonus: Decreases the amount of damage taken from EMP weapons (such as Ion Cannons) by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50%");
                } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the ship's hull integrity by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15%");
			tooltip.addPara("S-mod Bonus: Decreases the amount of damage taken from EMP weapons (such as Ion Cannons) by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().getHullSpec().getShieldType() == ShieldType.NONE && 
				!ship.getVariant().hasHullMod("frontshield")) return false;
		if (ship.getVariant().hasHullMod("shield_shunt")) return true;
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}
	
	
}









