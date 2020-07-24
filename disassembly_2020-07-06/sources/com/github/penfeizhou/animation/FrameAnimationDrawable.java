package com.github.penfeizhou.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.github.penfeizhou.animation.decode.FrameSeqDecoder;
import com.github.penfeizhou.animation.loader.Loader;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public abstract class FrameAnimationDrawable extends Drawable implements Animatable2Compat, FrameSeqDecoder.RenderListener {
    private static final int MSG_ANIMATION_END = 2;
    private static final int MSG_ANIMATION_START = 1;
    private static final String TAG = FrameAnimationDrawable.class.getSimpleName();
    /* access modifiers changed from: private */
    public Set<Animatable2Compat.AnimationCallback> animationCallbacks = new HashSet();
    private Bitmap bitmap;
    private DrawFilter drawFilter = new PaintFlagsDrawFilter(0, 3);
    private final FrameSeqDecoder frameSeqDecoder;
    private Runnable invalidateRunnable = new Runnable() {
        public void run() {
            FrameAnimationDrawable.this.invalidateSelf();
        }
    };
    private Matrix matrix = new Matrix();
    private final Paint paint = new Paint();
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                for (Animatable2Compat.AnimationCallback onAnimationStart : FrameAnimationDrawable.this.animationCallbacks) {
                    onAnimationStart.onAnimationStart(FrameAnimationDrawable.this);
                }
            } else if (i == 2) {
                for (Animatable2Compat.AnimationCallback onAnimationEnd : FrameAnimationDrawable.this.animationCallbacks) {
                    onAnimationEnd.onAnimationEnd(FrameAnimationDrawable.this);
                }
            }
        }
    };

    /* access modifiers changed from: protected */
    public abstract FrameSeqDecoder createFrameSeqDecoder(Loader loader, FrameSeqDecoder.RenderListener renderListener);

    public int getOpacity() {
        return -3;
    }

    public FrameAnimationDrawable(Loader loader) {
        this.paint.setAntiAlias(true);
        this.frameSeqDecoder = createFrameSeqDecoder(loader, this);
    }

    public void setLoopLimit(int i) {
        this.frameSeqDecoder.setLoopLimit(i);
    }

    public void reset() {
        this.frameSeqDecoder.reset();
    }

    public void pause() {
        this.frameSeqDecoder.pause();
    }

    public void resume() {
        this.frameSeqDecoder.resume();
    }

    public boolean isPaused() {
        return this.frameSeqDecoder.isPaused();
    }

    public void start() {
        this.frameSeqDecoder.start();
    }

    public void stop() {
        this.frameSeqDecoder.stop();
    }

    public boolean isRunning() {
        return this.frameSeqDecoder.isRunning();
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            canvas.setDrawFilter(this.drawFilter);
            canvas.drawBitmap(this.bitmap, this.matrix, this.paint);
        }
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        this.frameSeqDecoder.setDesiredSize(getBounds().width(), getBounds().height());
        this.matrix.setScale(((((float) getBounds().width()) * 1.0f) * ((float) this.frameSeqDecoder.getSampleSize())) / ((float) this.frameSeqDecoder.getBounds().width()), ((((float) getBounds().height()) * 1.0f) * ((float) this.frameSeqDecoder.getSampleSize())) / ((float) this.frameSeqDecoder.getBounds().height()));
    }

    public void setAlpha(int i) {
        this.paint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public void onStart() {
        Message.obtain(this.uiHandler, 1).sendToTarget();
    }

    public void onRender(ByteBuffer byteBuffer) {
        if (isRunning()) {
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 == null || bitmap2.isRecycled()) {
                this.bitmap = Bitmap.createBitmap(this.frameSeqDecoder.getBounds().width() / this.frameSeqDecoder.getSampleSize(), this.frameSeqDecoder.getBounds().height() / this.frameSeqDecoder.getSampleSize(), Bitmap.Config.ARGB_8888);
            }
            byteBuffer.rewind();
            if (byteBuffer.remaining() < this.bitmap.getByteCount()) {
                Log.e(TAG, "onRender:Buffer not large enough for pixels");
                return;
            }
            this.bitmap.copyPixelsFromBuffer(byteBuffer);
            this.uiHandler.post(this.invalidateRunnable);
        }
    }

    public void onEnd() {
        Message.obtain(this.uiHandler, 2).sendToTarget();
    }

    public boolean setVisible(boolean z, boolean z2) {
        if (z) {
            if (!isRunning()) {
                start();
            }
        } else if (isRunning()) {
            stop();
        }
        return super.setVisible(z, z2);
    }

    public int getIntrinsicWidth() {
        try {
            return this.frameSeqDecoder.getBounds().width();
        } catch (Exception unused) {
            return 0;
        }
    }

    public int getIntrinsicHeight() {
        try {
            return this.frameSeqDecoder.getBounds().height();
        } catch (Exception unused) {
            return 0;
        }
    }

    public void registerAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        this.animationCallbacks.add(animationCallback);
    }

    public boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        return this.animationCallbacks.remove(animationCallback);
    }

    public void clearAnimationCallbacks() {
        this.animationCallbacks.clear();
    }
}
