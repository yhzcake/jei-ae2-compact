package com.yshs.jeiae2compact.mixin;

import appeng.api.stacks.AEItemKey;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.style.ScreenStyle;
import appeng.helpers.InventoryAction;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import com.yshs.jeiae2compact.jei.JEIAE2CompactPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * mixin MEStorageScreen
 *
 * @param <T> idk
 */
@Mixin(value = MEStorageScreen.class)
public abstract class MEStorageScreenMixin<T extends MEStorageMenu> extends AEBaseScreen<T> {

    @Final
    @Shadow
    protected Repo repo;

    @SuppressWarnings("MissingJavadoc")
    public MEStorageScreenMixin(T menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    /**
     * 注入到mouseClicked方法，处理中键点击物品
     */
    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        // 检查是否是中键点击
        if (Minecraft.getInstance().options.keyPickItem.matchesMouse(button)) {

            // 获取JEI的运行时
            IJeiRuntime jeiRuntime = JEIAE2CompactPlugin.getJeiRuntime();
            // 得到书签覆盖层下面的物品
            ItemStack itemStack = jeiRuntime.getBookmarkOverlay().getItemStackUnderMouse();
            if (itemStack == null) {
                return;
            }
            // 得到目标物品的AEKey
            AEItemKey targetKey = AEItemKey.of(itemStack);
            // 遍历AE终端中的所有条目
            repo.getAllEntries().stream()
                    // 过滤掉无法自动合成的条目
                    .filter(GridInventoryEntry::isCraftable)
                    // 过滤掉空值
                    .filter(entry -> entry.getWhat() != null)
                    // 找到目标条目
                    .filter(entry -> entry.getWhat().equals(targetKey))
                    // 打开自动合成菜单
                    .forEach(entry -> {
                        long serial = entry.getSerial();
                        menu.handleInteraction(serial, InventoryAction.AUTO_CRAFT);
                    });
        }
    }
}
