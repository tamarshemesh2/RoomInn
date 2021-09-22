package postpc.finalproject.RoomInn;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import android.view.View;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RotateZoomImageViewFurniture extends AppCompatImageView implements View.OnTouchListener {
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    public float newRot = 0f;
    public float scaleDiff;

    public RotateZoomImageViewFurniture(Context context) {
        super(context);
        init(context);
    }

    public RotateZoomImageViewFurniture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotateZoomImageViewFurniture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

    }

//    public void setBoardLayout(RelativeLayout board) {
//        AXIS_X_MIN = board.getX();
//        AXIS_X_MAX = board.getX() + board.getWidth();
//        AXIS_Y_MIN = board.getY();
//        AXIS_Y_MAX = board.getY() + board.getHeight();
//    }

    /*
     * Use onSizeChanged() to calculate values based on the view's size.
     * The view has no size during init(), so we must wait for this
     * callback.
     */


    /*
     * Operate on two-finger events to rotate the image.
     * This method calculates the change in angle between the
     * pointers and rotates the image accordingly.  As the user
     * rotates their fingers, the image will follow.
     */
    RelativeLayout.LayoutParams params;
    int startWidth;
    int startHeight;
    float dx = 0, dy = 0, x = 0, y = 0;
    float angle = 0;


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.round(Math.toDegrees(radians) / 90) * 90;
    }
    public void setPlace(float x, float y){
        params = (RelativeLayout.LayoutParams) this.getLayoutParams();

        params.leftMargin = (int) (x);
        params.topMargin = (int) (y);

        params.rightMargin = 0;
        params.bottomMargin = 0;
        params.rightMargin = params.leftMargin + (5 * params.width);
        params.bottomMargin = params.topMargin + (10 * params.height);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final ImageView view = (ImageView) v;
        ((BitmapDrawable) view.getDrawable()).setAntiAlias(true);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                startWidth = params.width;
                startHeight = params.height;
                dx = event.getRawX() - params.leftMargin;
                dy = event.getRawY() - params.topMargin;

                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    mode = ZOOM;
                }

                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {

                    x = event.getRawX();
                    y = event.getRawY();
                    params.leftMargin = (int) (x - dx);
                    params.topMargin = (int) (y - dy);

                    params.rightMargin = 0;
                    params.bottomMargin = 0;
                    params.rightMargin = params.leftMargin + (5 * params.width);
                    params.bottomMargin = params.topMargin + (10 * params.height);

                    fixing();
                    view.setLayoutParams(params);


                } else if (mode == ZOOM) {

                    if (event.getPointerCount() == 2) {

                        newRot = rotation(event);
                        float r = newRot - d;
                        angle = r;

                        x = event.getRawX();
                        y = event.getRawY();

                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            float scale = newDist / oldDist * view.getScaleX();
                            if (scale > 0.6) {
                                scaleDiff = scale;
                                view.setScaleX(scale);
                                view.setScaleY(scale);
                            }
                        }

                        x = event.getRawX();
                        y = event.getRawY();

                        params.leftMargin = (int) ((x - dx) + scaleDiff);
                        params.topMargin = (int) ((y - dy) + scaleDiff);

                        params.rightMargin = 0;
                        params.bottomMargin = 0;
                        params.rightMargin = params.leftMargin + (5 * params.width);
                        params.bottomMargin = params.topMargin + (10 * params.height);


                        fixing();
                        view.setLayoutParams(params);
                    }
                    if (newRot>0){
                    view.animate().rotationBy(newRot).setDuration(0).setInterpolator(new LinearInterpolator()).start();}

                }

                break;
        }

        return true;
    }
    public void fixing()
    {
        if (params.leftMargin < 0){
            params.leftMargin=0;
            params.rightMargin = params.leftMargin + (5 * params.width);
        }
        if (params.topMargin < 0){
            params.topMargin=0;
            params.bottomMargin = params.topMargin + (10 * params.height);
        }
        if (params.bottomMargin<0){
            params.bottomMargin=0;
            params.topMargin =  (10 * params.height);
        }
        if (params.rightMargin<0){
            params.rightMargin=0;
            params.leftMargin =(5 * params.height);
        }


    }
}
