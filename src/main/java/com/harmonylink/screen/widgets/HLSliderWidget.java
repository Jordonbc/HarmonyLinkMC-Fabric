package com.harmonylink.screen.widgets;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import com.harmonylink.RangedValue;

public class HLSliderWidget extends SliderWidget {
    private final SliderCallback valueCallback;
    private final SliderCallback textCallback;
    private final RangedValue rangedValue;

    public HLSliderWidget(int x, int y, int width, int height, Text label, RangedValue rangedValue, SliderCallback valueCallback, SliderCallback textCallback) {
        super(x, y, width, height, label, rangedValue.getNormalizedValue());
        this.valueCallback = valueCallback;
        this.textCallback = textCallback;
        this.rangedValue = rangedValue;
    }

    public HLSliderWidget(int x, int y, int width, int height, Text label, RangedValue rangedValue, SliderCallback valueCallback) {
        this(x, y, width, height, label, rangedValue, valueCallback, null);
    }

    public HLSliderWidget(int x, int y, int width, int height, Text label, RangedValue rangedValue) {
        this(x, y, width, height, label, rangedValue, null, null);
    }

    @Override
    protected void updateMessage() {
        if (textCallback != null) {
            textCallback.setSliderText(this, rangedValue.getAbsoluteValue());
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
        double width = getWidth();
        double percentage = MathHelper.clamp((mouseX - (double)(getX() + 4)) / (width - 8), 0.0, 1.0);
        rangedValue.setNormalizedValue(percentage);
        updateMessage();
    }

    @Override
    protected void applyValue() {
        if (valueCallback != null) {
            valueCallback.onSliderValueChanged(this, rangedValue.getAbsoluteValue());
        }
    }


    public interface SliderCallback {
        void onSliderValueChanged(HLSliderWidget slider, double value);

        default void setSliderText(HLSliderWidget slider, double value) {
            // Default implementation does nothing
        }
    }
}
