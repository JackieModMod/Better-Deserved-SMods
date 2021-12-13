package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class OperationsCenter extends BaseHullMod {

	public static final float RECOVERY_BONUS = 250f;
	public static final String MOD_ID = "operations_center_mod";
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("operations_center") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("operations_center"))) {
			stats.getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).modifyFlat(id, 2f);
			stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(id, 2f);
		}
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) RECOVERY_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases nav rating of your fleet by %s when deployed.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "2%");
			tooltip.addPara("S-mod Bonus: Grants %s ECM rating when deployed.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "2%");
			return;
		} else if (ship.getVariant().getSMods().contains("operations_center")) {
			tooltip.addPara("S-mod Bonus: Increases nav rating of your fleet by %s when deployed.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "2%");
			tooltip.addPara("S-mod Bonus: Grants %s ECM rating when deployed.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "2%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("operations_center")) {
			tooltip.addPara("Built-in Bonus: Increases nav rating of your fleet by %s when deployed.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "2%");
			tooltip.addPara("Built-in Bonus: Grants %s ECM rating when deployed.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "2%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases nav rating of your fleet by %s when deployed.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "2%");
			tooltip.addPara("S-mod Bonus: Grants %s ECM rating when deployed.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "2%");
		}
    }
	
	
	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		CombatEngineAPI engine = Global.getCombatEngine();
		if (engine == null) return;
		
		CombatFleetManagerAPI manager = engine.getFleetManager(ship.getOriginalOwner());
		if (manager == null) return;
		
		DeployedFleetMemberAPI member = manager.getDeployedFleetMember(ship);
		if (member == null) return; // happens in refit screen etc
		
		boolean apply = ship == engine.getPlayerShip();
		PersonAPI commander = null;
		if (member.getMember() != null) {
			commander = member.getMember().getFleetCommander();
			if (member.getMember().getFleetCommanderForStats() != null) {
				commander = member.getMember().getFleetCommanderForStats();
			}
		}
		apply |= commander != null && ship.getCaptain() == commander;
		
		if (apply) {
			ship.getMutableStats().getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).modifyFlat(MOD_ID, RECOVERY_BONUS * 0.01f);
		} else {
			ship.getMutableStats().getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).unmodify(MOD_ID);
		}
	}

}








