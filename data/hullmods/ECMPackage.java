package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ECMPackage extends BaseHullMod {

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 1f);
		mag.put(HullSize.DESTROYER, 2f);
		mag.put(HullSize.CRUISER, 3f);
		mag.put(HullSize.CAPITAL_SHIP, 4f);
	}
	
	public static final float EW_PENALTY_MULT = 0f;
        public static float AIM_BONUS = 0.5f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("ecm") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("ecm"))) {
			stats.getAutofireAimAccuracy().modifyFlat(id, AIM_BONUS);
			stats.getDynamic().getStat(Stats.ELECTRONIC_WARFARE_PENALTY_MULT).modifyMult(id, EW_PENALTY_MULT);
			stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MOD).modifyMult(id, EW_PENALTY_MULT); ////Remove if this conflicts with any ECM changes!
		}
		stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(id, (Float) mag.get(hullSize));
	}
        
	public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
		if (ship.getVariant().getSMods().contains("ecm") || (Global.getSettings().getBoolean("BuiltInSMod") && ship.getVariant().getHullSpec().isBuiltInMod("ecm"))) {
			MutableShipStatsAPI stats = fighter.getMutableStats();
			stats.getAutofireAimAccuracy().modifyFlat(id, AIM_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue() + "%";
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Ship and Fighters gain %s target leading accuracy", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+50" + "%");
			tooltip.addPara("S-mod Bonus: Immune to weapon range reduction from superior enemy Electronic Warfare.", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("ecm")) {
			tooltip.addPara("S-mod Bonus: Ship and Fighters gain %s target leading accuracy", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+50" + "%");
			tooltip.addPara("S-mod Bonus: Immune to weapon range reduction from superior enemy Electronic Warfare.", Misc.getPositiveHighlightColor(), 10f);
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("ecm")) {
                        tooltip.addPara("Built-in Bonus: Ship and Fighters gain %s target leading accuracy", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+50" + "%");
			tooltip.addPara("Built-in Bonus: Immune to weapon range reduction from superior enemy Electronic Warfare.", Misc.getPositiveHighlightColor(), 10f);
                } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Ship and Fighters gain %s target leading accuracy", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+50" + "%");
			tooltip.addPara("S-mod Bonus: Immune to weapon range reduction from superior enemy Electronic Warfare.", Misc.getGrayColor(),10f);
		}
    }
}




