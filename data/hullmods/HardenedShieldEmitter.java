package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class HardenedShieldEmitter extends BaseHullMod {

	public static float PIERCE_MULT = 0.5f;
	public static float SHIELD_BONUS = 15f;
        public static float SSHIELD_BONUS = 22f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("hardenedshieldemitter") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("hardenedshieldemitter"))) {
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SSHIELD_BONUS * 0.01f);
		} else {
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		}
		stats.getDynamic().getStat(Stats.SHIELD_PIERCED_MULT).modifyMult(id, PIERCE_MULT);		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_BONUS + "%";
		return null;
	}
        
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Shield damage reduction increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SSHIELD_BONUS + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("hardenedshieldemitter")) {
			tooltip.addPara("S-mod Bonus: Shield damage reduction increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SSHIELD_BONUS + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("hardenedshieldemitter")) {
			tooltip.addPara("Built-in Bonus: Shield damage reduction increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), (int) SSHIELD_BONUS + "%");
                } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Shield damage reduction increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), (int) SSHIELD_BONUS + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}

}
