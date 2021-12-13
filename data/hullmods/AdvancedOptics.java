package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AdvancedOptics extends BaseHullMod {

	public static final float BEAM_RANGE_BONUS = 200f;
	//public static final float BEAM_DAMAGE_PENALTY = 25f;
	public static final float BEAM_DAMAGE_BONUS = 5f;
	public static final float BEAM_TURN_PENALTY = 30f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("advancedoptics") || Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("advancedoptics")) {
			stats.getBeamWeaponDamageMult().modifyPercent(id, BEAM_DAMAGE_BONUS);
		}
		stats.getBeamWeaponRangeBonus().modifyFlat(id, BEAM_RANGE_BONUS);
		//stats.getBeamWeaponDamageMult().modifyPercent(id, -BEAM_DAMAGE_PENALTY);
		stats.getBeamWeaponTurnRateBonus().modifyMult(id, 1f - BEAM_TURN_PENALTY * 0.01f);
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s Beam Damage.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+5" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("advancedoptics")) {
			tooltip.addPara("S-mod Bonus: %s Beam Damage.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("advancedoptics")) {
            tooltip.addPara("Built-in Bonus: %s Beam Damage.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s Beam Damage.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+5" + "%");
		}
    }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) BEAM_RANGE_BONUS;
		//if (index == 1) return "" + (int) BEAM_DAMAGE_PENALTY;
		if (index == 1) return "" + (int) BEAM_TURN_PENALTY + "%";
		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains(HullMods.HIGH_SCATTER_AMP);
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains(HullMods.HIGH_SCATTER_AMP)) {
			return "Incompatible with High Scatter Amplifier";
		}
		return null;
	}
}
