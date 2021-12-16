package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AdvancedTurretGyros extends BaseHullMod {

	public static final float TURRET_SPEED_BONUS = 75f;
	public static final float STURRET_SPEED_BONUS = 200f;
	public static final float RECOIL_BONUS = 25f;
	public static final float MISSILE_SPEC_ROF_BONUS = 33f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("turretgyros") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("turretgyros"))) {
			stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
			stats.getWeaponTurnRateBonus().modifyPercent(id, STURRET_SPEED_BONUS);
			stats.getBeamWeaponTurnRateBonus().modifyPercent(id, STURRET_SPEED_BONUS);
			stats.getMissileRoFMult().modifyPercent(id, MISSILE_SPEC_ROF_BONUS);
		} else {
			stats.getWeaponTurnRateBonus().modifyPercent(id, TURRET_SPEED_BONUS);
			stats.getBeamWeaponTurnRateBonus().modifyPercent(id, TURRET_SPEED_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) TURRET_SPEED_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Turret turn rate bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "200" + "%");
			tooltip.addPara("S-mod Bonus: %s weapon recoil.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("S-mod Bonus: %s rate of fire for missile weapons", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("turretgyros")) {
			tooltip.addPara("S-mod Bonus: Turret turn rate bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "200" + "%");
			tooltip.addPara("S-mod Bonus: %s weapon recoil.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("S-mod Bonus: %s rate of fire for missile weapons", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+25" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("turretgyros")) {
			tooltip.addPara("Built-in Bonus: Turret turn rate bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "200" + "%");
			tooltip.addPara("Built-in Bonus: %s weapon recoil.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("Built-in Bonus: %s rate of fire for missile weapons", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+25" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Turret turn rate bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "200" + "%");
			tooltip.addPara("S-mod Bonus: %s weapon recoil.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("S-mod Bonus: %s rate of fire for missile weapons", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
		}
    }


}
