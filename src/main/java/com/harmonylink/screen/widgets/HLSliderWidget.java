package com.harmonylink.screen.widgets;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class HLSliderWidget extends SliderWidget {
    private final SliderCallback callback;
    private final double minValue;
    private final double maxValue;
    private double rangedValue;
    private double percentage;

    public HLSliderWidget(int x, int y, int width, int height, Text label, double value, double minValue, double maxValue, SliderCallback callback) {
        super(x, y, width, height, label, value);
        this.callback = callback;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.rangedValue = value;
        this.percentage = calculatePercentage(value);
    }

    public HLSliderWidget(int x, int y, int width, int height, Text label, double value, double minValue, double maxValue) {
        this(x, y, width, height, label, value, minValue, maxValue, null);
    }

    @Override
    protected void updateMessage() {
        // Perform any necessary updates to the label message
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
        double width = getWidth();
        percentage = MathHelper.clamp((mouseX - (double)(getX() + 4)) / (width - 8), 0.0, 1.0);
        rangedValue = calculateRangedValue(percentage);
    }

    private double calculatePercentage(double value) {
        double range = (maxValue + 1) - minValue;
        return (value - minValue) / range;
    }

    private double calculateRangedValue(double percentage) {
        double range = (maxValue + 1) - minValue;
        return minValue + (range * percentage);
    }

    @Override
    protected void applyValue() {
        if (callback != null) {
            callback.onSliderValueChanged(this, rangedValue);
        }
    }

    // Define the callback functional interface
    public interface SliderCallback {
        void onSliderValueChanged(HLSliderWidget slider, double rangedValue);
    }
}
