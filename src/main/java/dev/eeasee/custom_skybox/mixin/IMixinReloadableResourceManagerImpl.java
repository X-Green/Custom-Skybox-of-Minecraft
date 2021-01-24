package dev.eeasee.custom_skybox.mixin;

import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ReloadableResourceManagerImpl.class)
public interface IMixinReloadableResourceManagerImpl {
    @Accessor
    Map<String, NamespaceResourceManager> getNamespaceManagers();
}
