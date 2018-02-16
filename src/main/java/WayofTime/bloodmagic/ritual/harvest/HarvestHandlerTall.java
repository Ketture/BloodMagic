package WayofTime.bloodmagic.ritual.harvest;

import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.iface.IHarvestHandler;
import WayofTime.bloodmagic.core.registry.HarvestRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Harvest handler for crops that grow vertically such as Sugar Cane and Cactus. <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerTallCrop(BlockStack)}
 */
public class HarvestHandlerTall implements IHarvestHandler {
    public HarvestHandlerTall() {
        HarvestRegistry.registerTallCrop(new BlockStack(Blocks.REEDS));
        HarvestRegistry.registerTallCrop(new BlockStack(Blocks.CACTUS));
    }

    @Override
    public boolean harvestAndPlant(World world, BlockPos pos, BlockStack blockStack) {
        boolean retFlag = false;

        List<ItemStack> drops = new ArrayList<ItemStack>();
        if (HarvestRegistry.getTallCrops().contains(blockStack)) {
            BlockStack up = BlockStack.getStackFromPos(world, pos.up());
            if (up.equals(blockStack)) {
                drops = up.getBlock().getDrops(world, pos.up(), up.getState(), 0);
                world.destroyBlock(pos.up(), false);
                retFlag = true;
            }
        }

        if (!world.isRemote) {
            for (ItemStack drop : drops) {
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), drop);
                world.spawnEntity(item);
            }
        }

        return retFlag;
    }
}
