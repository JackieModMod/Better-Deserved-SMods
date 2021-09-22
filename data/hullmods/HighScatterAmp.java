package data.hullmods;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class HighScatterAmp extends BaseHullMod {

	public static float RANGE_PENALTY_PERCENT = 50f;
	public static float NONPD_RANGE_PENALTY_PERCENT = 30f;
	public static float PD_RANGE_BONUS_PERCENT = 1.142857f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		if (stats.getVariant().getSMods().contains("high_scatter_amp") || stats.getVariant().getHullSpec().isBuiltInMod("high_scatter_amp")) {
			stats.getBeamWeaponRangeBonus().modifyMult(id, 1f - NONPD_RANGE_PENALTY_PERCENT * 0.01f);
			stats.getBeamPDWeaponRangeBonus().modifyMult(id, PD_RANGE_BONUS_PERCENT); //To counteract the top penalties.
		} else {
			stats.getBeamWeaponRangeBonus().modifyMult(id, 1f - RANGE_PENALTY_PERCENT * 0.01f);
		}
		
		
		// test code for WeaponOPCostModifier, FighterOPCostModifier
//		stats.addListener(new WeaponOPCostModifier() {
//			public int getWeaponOPCost(MutableShipStatsAPI stats, WeaponSpecAPI weapon, int currCost) {
//				if (weapon.getWeaponId().equals("amblaster")) {
//					return 1;
//				}
//				return currCost;
//			}
//		});
//		stats.addListener(new FighterOPCostModifier() {
//			public int getFighterOPCost(MutableShipStatsAPI stats, FighterWingSpecAPI fighter, int currCost) {
//				if (fighter.getId().equals("talon_wing")) {
//					return 20;
//				}
//				return currCost;
//			}
//		});
	}
	
//	@Override
//	public boolean affectsOPCosts() {
//		return true;
//	}



	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.addListener(new HighScatterAmpDamageDealtMod(ship));
		
		/* test code for WeaponRangeModifier
		ship.addListener(new WeaponRangeModifier() {
			public float getWeaponRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
				return 0;
			}
			public float getWeaponRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
				return 1f;
			}
			public float getWeaponRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
				if (weapon.getId().equals("amblaster")) {
					return 500;
				}
				return 0f;
			}
		});
		*/
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)RANGE_PENALTY_PERCENT + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Range reduction of non-PD beam weapons reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30" + "%");
			tooltip.addPara("S-mod Bonus: Range reduction of point-defense beam weapons reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("high_scatter_amp")) {
			tooltip.addPara("S-mod Bonus: Range reduction of non-PD beam weapons reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30" + "%");
			tooltip.addPara("S-mod Bonus: Range reduction of point-defense beam weapons reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "20" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("high_scatter_amp")) {
			tooltip.addPara("Built-in Bonus: Range reduction of non-PD beam weapons reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30" + "%");
			tooltip.addPara("Built-in Bonus: Range reduction of point-defense beam weapons reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "20" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Range reduction of non-PD beam weapons reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30" + "%");
			tooltip.addPara("S-mod Bonus: Range reduction of point-defense beam weapons reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
		}
    }
	
	public static class HighScatterAmpDamageDealtMod implements DamageDealtModifier {
		protected ShipAPI ship;
		public HighScatterAmpDamageDealtMod(ShipAPI ship) {
			this.ship = ship;
		}
		
		public String modifyDamageDealt(Object param,
								   		CombatEntityAPI target, DamageAPI damage,
								   		Vector2f point, boolean shieldHit) {
			
			if (!(param instanceof DamagingProjectileAPI) && param instanceof BeamAPI) {
				damage.setForceHardFlux(true);
			}
			return null;
		}
	}
}









