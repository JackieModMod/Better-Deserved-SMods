package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class FrontShieldEmitter extends BaseHullMod {

	public static final float ARC_BONUS = 100f;
	public static final float UPKEEP_BONUS = 50f;
        public static final float SARC_BONUS = 125f;
	public static final float SMOD_UPKEEP_BONUS = 75f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("frontemitter") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("frontemitter"))) {
			stats.getShieldUpkeepMult().modifyMult(id, 1f - SMOD_UPKEEP_BONUS * 0.01f);
                        stats.getShieldArcBonus().modifyPercent(id, SARC_BONUS);
		} else {
			stats.getShieldUpkeepMult().modifyMult(id, 1f - UPKEEP_BONUS * 0.01f);
                        stats.getShieldArcBonus().modifyPercent(id, ARC_BONUS);
		}
	}
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ShieldAPI shield = ship.getShield();
		if (shield != null) {
			shield.setType(ShieldType.FRONT);
		}
	}

	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) ARC_BONUS + "%";
		if (index == 1) return "" + (int) UPKEEP_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {	
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increase the shield's arc by an additional %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
			tooltip.addPara("Reduction of soft flux generated by shields increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "75" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("frontemitter")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Increase the shield's arc by an additional %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
			tooltip.addPara("Reduction of soft flux generated by shields increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "75" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("frontemitter")) {
			tooltip.addSectionHeading("Built-in bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Increase the shield's arc by an additional %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
			tooltip.addPara("Reduction of soft flux generated by shields increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "75" + "%");
        } else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
            tooltip.addPara("Increase the shield's arc by an additional %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
			tooltip.addPara("Reduction of soft flux generated by shields increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "75" + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().hasHullMod("frontemitter") && ship.getShield() != null) return true;
		return ship != null && ship.getShield() != null && ship.getShield().getType() == ShieldType.OMNI &&
						!ship.getVariant().getHullMods().contains("adaptiveshields");

	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship == null || ship.getShield() == null) {
			return "Ship has no shields";
		}
		
		if (ship.getShield().getType() == ShieldType.FRONT) {
			return "Ship already has front shields";
		}
		
		if (ship.getVariant().getHullMods().contains("adaptiveshields")) {
			return "Incompatible with Shield Conversion - Omni";
		}
		
		return null;
	}
	

}




