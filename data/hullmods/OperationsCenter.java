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
	public static float SPEED_BONUS = 15f;
	public static float AIM_BONUS = 0.33f;
	
	public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
		if (ship.getVariant().getSMods().contains("operations_center") || (Global.getSettings().getBoolean("BuiltInSMod") && ship.getVariant().getHullSpec().isBuiltInMod("operations_center"))) {
			MutableShipStatsAPI stats = fighter.getMutableStats();
			stats.getMaxSpeed().modifyPercent(id, SPEED_BONUS);
			stats.getAcceleration().modifyPercent(id, SPEED_BONUS * 2f);
			stats.getDeceleration().modifyPercent(id, SPEED_BONUS * 2f);
			stats.getAutofireAimAccuracy().modifyFlat(id, AIM_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) RECOVERY_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Fighters gain %s top speed.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+15%");
			tooltip.addPara("S-mod Bonus: Fighters gain %s target leading accuracy.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+33%");
			return;
		} else if (ship.getVariant().getSMods().contains("operations_center")) {
			tooltip.addPara("S-mod Bonus: Fighters gain %s top speed.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+15%");
			tooltip.addPara("S-mod Bonus: Fighters gain %s target leading accuracy.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+33%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("operations_center")) {
			tooltip.addPara("Built-in Bonus: Fighters gain %s top speed.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+15%");
			tooltip.addPara("Built-in Bonus: Fighters gain %s target leading accuracy.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+33%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Fighters gain %s top speed.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+15%");
			tooltip.addPara("S-mod Bonus: Fighters gain %s target leading accuracy.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+33%");
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








