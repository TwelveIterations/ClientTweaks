package net.blay09.mods.clienttweaks.mixin;

import net.blay09.mods.clienttweaks.ClientTweaksConfig;
import net.blay09.mods.clienttweaks.MineSingleBlockHandler;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "handleInventoryMouseClick", at = @At("HEAD"), cancellable = true)
    private void clientTweaks$preventDoubleClickStacking(int containerId, int slotId, int button, ClickType clickType, Player player, CallbackInfo callbackInfo) {
        if (clickType == ClickType.PICKUP_ALL && ClientTweaksConfig.getActive().ui.disableDoubleClickStacking) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void clientTweaks$preventAdditionalStartDestroyBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (MineSingleBlockHandler.shouldPreventBlockBreaking()) {
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void clientTweaks$preventAdditionalContinueDestroyBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (MineSingleBlockHandler.shouldPreventBlockBreaking()) {
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void clientTweaks$markBlockBreakConsumed(BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (callbackInfo.getReturnValue()) {
            MineSingleBlockHandler.onBlockBroken();
        }
    }
}
