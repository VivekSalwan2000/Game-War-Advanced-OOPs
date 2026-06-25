package Utility;

import ChallengeDecision.*;
import Game.ArmyUnit;
import Game.AttackManager;
import Models.Village;
import VillageElements.AttackingEntities;
import VillageElements.Defender;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an adapter to convert AttackManager class to Arbitrer class provide by professor
 */
public class ArbitrerAdapter extends AttackManager {

    /**
     * This method returns the output of the fight.
     * @param attackerArmy Attacking army of the attacker
     * @param defender     Village of the defender
     * @return Outcome of the attack
     */
    @Override
    public ChallengeResult judgeAttack(ArmyUnit attackerArmy, Village defender) {

        //create the entity set for the Arbitrer
        ChallengeEntitySet<Double,Double> challenger = new ChallengeEntitySet<>();
        ChallengeEntitySet<Double,Double> challengee = new ChallengeEntitySet<>();

        //add all the attack and defence into their respective entity sets
        List<ChallengeAttack<Double,Double>> entityAttackList=new ArrayList<>(attackerArmy.getArmyUnit().size());
        List<ChallengeDefense<Double,Double>> entityDefenseList=new ArrayList<>();
        List<ChallengeResource> entityResourceList=new ArrayList<>();

        attackerArmy.getArmyUnit().forEach(
                (challengerEntity)->{
                    AttackingEntities attackingEntity = (AttackingEntities) challengerEntity;
                    ChallengeAttack challengeEntity = new Attack_Entity_To_Challenge_Attack_Adapter(attackingEntity);
                    entityAttackList.add(challengeEntity);
                }
        );

        challenger.setEntityAttackList(entityAttackList);
        //System.out.println(challenger.getEntityAttackList());

        defender.getVillageMap().getBuildings().forEach(
                (challengeeEntity)->{
                    if(challengeeEntity instanceof Defender){
                        AttackingEntities attackingEntities = (AttackingEntities) challengeeEntity;
                        ChallengeDefense challengeDefense = new Defence_Entity_To_Challenge_Defense_Adapter(attackingEntities);
                        entityDefenseList.add(challengeDefense);
                    }
                }
        );

        challengee.setEntityDefenseList(entityDefenseList);

        return Arbitrer.challengeDecide(challenger, challengee);
    }
}
