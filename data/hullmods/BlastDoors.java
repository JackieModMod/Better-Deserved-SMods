package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class BlastDoors extends BaseHullMod {

	public static final float HULL_BONUS = 20f;
	public static final float CASUALTY_REDUCTION = 50f;
        public static final float HE_REDUCTION = 25f;
	//public static final float HULL_DAMAGE_CR_MULT = 0.25f;
	

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
//		stats.getCargoMod().modifyPercent(id, -50f);
//		stats.getCargoMod().modifyPercent(id + "sfsdfd", +25f);
//		stats.getMaxCrewMod().modifyPercent(id, 100);
		if (stats.getVariant().getSMods().contains("blast_doors") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("blast_doors"))) {
                    stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - HE_REDUCTION / 100f);
		}
                stats.getHullBonus().modifyPercent(id, HULL_BONUS);
                stats.getCrewLossMult().modifyMult(id, 1f - CASUALTY_REDUCTION * 0.01f);
		//stats.getDynamic().getStat(Stats.HULL_DAMAGE_CR_LOSS).modifyMult(id, HULL_DAMAGE_CR_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HULL_BONUS + "%";
		if (index == 1) return "" + (int) CASUALTY_REDUCTION + "%";
		//if (index == 2) return "" + (int) ((1f - HULL_DAMAGE_CR_MULT) * 100f);
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s high explosive damage taken by hull and armor", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-25%");
			return;
		} else if (ship.getVariant().getSMods().contains("blast_doors")) {
			tooltip.addPara("S-mod Bonus: %s high explosive damage taken by hull and armor", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-25%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("blast_doors")) {
			tooltip.addPara("Built-in Bonus: %s high explosive damage taken by hull and armor", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-25%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s high explosive damage taken by hull and armor", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-25%");
		}
    }
}
