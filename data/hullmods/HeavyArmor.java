package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class HeavyArmor extends BaseHullMod {

	public static final float MANEUVER_PENALTY = 10f;
	
	
	private static Map mag = new HashMap();
	private static Map smag = new HashMap();
	static {
		smag.put(HullSize.FRIGATE, 125f);
		smag.put(HullSize.DESTROYER, 250f);
		smag.put(HullSize.CRUISER, 325f);
		smag.put(HullSize.CAPITAL_SHIP, 400f);
		mag.put(HullSize.FRIGATE, 150f);
		mag.put(HullSize.DESTROYER, 300f);
		mag.put(HullSize.CRUISER, 400f);
		mag.put(HullSize.CAPITAL_SHIP, 500f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		if (stats.getVariant().getSMods().contains("heavyarmor")) {
			stats.getArmorBonus().modifyFlat(id, (Float) smag.get(hullSize));
			
			stats.getAcceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
			stats.getDeceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
			stats.getTurnAcceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
			stats.getMaxTurnRate().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
		}  else {
			stats.getArmorBonus().modifyFlat(id, (Float) mag.get(hullSize));
			
			stats.getAcceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
			stats.getDeceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
			stats.getTurnAcceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
			stats.getMaxTurnRate().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
		}
		
//		stats.getDynamic().getMod(Stats.MAX_LOGISTICS_HULLMODS_MOD).modifyFlat(id, 1);
//		//stats.getCargoMod().modifyFlat(id, -70);
//		stats.getTimeMult().modifyMult(id, 4f);
//		//if (stats.getEntity() != null && stats.getEntity() == Global.getCombatEngine().getPlayerShip()) {
//			Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / 4f);
//		//}
		//stats.getNumFighterBays().modifyFlat(id, -1f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 4) return "" + (int) MANEUVER_PENALTY + "%";
		return null;
		//if (index == 0) return "" + ((Float) mag.get(hullSize)).intValue();
		//return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Penalty: Heavy Armor's armor bonus reduced to %s points.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "125/250/325/400");
			return;
		} else if (ship.getVariant().getSMods().contains("heavyarmor")) {
			tooltip.addPara("S-mod Penalty: Heavy Armor's armor bonus reduced to %s points.", 10f, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), "125/250/325/400");
		} else if (ship.getHullSpec().isBuiltInMod("heavyarmor")) {
			//No penalties!
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Penalty: Heavy Armor's armor bonus reduced to %s points.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "125/250/325/400");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		//if (ship.getCargoMod().computeEffective(ship.getHullSpec().getCargo()) < 70) return false;

		return true;
	}
}
