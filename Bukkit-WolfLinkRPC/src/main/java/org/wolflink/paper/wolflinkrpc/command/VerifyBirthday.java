package org.wolflink.paper.wolflinkrpc.command;

import kotlin.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.wolflink.common.wolflinkrpc.api.annotations.LocalCallHandler;
import org.wolflink.common.wolflinkrpc.api.enums.PermissionLevel;
import org.wolflink.common.wolflinkrpc.api.interfaces.CallbackFunction;
import org.wolflink.common.wolflinkrpc.api.interfaces.command.ILocalHandler;
import org.wolflink.common.wolflinkrpc.entity.RPCDataPack;
import org.wolflink.common.wolflinkrpc.entity.impl.databody.SimpleCommandExecuteBody;
import org.wolflink.common.wolflinkrpc.entity.role.ClientReceiver;
import org.wolflink.common.wolflinkrpc.entity.role.RPCUser;
import org.wolflink.common.wolflinkrpc.service.MQService;

import java.util.List;
import java.util.Random;

@LocalCallHandler
public class VerifyBirthday implements ILocalHandler {
    @NotNull
    @Override
    public String getCommand() {
        return "> 生日";
    }

    @NotNull
    @Override
    public Pair<Boolean, String> invoke(@NotNull RPCUser sender, @NotNull List<String> args) {

        String routingKey = "mirai_WolfBot+";
        String message = "验证生日";
        RPCDataPack datapack = new RPCDataPack.Builder()
                .setDatapackBody(new SimpleCommandExecuteBody(message))
                .setSender(sender)
                .addReceiver(new ClientReceiver(routingKey))
                .build();
        MQService.INSTANCE.sendDataPack(datapack, true, new CallbackFunction() {
            @Override
            public void success(@NotNull RPCDataPack datapack) {
                Player p = Bukkit.getPlayer(sender.getUserName());
                if(p == null || !p.isOnline())return;
                if(Bukkit.getOnlinePlayers().size() > 1)
                {
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("[ 公告 ] 今天是 "+sender.getUserName()+" 的生日哦~大家快来一起祝Ta生日快乐吧！");
                    Bukkit.broadcastMessage("");
                }else
                {
                    p.sendMessage("§7[ §6狼与香辛料 §7] §e服务器里在线人数不多喔，不要灰心！已经去群里通知其他人啦~");
                }
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE,1f,1f);
                p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1f,1f);
                p.sendTitle("§6生日快乐！§f"+p.getName(),"§7生日礼物已经发到你的背包了喔~",10,60,10);

                Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                FireworkEffect.Type type = FireworkEffect.Type.BALL_LARGE;
                Random random = new Random();
                int r1 = random.nextInt(256);
                int g1 = random.nextInt(256);
                int b1 = random.nextInt(256);
                int r2 = random.nextInt(256);
                int g2 = random.nextInt(256);
                int b2 = random.nextInt(256);
                Color c1 = Color.fromRGB(r1,g1,b1);
                Color c2 = Color.fromRGB(r2,g2,b2);
                FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(true).withColor(c1).withFade(c2).with(type).trail(true).build();
                fireworkMeta.addEffect(fireworkEffect);
                fireworkMeta.setPower(random.nextInt(2) + 1);
                firework.setFireworkMeta(fireworkMeta);
            }
            @Override
            public void failed(@NotNull RPCDataPack datapack) {
                Player p = Bukkit.getPlayer(sender.getUserName());
                if(p == null || !p.isOnline())return;
                p.sendMessage("§7[ §6狼与香辛料 §7] §c想什么呢 今天不是你的生日！");
            }
        }, 10);
        return new Pair<>(true,"指令已执行");
    }

    @NotNull
    @Override
    public PermissionLevel getPermission() {
        return PermissionLevel.DEFAULT;
    }
}
