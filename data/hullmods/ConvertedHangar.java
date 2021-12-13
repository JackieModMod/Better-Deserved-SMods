package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.DefectiveManufactory;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ConvertedHangar extends BaseHullMod {

	public static final int CREW_REQ = 20;
	//public static final int CARGO_REQ = 80;
	public static final int ALL_FIGHTER_COST_PERCENT = 50;
	public static final int BOMBER_COST_PERCENT = 100;
	public static float SPEED_REDUCTION = 0.16666666f;
	public static float DAMAGE_INCREASE = 0.2f;
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 0f);
		mag.put(HullSize.DESTROYER, 75f);
		mag.put(HullSize.CRUISER, 50f);
		mag.put(HullSize.CAPITAL_SHIP, 25f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getFighterRefitTimeMult().modifyPercent(id, ((Float) mag.get(hullSize)));
		stats.getNumFighterBays().modifyFlat(id, 1f);

		stats.getMinCrewMod().modifyFlat(id, CREW_REQ);
		//stats.getDynamic().getMod(Stats.ALL_FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.BOMBER_COST_MOD).modifyPercent(id, BOMBER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.INTERCEPTOR_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.SUPPORT_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		//stats.getCargoMod().modifyFlat(id, -CARGO_REQ);
	}
	
	public boolean isApplicableToShip(ShipAPI ship) {
		//if (ship.getCargoMod().computeEffective(ship.getHullSpec().getCargo()) < CARGO_REQ) return false;
		
		return ship != null && !ship.isFrigate() && ship.getHullSpec().getFighterBays() <= 0 &&
								//ship.getNumFighterBays() <= 0 &&
								!ship.getVariant().hasHullMod(HullMods.CONVERTED_BAY) &&
								!ship.getVariant().hasHullMod(HullMods.PHASE_FIELD);
								//ship.getHullSpec().getShieldType() != ShieldType.PHASE;		
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.isFrigate()) return "Can not be installed on a frigate";
		if (ship != null && ship.getHullSpec().getFighterBays() > 0) return "Ship has standard fighter bays";
		if (ship != null && ship.getVariant().hasHullMod(HullMods.CONVERTED_BAY)) return "Ship has fighter bays";
		//if (ship != null && ship.getNumFighterBays() > 0) return "Ship has fighter bays";
		return "Can not be installed on a phase ship";
	}
	
	public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
		if (ship.getVariant().getSMods().contains("converted_hangar") || (Global.getSettings().getBoolean("BuiltInSMod") && ship.getVariant().getHullSpec().isBuiltInMod("converted_hangar"))) {
			MutableShipStatsAPI stats = fighter.getMutableStats();
			stats.getMaxSpeed().modifyMult(id, 1f - SPEED_REDUCTION);
			stats.getArmorDamageTakenMult().modifyPercent(id, DAMAGE_INCREASE * 100f);
			stats.getShieldDamageTakenMult().modifyPercent(id, DAMAGE_INCREASE * 100f);
			stats.getHullDamageTakenMult().modifyPercent(id, DAMAGE_INCREASE * 100f);
			fighter.setHeavyDHullOverlay();
		} else {
			new DefectiveManufactory().applyEffectsToFighterSpawnedByShip(fighter, ship, id);
		}
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 2) return "" + CREW_REQ;
		if (index == 3) return "" + BOMBER_COST_PERCENT + "%";
		if (index == 4) return "" + ALL_FIGHTER_COST_PERCENT + "%";
		return new DefectiveManufactory().getDescriptionParam(index, hullSize, ship);
//		if (index == 0) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
//		if (index == 1) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
//		if (index == 2) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
//		if (index == 3) return "" + CREW_REQ;
//		return null;
		//if (index == 0) return "" + ((Float) mag.get(hullSize)).intValue();
		//return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Fighter speed reduction reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("S-mod Bonus: Fighter increased damage taken reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("converted_hangar")) {
			tooltip.addPara("S-mod Bonus: Fighter speed reduction reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("S-mod Bonus: Fighter increased damage taken reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "20" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("converted_hangar")) {
			tooltip.addPara("Built-in Bonus: Fighter speed reduction reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("Built-in Bonus: Fighter increased damage taken reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "20" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Fighter speed reduction reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "17" + "%");
			tooltip.addPara("S-mod Bonus: Fighter increased damage taken reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
		}
    }
	
	@Override
	public boolean affectsOPCosts() {
		return true;
	}
}



