package libayon.event.events.render;

import libayon.event.CancellableEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class FluidRenderEvent extends CancellableEvent {

	private final BlockPos blockPos;
	private final IBlockState state;

	private boolean forceDraw;

	/**
	 * @formatter:off
	 * This event is fired by the BlockRendererDispatcher class: {@link net.minecraft.client.renderer.BlockRendererDispatcher#renderBlock}
	 * @formatter:on
	 * @param block
	 */
	public FluidRenderEvent(BlockPos blockPos, IBlockState state) {
		this.blockPos = blockPos;
		this.state = state;

		this.forceDraw = false;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public IBlockState getState() {
		return state;
	}

	public Block getBlock() {
		return (state.getBlock() == null ? null : state.getBlock());
	}

	public void setForceDraw(boolean forceDraw) {
		this.forceDraw = forceDraw;
	}

	public boolean shouldForceDraw() {
		return forceDraw;
	}

}