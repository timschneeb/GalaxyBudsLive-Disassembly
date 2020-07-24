package androidx.appcompat.util;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class SeslSubheaderRoundedCorner extends SeslRoundedCorner {
    static final String TAG = "SeslSubheaderRoundedCorner";

    public SeslSubheaderRoundedCorner(Context context) {
        super(context);
    }

    public SeslSubheaderRoundedCorner(Context context, boolean z) {
        super(context, z);
    }

    public void drawRoundedCorner(int i, int i2, int i3, int i4, Canvas canvas) {
        this.mRoundedCornerBounds.set(i, i2, i3, i4);
        if ((this.mRoundedCornerMode & 1) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeBottom.setBounds(0, this.mRoundedCornerBounds.bottom, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom + this.mRoundStrokeHeight);
                this.mRoundStrokeBottom.draw(canvas);
            }
            this.mTopLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.bottom + this.mRoundRadius);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            this.mTopRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.bottom, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom + this.mRoundRadius);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeTop.setBounds(0, this.mRoundedCornerBounds.top - this.mRoundStrokeHeight, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top);
                this.mRoundStrokeTop.draw(canvas);
            }
            this.mBottomLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top - this.mRoundRadius, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.top);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            this.mBottomRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.top - this.mRoundRadius, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top);
            this.mBottomRightRound.draw(canvas);
        }
    }

    public void drawRoundedCorner(View view, Canvas canvas) {
        if (view.getTranslationY() != 0.0f) {
            this.mX = Math.round(view.getX());
            this.mY = Math.round(view.getY());
        } else {
            this.mX = view.getLeft();
            this.mY = view.getTop();
        }
        this.mRoundedCornerBounds.set(this.mX, this.mY, this.mX + view.getWidth(), this.mY + view.getHeight());
        if ((this.mRoundedCornerMode & 1) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeBottom.setBounds(0, this.mRoundedCornerBounds.bottom, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom + this.mRoundStrokeHeight);
                this.mRoundStrokeBottom.draw(canvas);
            }
            this.mTopLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.bottom, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.bottom + this.mRoundRadius);
            this.mTopLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 2) != 0) {
            this.mTopRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.bottom, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.bottom + this.mRoundRadius);
            this.mTopRightRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 4) != 0) {
            if (this.mIsStrokeRoundedCorner) {
                this.mRoundStrokeTop.setBounds(0, this.mRoundedCornerBounds.top - this.mRoundStrokeHeight, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top);
                this.mRoundStrokeTop.draw(canvas);
            }
            this.mBottomLeftRound.setBounds(this.mRoundedCornerBounds.left, this.mRoundedCornerBounds.top - this.mRoundRadius, this.mRoundedCornerBounds.left + this.mRoundRadius, this.mRoundedCornerBounds.top);
            this.mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            this.mBottomRightRound.setBounds(this.mRoundedCornerBounds.right - this.mRoundRadius, this.mRoundedCornerBounds.top - this.mRoundRadius, this.mRoundedCornerBounds.right, this.mRoundedCornerBounds.top);
            this.mBottomRightRound.draw(canvas);
        }
    }
}
