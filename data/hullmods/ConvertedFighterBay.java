package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ConvertedFighterBay extends BaseLogisticsHullMod {

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 100f);
		mag.put(HullSize.DESTROYER, 200f);
		mag.put(HullSize.CRUISER, 250f);
		mag.put(HullSize.CAPITAL_SHIP, 300f);
	}
	public static final int CREW_REQ_PER_BAY = 20;
	public static final int MAX_CREW = 80;
	public static final int CARGO_PER_BAY = 50;
	public static final float FLUX_PER_BAY = 0.02f;
	public static final int logisticMax = Global.getSettings().getInt("maxLogisticsHullmods");
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		int bays = (int) Math.round(stats.getNumFighterBays().getBaseValue());
		stats.getNumFighterBays().modifyFlat(id, -bays);

		int crewReduction = CREW_REQ_PER_BAY * bays;
		if (crewReduction > MAX_CREW) crewReduction = MAX_CREW;
		int cargo = CARGO_PER_BAY * bays;
		float flux = 1.05f + FLUX_PER_BAY * bays;
		
		stats.getMinCrewMod().modifyPercent(id, -crewReduction);
		stats.getCargoMod().modifyFlat(id, cargo);
		
		if (stats.getVariant().getSMods().contains("converted_fighterbay") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("converted_fighterbay"))) {
			stats.getFluxCapacity().modifyMult(id, flux);
			stats.getFluxDissipation().modifyMult(id, flux);
			stats.getArmorBonus().modifyFlat(id, (Float) mag.get(hullSize));
		}
	}
	
	public boolean isApplicableToShip(ShipAPI ship) {
		int builtIn = ship.getHullSpec().getBuiltInWings().size();
		int bays = (int) Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        int logistic = 0;
        for (String s : ship.getVariant().getNonBuiltInHullmods()) {
            if (Misc.getMod(s).hasUITag("Logistics") && !Misc.getMod(s).getId().equals("converted_fighterbay")) {
                logistic += 1;
            }
        }
		if (builtIn <= 0 || bays > builtIn) return false;
		if (logistic >= logisticMax) return false;
		return true;
	}

    public String getUnapplicableReason(ShipAPI ship) {
		int builtIn = ship.getHullSpec().getBuiltInWings().size();
		int bays = (int) Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        int logistic = 0;
        for (String s : ship.getVariant().getNonBuiltInHullmods()) {
            if (Misc.getMod(s).hasUITag("Logistics")) {
                logistic += 1;
            }
        }
		if (builtIn <= 0 || bays > builtIn) return "Requires built-in fighter wings only";
		if (logistic >= logisticMax) return "Maximum of " + String.valueOf(logisticMax) + " non-built-in \"Logistics\" hullmods per hull";
		return "";
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + CARGO_PER_BAY;
		if (index == 1) return "" + CREW_REQ_PER_BAY + "%";
		if (index == 2) return "" + MAX_CREW + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increase the ship's armor by %s/%s/%s/%s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100", "200", "250", "300");
			tooltip.addPara("S-mod Bonus: Increases the ship's flux stats by %s and an additional %s per converted fighter bay", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5%", "2%");
			return;
		} else if (ship.getVariant().getSMods().contains("converted_fighterbay")) {
			tooltip.addPara("S-mod Bonus: Increase the ship's armor by %s/%s/%s/%s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100", "200", "250", "300");
			tooltip.addPara("S-mod Bonus: Increases the ship's flux stats by %s and an additional %s per converted fighter bay", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5%", "2%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("converted_fighterbay")) {
			tooltip.addPara("Built-in Bonus: Increase the ship's armor by %s/%s/%s/%s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100", "200", "250", "300");
			tooltip.addPara("Built-in Bonus: Increases the ship's flux stats by %s and an additional %s per converted fighter bay", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5%", "2%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increase the ship's armor by %s/%s/%s/%s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100", "200", "250", "300");
			tooltip.addPara("S-mod Bonus: Increases the ship's flux stats by %s and an additional %s per converted fighter bay", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5%", "2%");
		}
    }	
}