package ne.patreg.com.patreg1;

import android.util.Log;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Created by chitt on 2/13/2018.
 */

public class PaintView extends View {
    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.parseColor("#f4daee");
    public static final int DEFAULT_BG_COLOR = Color.parseColor("#493540");
    private static final float TOUCH_TOLERANCE = 0;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private Paint mLine;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    File file;
    public PaintView(Context context) throws IOException {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mLine = new Paint();
        mLine.setAntiAlias(true);
        mLine.setDither(true);
        mLine.setColor(DEFAULT_COLOR);
        mLine.setStyle(Paint.Style.STROKE);
        mLine.setStrokeJoin(Paint.Join.ROUND);
        mLine.setStrokeCap(Paint.Cap.ROUND);
        mLine.setXfermode(null);
        mLine.setAlpha(0xff);
    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
        Log.i("testing","init called");
        file=new File(getContext().getExternalFilesDir(null),"XY_coordinates.txt");
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
            //bufferedWriter.write("Coordinates\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);

            mCanvas.drawPath(fp.path, mPaint);

        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawLine(0, canvas.getHeight()/2 - 100, canvas.getWidth(), canvas.getHeight()/2 - 100, mLine);
        canvas.drawLine(0, canvas.getHeight()/2 + 100, canvas.getWidth(), canvas.getHeight()/2 + 100, mLine);
        canvas.drawLine(0, canvas.getHeight()/2 - 300, canvas.getWidth(), canvas.getHeight()/2 - 300, mLine);
        canvas.drawLine(0, canvas.getHeight()/2 + 300, canvas.getWidth(), canvas.getHeight()/2 + 300, mLine);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }
    String sb;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x   = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                sb=x+","+y+","+0;
                try {
                    BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true));
                    bufferedWriter.write(sb);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                sb=x+","+y+","+0;
                try {
                    BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true));
                    bufferedWriter.write(sb);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                sb=x+","+y+","+1;
                try {
                    BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true));
                    bufferedWriter.write(sb);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }
}
