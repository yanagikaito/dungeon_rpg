package dungeon_rpg;

import dungeon_rpg.battle.Battle;
import dungeon_rpg.magic.*;
import dungeon_rpg.map.MapManager;
import dungeon_rpg.monster.Boss;
import dungeon_rpg.monster.Slime;
import dungeon_rpg.player.Player;

import java.util.Random;


public class GameStart {

    Player player = new Player("勇者", 100, 1, 50, 0, 0, 0, 0);
    Slime slime = new Slime("スライム", 200, 1, 0, 0);
    Boss boss = new Boss("魔王", 300, 0, 20, 0);
    Battle battle = new Battle();
    Position current = new Position(1, 1);

    // MapManagerクラスをインスタンス化して引数this(GameStart)を渡します。
    public MapManager mapM = new MapManager(this);


    public GameStart() {
        mapM.maze();
        mapM.maze2();
    }

    public void battleLog() {
        for (int i = 0; slime.getHp() > 0; i++) {
            if (slime.getHp() != 0) {
                battle.playerAttackDamage(player, slime);
                battle.magicAttack(MagicType.FIRE, player, slime);
                battle.magicAttack(MagicType.THUNDER, player, slime);
                battle.magicAttack(MagicType.BLIZZARD, player, slime);
                if (slime.getHp() <= 0) {
                    slime.Hp0();
                    break;
                }
                battle.playerCriticalHit(player, slime);
                if (slime.getHp() <= 0) {
                    slime.Hp0();
                    break;
                }
                battle.monsterAttackDamage(player, slime);
                if (player.getHp() <= 0) {
                    player.Hp0();
                    break;
                }
                battle.monsterLargeDamage(player, slime);
                if (player.getHp() <= 0) {
                    player.Hp0();
                    break;
                }
            }
        }
        if (player.getHp() > 0) {
            System.out.println("勇者は勝利した！");
            battle.training(player);
            player = new Player("勇者", player.getHp(), player.getLv(), player.getMp(), 0, 0, 0, player.getGold());
            slime = new Slime("スライム", 200, 1, 0, 0);
        } else {
            System.out.println("勇者は負けた！");
            mapM.current = new Position(1, 1);
            player = new Player("勇者", 100, 1, 50, 0, 0, 0, 0);
            slime = new Slime("スライム", 200, 1, 0, 0);
        }
    }

    public void bossBattlelog() {
        for (int i = 0; boss.getHp() > 0; i++) {
            battle.playerBossAttackDamage(player, boss);
            battle.bossMagicAttack(MagicType.FIRE, player, boss);
            battle.bossMagicAttack(MagicType.BLIZZARD, player, boss);
            battle.bossMagicAttack(MagicType.THUNDER, player, boss);
            if (boss.getHp() <= 0) {
                boss.Hp0();
                break;
            }
            battle.bossAttackDamage(player, boss);
            if (player.getHp() <= 0) {
                player.Hp0();
                break;
            }
        }
        if (player.getHp() > 0) {
            System.out.println("勇者は魔王に勝利しました！");
            battle.training(player);
        } else {
            System.out.println("勇者は魔王に負けました！");
            mapM.current = new Position(1, 1);
            player = new Player("勇者", 100, 1, 50, 0, 0, 0, 0);
            slime = new Slime("スライム", 200, 1, 0, 0);
        }
    }

    public void recovery() {
        Random random = new Random();
        int randomHealing = 5;
        int recoveryMoney = 20;
        if (recoveryMoney < player.getGold()) {
            int remainingMoney = player.getGold() - recoveryMoney;
            if (player.getGold() < 0) {
                player.Gold0();
                player.put("所持金が0になりました！");
            }
            randomHealing = random.nextInt(randomHealing) + 11;
            int healing = player.getHp() + randomHealing;
            player.setHp(healing);
            player.setGold(remainingMoney);
            player.put(player.getName() + "のHPは" + randomHealing + "回復！");
            player.put(player.getName() + "の現在のHPは" + player.getHp() + "です！");
            player.put("残り所持金" + player.getGold() + "です！");
        }
    }
}
