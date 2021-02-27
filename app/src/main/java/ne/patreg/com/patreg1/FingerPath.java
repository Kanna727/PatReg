package ne.patreg.com.patreg1;


import android.graphics.Path;

public class FingerPath {
    public int color;
    int strokeWidth;
    Path path;

    FingerPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
