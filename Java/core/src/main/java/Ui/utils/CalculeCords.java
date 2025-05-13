package Ui.utils;

public class CalculeCords {
    public static float[] calculateRealCoordinates(float relativeX, float relativeY, float offsetX, float offsetY) {
        int textureWidth = 17;
        int textureHeight = 16;
        float realX = offsetX + (relativeX * textureWidth);
        float realY = offsetY + (relativeY * textureHeight);
        return new float[]{realX, realY};
    }
}