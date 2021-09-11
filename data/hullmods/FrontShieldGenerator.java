package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class FrontShieldGenerator extends BaseHullMod {
	
	public static final float SHIELD_ARC = 90f;
	public static final float SPEED_MULT = 0.8f;
	public static float SHIELD_BONUS = 2f;
	
	
//	private static Map mag = new HashMap();
//	static {
//		mag.put(HullSize.FRIGATE, 40f);
//		mag.put(HullSize.DESTROYER, 30f);
//		mag.put(HullSize.CRUISER, 25f);
//		mag.put(HullSize.CAPITAL_SHIP, 15f);
//	}
	
	//public void applyEffectsAfterShipCreationFirstPass(ShipAPI ship, String id) {
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ShieldAPI shield = ship.getShield();
		if (shield == null) {
			ship.setShield(ShieldType.FRONT, 0.5f, 1.2f, SHIELD_ARC);
		}
	}
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getMaxSpeed().modifyFlat(id, (Float) mag.get(hullSize) * -1f);
		stats.getMaxSpeed().modifyMult(id, SPEED_MULT);
		if (stats.getVariant().getSMods().contains("frontshield") || stats.getVariant().getHullSpec().isBuiltInMod("frontshield")) {
			stats.getMaxSpeed().modifyMult(id, SPEED_MULT*1.125f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		} else {
			stats.getMaxSpeed().modifyMult(id, SPEED_MULT);
		}
	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) {
			return "" + (int) SHIELD_ARC;
		}
		if (index == 1) {
			return "" + (int) Math.round((1f - SPEED_MULT) * 100f) + "%";
		}
		
//		if (index == 1) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
//		if (index == 2) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
//		if (index == 3) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
//		if (index == 4) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Top speed reduction reduced to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("frontshield")) {
			tooltip.addPara("S-mod Bonus: Top speed reduction reduced to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "10" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		} else if (ship.getHullSpec().isBuiltInMod("frontshield")) {
			tooltip.addPara("Built-in Bonus: Top speed reduction reduced to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "10" + "%");
			tooltip.addPara("Built-in Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Top speed reduction reduced to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		//return ship != null && ship.getShield() == null && ship.getPhaseCloak() == null;
		return ship != null && ship.getHullSpec().getDefenseType() == ShieldType.NONE;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship already has shields";
	}
}
