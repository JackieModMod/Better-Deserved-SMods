package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class OmniShieldEmitter extends BaseHullMod {

	public static final float ARC_PENALTY = 33.3333f;
	public static final float SARC_PENALTY = 16.6666f;
	public static float SHIELD_BONUS = 2f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		if (stats.getVariant().getSMods().contains("adaptiveshields") || stats.getVariant().getHullSpec().isBuiltInMod("adaptiveshields")) {
			stats.getShieldArcBonus().modifyMult(id, 1f - SARC_PENALTY * 0.01f);
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		} else {
			stats.getShieldArcBonus().modifyMult(id, 1f - ARC_PENALTY * 0.01f);
		}
	}
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ShieldAPI shield = ship.getShield();
		if (shield != null) {
			shield.setType(ShieldType.OMNI);
		}
	}

	
	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) ARC_PENALTY;
		if (index == 0) return "" + (int) ARC_PENALTY + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Reduction of shield arc decreased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("adaptiveshields")) {
			tooltip.addPara("S-mod Bonus: Reduction of shield arc decreased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		} else if (ship.getHullSpec().isBuiltInMod("adaptiveshields")) {
			tooltip.addPara("Built-in Bonus: Reduction of shield arc decreased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("Built-in Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Reduction of shield arc decreased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("S-mod Bonus: Reduces the amount of damage taken by shields by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SHIELD_BONUS + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().getHullSpec().getShieldType() == ShieldType.OMNI && 
				!ship.getVariant().hasHullMod("frontemitter")) return false;
		if (ship.getVariant().getHullMods().contains("frontemitter")) return false;
		if (ship.getVariant().hasHullMod("adaptiveshields") && ship.getShield() != null) return true;
		return ship != null && ship.getShield() != null && ship.getShield().getType() == ShieldType.FRONT;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship == null || ship.getShield() == null) return "Ship has no shields";
		
		if (ship.getShield().getType() == ShieldType.OMNI) { 
			return "Ship already has omni-directional shields";
		}
		
		if (ship.getVariant().getHullMods().contains("frontemitter")) {
			return "Incompatible with Shield Conversion - Front";
		}
		
		return null;
	}
	
}
