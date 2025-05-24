package com.yshs.jeiae2compact;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * 主类
 */
@Mod(JEIAE2Compact.MODID)
public class JEIAE2Compact {
    /**
     * MOD ID
     */
    public static final String MODID = "jeiae2compact";

    @SuppressWarnings("MissingJavadoc")
    public JEIAE2Compact() {
        // 注册到MinecraftForge事件总线，确保客户端事件能够被正确处理
        MinecraftForge.EVENT_BUS.register(this);
    }
}
