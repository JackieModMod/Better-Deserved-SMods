package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class ExpandedDeckCrew extends BaseHullMod {

	public static final float RATE_DECREASE_MODIFIER = 10f;
	public static final float RATE_INCREASE_MODIFIER = 20f;
	public static final float SRATE_DECREASE_MODIFIER = 15f;
	public static final float SRATE_INCREASE_MODIFIER = 25f;
	
	public static final float CREW_PER_DECK = 20f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("expanded_deck_crew") || stats.getVariant().getHullSpec().isBuiltInMod("expanded_deck_crew")) {
			stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id, 1f - SRATE_DECREASE_MODIFIER / 100f);
			stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, SRATE_INCREASE_MODIFIER);
		} else {
			stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id, 1f - RATE_DECREASE_MODIFIER / 100f);
			stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, RATE_INCREASE_MODIFIER);
		}
		
		
		int crew = (int) (stats.getNumFighterBays().getBaseValue() * CREW_PER_DECK);
		stats.getMinCrewMod().modifyFlat(id, crew);
	}
		
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) RATE_DECREASE_MODIFIER + "%";
		if (index == 1) return "" + (int) RATE_INCREASE_MODIFIER + "%";
		if (index == 2) return "" + (int) CREW_PER_DECK + "";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Fighter Replacement Rate Reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Fighter Replacement Rate Recovery increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("expanded_deck_crew")) {
			tooltip.addPara("S-mod Bonus: Fighter Replacement Rate Reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Fighter Replacement Rate Recovery increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("expanded_deck_crew")) {
			tooltip.addPara("Built-in Bonus: Fighter Replacement Rate Reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("Built-in Bonus: Fighter Replacement Rate Recovery increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Fighter Replacement Rate Reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Fighter Replacement Rate Recovery increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
		}
    }
	
	public boolean isApplicableToShip(ShipAPI ship) {
		int baysModified = (int) ship.getMutableStats().getNumFighterBays().getModifiedValue();
		if (baysModified <= 0) return false; // only count removed bays, not added bays for this
		
		int bays = (int) ship.getMutableStats().getNumFighterBays().getBaseValue();
//		if (ship != null && ship.getVariant().getHullSpec().getBuiltInWings().size() >= bays) {
//			return false;
//		}
		return ship != null && bays > 0; 
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship does not have standard fighter bays";
	}
}




