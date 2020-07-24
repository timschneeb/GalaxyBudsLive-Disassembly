package com.google.android.material.shape;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import androidx.core.graphics.drawable.TintAwareDrawable;
import com.google.android.material.shadow.ShadowRenderer;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import com.google.android.material.shape.ShapePath;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MaterialShapeDrawable extends Drawable implements TintAwareDrawable {
    public static final int SHADOW_COMPAT_MODE_ALWAYS = 2;
    public static final int SHADOW_COMPAT_MODE_DEFAULT = 0;
    public static final int SHADOW_COMPAT_MODE_NEVER = 1;
    private static final Paint clearPaint = new Paint(1);
    /* access modifiers changed from: private */
    public final ShapePath.ShadowCompatOperation[] cornerShadowOperation;
    private MaterialShapeDrawableState drawableState;
    /* access modifiers changed from: private */
    public final ShapePath.ShadowCompatOperation[] edgeShadowOperation;
    private final Paint fillPaint;
    private final RectF insetRectF;
    private final Matrix matrix;
    private final Path path;
    private boolean pathDirty;
    private final Path pathInsetByStroke;
    private final ShapeAppearancePathProvider pathProvider;
    private final ShapeAppearancePathProvider.PathListener pathShadowListener;
    private final RectF rectF;
    private final Region scratchRegion;
    private final ShadowRenderer shadowRenderer;
    private final Paint strokePaint;
    private ShapeAppearanceModel strokeShapeAppearance;
    private PorterDuffColorFilter strokeTintFilter;
    private PorterDuffColorFilter tintFilter;
    private final Region transparentRegion;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CompatibilityShadowMode {
    }

    private static int modulateAlpha(int i, int i2) {
        return (i * (i2 + (i2 >>> 7))) >>> 8;
    }

    public int getOpacity() {
        return -3;
    }

    public MaterialShapeDrawable() {
        this(new ShapeAppearanceModel());
    }

    public MaterialShapeDrawable(Context context, AttributeSet attributeSet, int i, int i2) {
        this(new ShapeAppearanceModel(context, attributeSet, i, i2));
    }

    public MaterialShapeDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this(new MaterialShapeDrawableState(shapeAppearanceModel));
    }

    private MaterialShapeDrawable(MaterialShapeDrawableState materialShapeDrawableState) {
        this.cornerShadowOperation = new ShapePath.ShadowCompatOperation[4];
        this.edgeShadowOperation = new ShapePath.ShadowCompatOperation[4];
        this.matrix = new Matrix();
        this.path = new Path();
        this.pathInsetByStroke = new Path();
        this.rectF = new RectF();
        this.insetRectF = new RectF();
        this.transparentRegion = new Region();
        this.scratchRegion = new Region();
        this.fillPaint = new Paint(1);
        this.strokePaint = new Paint(1);
        this.shadowRenderer = new ShadowRenderer();
        this.pathProvider = new ShapeAppearancePathProvider();
        this.drawableState = materialShapeDrawableState;
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.fillPaint.setStyle(Paint.Style.FILL);
        clearPaint.setColor(-1);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        updateTintFilter();
        updateColorsForState(getState(), false);
        this.pathShadowListener = new ShapeAppearancePathProvider.PathListener() {
            public void onCornerPathCreated(ShapePath shapePath, Matrix matrix, int i) {
                MaterialShapeDrawable.this.cornerShadowOperation[i] = shapePath.createShadowCompatOperation(matrix);
            }

            public void onEdgePathCreated(ShapePath shapePath, Matrix matrix, int i) {
                MaterialShapeDrawable.this.edgeShadowOperation[i] = shapePath.createShadowCompatOperation(matrix);
            }
        };
    }

    public Drawable.ConstantState getConstantState() {
        return this.drawableState;
    }

    public Drawable mutate() {
        this.drawableState = new MaterialShapeDrawableState(this.drawableState);
        return this;
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.drawableState.shapeAppearanceModel = shapeAppearanceModel;
        invalidateSelf();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.drawableState.shapeAppearanceModel;
    }

    @Deprecated
    public void setShapedViewModel(ShapePathModel shapePathModel) {
        setShapeAppearanceModel(shapePathModel);
    }

    @Deprecated
    public ShapeAppearanceModel getShapedViewModel() {
        return getShapeAppearanceModel();
    }

    public void setFillColor(ColorStateList colorStateList) {
        if (this.drawableState.fillColor != colorStateList) {
            this.drawableState.fillColor = colorStateList;
            onStateChange(getState());
        }
    }

    public ColorStateList getFillColor() {
        return this.drawableState.fillColor;
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        if (this.drawableState.strokeColor != colorStateList) {
            this.drawableState.strokeColor = colorStateList;
            onStateChange(getState());
        }
    }

    public ColorStateList getStrokeColor() {
        return this.drawableState.strokeColor;
    }

    public void setTintMode(PorterDuff.Mode mode) {
        if (this.drawableState.tintMode != mode) {
            this.drawableState.tintMode = mode;
            updateTintFilter();
            invalidateSelfIgnoreShape();
        }
    }

    public void setTintList(ColorStateList colorStateList) {
        this.drawableState.tintList = colorStateList;
        updateTintFilter();
        invalidateSelfIgnoreShape();
    }

    public ColorStateList getTintList() {
        return this.drawableState.tintList;
    }

    public ColorStateList getStrokeTintList() {
        return this.drawableState.strokeTintList;
    }

    @Deprecated
    public int getStrokeTint() {
        return this.drawableState.strokeTintList.getColorForState(getState(), 0);
    }

    public void setTint(int i) {
        setTintList(ColorStateList.valueOf(i));
    }

    public void setStrokeTint(ColorStateList colorStateList) {
        this.drawableState.strokeTintList = colorStateList;
        updateTintFilter();
        invalidateSelfIgnoreShape();
    }

    public void setStrokeTint(int i) {
        setStrokeTint(ColorStateList.valueOf(i));
    }

    public void setStroke(float f, int i) {
        setStrokeWidth(f);
        setStrokeColor(ColorStateList.valueOf(i));
    }

    public void setStroke(float f, ColorStateList colorStateList) {
        setStrokeWidth(f);
        setStrokeColor(colorStateList);
    }

    public float getStrokeWidth() {
        return this.drawableState.strokeWidth;
    }

    public void setStrokeWidth(float f) {
        this.drawableState.strokeWidth = f;
        invalidateSelf();
    }

    public void setAlpha(int i) {
        if (this.drawableState.alpha != i) {
            this.drawableState.alpha = i;
            invalidateSelfIgnoreShape();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.drawableState.colorFilter = colorFilter;
        invalidateSelfIgnoreShape();
    }

    public Region getTransparentRegion() {
        this.transparentRegion.set(getBounds());
        calculatePath(getBoundsAsRectF(), this.path);
        this.scratchRegion.setPath(this.path, this.transparentRegion);
        this.transparentRegion.op(this.scratchRegion, Region.Op.DIFFERENCE);
        return this.transparentRegion;
    }

    /* access modifiers changed from: protected */
    public RectF getBoundsAsRectF() {
        Rect bounds = getBounds();
        this.rectF.set((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom);
        return this.rectF;
    }

    public void setCornerRadius(float f) {
        this.drawableState.shapeAppearanceModel.setCornerRadius(f);
        invalidateSelf();
    }

    public boolean isPointInTransparentRegion(int i, int i2) {
        return getTransparentRegion().contains(i, i2);
    }

    public int getShadowCompatibilityMode() {
        return this.drawableState.shadowCompatMode;
    }

    public void setShadowCompatibilityMode(int i) {
        if (this.drawableState.shadowCompatMode != i) {
            this.drawableState.shadowCompatMode = i;
            invalidateSelfIgnoreShape();
        }
    }

    @Deprecated
    public boolean isShadowEnabled() {
        return this.drawableState.shadowCompatMode == 0 || this.drawableState.shadowCompatMode == 2;
    }

    @Deprecated
    public void setShadowEnabled(boolean z) {
        setShadowCompatibilityMode(z ^ true ? 1 : 0);
    }

    public float getInterpolation() {
        return this.drawableState.interpolation;
    }

    public void setInterpolation(float f) {
        if (this.drawableState.interpolation != f) {
            this.drawableState.interpolation = f;
            invalidateSelf();
        }
    }

    public int getShadowElevation() {
        return this.drawableState.shadowCompatElevation;
    }

    public void setShadowElevation(int i) {
        if (this.drawableState.shadowCompatElevation != i) {
            MaterialShapeDrawableState materialShapeDrawableState = this.drawableState;
            materialShapeDrawableState.shadowCompatRadius = i;
            materialShapeDrawableState.shadowCompatElevation = i;
            invalidateSelfIgnoreShape();
        }
    }

    public int getShadowVerticalOffset() {
        return this.drawableState.shadowCompatOffset;
    }

    public void setShadowVerticalOffset(int i) {
        if (this.drawableState.shadowCompatOffset != i) {
            this.drawableState.shadowCompatOffset = i;
            invalidateSelfIgnoreShape();
        }
    }

    public int getShadowCompatRotation() {
        return this.drawableState.shadowCompatRotation;
    }

    public void setShadowCompatRotation(int i) {
        if (this.drawableState.shadowCompatRotation != i) {
            this.drawableState.shadowCompatRotation = i;
            invalidateSelfIgnoreShape();
        }
    }

    @Deprecated
    public int getShadowRadius() {
        return this.drawableState.shadowCompatRadius;
    }

    @Deprecated
    public void setShadowRadius(int i) {
        this.drawableState.shadowCompatRadius = i;
    }

    private boolean requiresCompatShadow() {
        return Build.VERSION.SDK_INT < 21 || !this.path.isConvex();
    }

    public float getScale() {
        return this.drawableState.scale;
    }

    public void setScale(float f) {
        if (this.drawableState.scale != f) {
            this.drawableState.scale = f;
            invalidateSelf();
        }
    }

    public void invalidateSelf() {
        this.pathDirty = true;
        invalidateSelfIgnoreShape();
    }

    private void invalidateSelfIgnoreShape() {
        super.invalidateSelf();
    }

    public void setUseTintColorForShadow(boolean z) {
        if (this.drawableState.useTintColorForShadow != z) {
            this.drawableState.useTintColorForShadow = z;
            invalidateSelf();
        }
    }

    public void setShadowColor(int i) {
        this.shadowRenderer.setShadowColor(i);
        this.drawableState.useTintColorForShadow = false;
        invalidateSelfIgnoreShape();
    }

    public Paint.Style getPaintStyle() {
        return this.drawableState.paintStyle;
    }

    public void setPaintStyle(Paint.Style style) {
        this.drawableState.paintStyle = style;
        invalidateSelfIgnoreShape();
    }

    private boolean hasCompatShadow() {
        if (this.drawableState.shadowCompatMode == 1 || this.drawableState.shadowCompatRadius <= 0 || (this.drawableState.shadowCompatMode != 2 && !requiresCompatShadow())) {
            return false;
        }
        return true;
    }

    private boolean hasFill() {
        return this.drawableState.paintStyle == Paint.Style.FILL_AND_STROKE || this.drawableState.paintStyle == Paint.Style.FILL;
    }

    private boolean hasStroke() {
        return (this.drawableState.paintStyle == Paint.Style.FILL_AND_STROKE || this.drawableState.paintStyle == Paint.Style.STROKE) && this.strokePaint.getStrokeWidth() > 0.0f;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.pathDirty = true;
        super.onBoundsChange(rect);
    }

    public void draw(Canvas canvas) {
        this.fillPaint.setColorFilter(this.tintFilter);
        int alpha = this.fillPaint.getAlpha();
        this.fillPaint.setAlpha(modulateAlpha(alpha, this.drawableState.alpha));
        this.strokePaint.setColorFilter(this.strokeTintFilter);
        this.strokePaint.setStrokeWidth(this.drawableState.strokeWidth);
        int alpha2 = this.strokePaint.getAlpha();
        this.strokePaint.setAlpha(modulateAlpha(alpha2, this.drawableState.alpha));
        if (this.pathDirty) {
            calculateStrokePath();
            calculatePath(getBoundsAsRectF(), this.path);
            this.pathDirty = false;
        }
        if (hasCompatShadow()) {
            canvas.save();
            prepareCanvasForShadow(canvas);
            Bitmap createBitmap = Bitmap.createBitmap(getBounds().width() + (this.drawableState.shadowCompatRadius * 2), getBounds().height() + (this.drawableState.shadowCompatRadius * 2), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(createBitmap);
            float f = (float) (getBounds().left - this.drawableState.shadowCompatRadius);
            float f2 = (float) (getBounds().top - this.drawableState.shadowCompatRadius);
            canvas2.translate(-f, -f2);
            drawCompatShadow(canvas2);
            canvas.drawBitmap(createBitmap, f, f2, (Paint) null);
            createBitmap.recycle();
            canvas.restore();
        }
        if (hasFill()) {
            drawFillShape(canvas);
        }
        if (hasStroke()) {
            drawStrokeShape(canvas);
        }
        this.fillPaint.setAlpha(alpha);
        this.strokePaint.setAlpha(alpha2);
    }

    private void drawShape(Canvas canvas, Paint paint, Path path2, ShapeAppearanceModel shapeAppearanceModel, RectF rectF2) {
        if (shapeAppearanceModel.isRoundRect()) {
            float cornerSize = shapeAppearanceModel.getTopRightCorner().getCornerSize();
            canvas.drawRoundRect(rectF2, cornerSize, cornerSize, paint);
            return;
        }
        canvas.drawPath(path2, paint);
    }

    /* access modifiers changed from: protected */
    public void drawShape(Canvas canvas, Paint paint, Path path2, RectF rectF2) {
        drawShape(canvas, paint, path2, this.drawableState.shapeAppearanceModel, rectF2);
    }

    private void drawFillShape(Canvas canvas) {
        drawShape(canvas, this.fillPaint, this.path, this.drawableState.shapeAppearanceModel, getBoundsAsRectF());
    }

    private void drawStrokeShape(Canvas canvas) {
        drawShape(canvas, this.strokePaint, this.pathInsetByStroke, this.strokeShapeAppearance, getBoundsInsetByStroke());
    }

    private void prepareCanvasForShadow(Canvas canvas) {
        int sin = (int) (((double) this.drawableState.shadowCompatOffset) * Math.sin(Math.toRadians((double) this.drawableState.shadowCompatRotation)));
        int cos = (int) (((double) this.drawableState.shadowCompatOffset) * Math.cos(Math.toRadians((double) this.drawableState.shadowCompatRotation)));
        if (Build.VERSION.SDK_INT < 21) {
            Rect clipBounds = canvas.getClipBounds();
            clipBounds.inset(-this.drawableState.shadowCompatRadius, -this.drawableState.shadowCompatRadius);
            clipBounds.offset(-Math.abs(sin), -Math.abs(cos));
            canvas.clipRect(clipBounds, Region.Op.REPLACE);
        }
        canvas.translate((float) sin, (float) cos);
    }

    private void drawCompatShadow(Canvas canvas) {
        if (this.drawableState.shadowCompatOffset != 0) {
            canvas.drawPath(this.path, this.shadowRenderer.getShadowPaint());
        }
        for (int i = 0; i < 4; i++) {
            this.cornerShadowOperation[i].draw(this.shadowRenderer, this.drawableState.shadowCompatRadius, canvas);
            this.edgeShadowOperation[i].draw(this.shadowRenderer, this.drawableState.shadowCompatRadius, canvas);
        }
        int sin = (int) (((double) this.drawableState.shadowCompatOffset) * Math.sin(Math.toRadians((double) this.drawableState.shadowCompatRotation)));
        int cos = (int) (((double) this.drawableState.shadowCompatOffset) * Math.cos(Math.toRadians((double) this.drawableState.shadowCompatRotation)));
        canvas.translate((float) (-sin), (float) (-cos));
        canvas.drawPath(this.path, clearPaint);
        canvas.translate((float) sin, (float) cos);
    }

    @Deprecated
    public void getPathForSize(Rect rect, Path path2) {
        calculatePathForSize(new RectF(rect), path2);
    }

    private void calculatePathForSize(RectF rectF2, Path path2) {
        this.pathProvider.calculatePath(this.drawableState.shapeAppearanceModel, this.drawableState.interpolation, rectF2, this.pathShadowListener, path2);
    }

    private void calculateStrokePath() {
        this.strokeShapeAppearance = new ShapeAppearanceModel(getShapeAppearanceModel());
        this.strokeShapeAppearance.setCornerRadii(adjustCornerSizeForStrokeSize(this.strokeShapeAppearance.getTopLeftCorner().cornerSize), adjustCornerSizeForStrokeSize(this.strokeShapeAppearance.getTopRightCorner().cornerSize), adjustCornerSizeForStrokeSize(this.strokeShapeAppearance.getBottomRightCorner().cornerSize), adjustCornerSizeForStrokeSize(this.strokeShapeAppearance.getBottomLeftCorner().cornerSize));
        this.pathProvider.calculatePath(this.strokeShapeAppearance, this.drawableState.interpolation, getBoundsInsetByStroke(), this.pathInsetByStroke);
    }

    private float adjustCornerSizeForStrokeSize(float f) {
        return Math.max(f - getStrokeInsetLength(), 0.0f);
    }

    public void getOutline(Outline outline) {
        if (this.drawableState.shadowCompatMode != 2) {
            if (this.drawableState.shapeAppearanceModel.isRoundRect()) {
                outline.setRoundRect(getBounds(), this.drawableState.shapeAppearanceModel.getTopLeftCorner().getCornerSize());
                return;
            }
            calculatePath(getBoundsAsRectF(), this.path);
            if (this.path.isConvex()) {
                outline.setConvexPath(this.path);
            }
        }
    }

    private void calculatePath(RectF rectF2, Path path2) {
        calculatePathForSize(rectF2, path2);
        if (this.drawableState.scale != 1.0f) {
            this.matrix.reset();
            this.matrix.setScale(this.drawableState.scale, this.drawableState.scale, rectF2.width() / 2.0f, rectF2.height() / 2.0f);
            path2.transform(this.matrix);
        }
    }

    private void updateTintFilter() {
        this.tintFilter = calculateTintFilter(this.drawableState.tintList, this.drawableState.tintMode);
        this.strokeTintFilter = calculateTintFilter(this.drawableState.strokeTintList, this.drawableState.tintMode);
        if (this.drawableState.useTintColorForShadow) {
            this.shadowRenderer.setShadowColor(this.drawableState.tintList.getColorForState(getState(), 0));
        }
    }

    private PorterDuffColorFilter calculateTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    public boolean isStateful() {
        return super.isStateful() || (this.drawableState.tintList != null && this.drawableState.tintList.isStateful()) || ((this.drawableState.strokeTintList != null && this.drawableState.strokeTintList.isStateful()) || ((this.drawableState.strokeColor != null && this.drawableState.strokeColor.isStateful()) || (this.drawableState.fillColor != null && this.drawableState.fillColor.isStateful())));
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        updateColorsForState(iArr, onStateChange);
        updateTintFilter();
        return onStateChange;
    }

    private boolean updateColorsForState(int[] iArr, boolean z) {
        int color;
        int colorForState;
        int color2;
        int colorForState2;
        if (!(this.drawableState.fillColor == null || (color2 = this.fillPaint.getColor()) == (colorForState2 = this.drawableState.fillColor.getColorForState(iArr, color2)))) {
            this.fillPaint.setColor(colorForState2);
            z = true;
        }
        if (this.drawableState.strokeColor == null || (color = this.strokePaint.getColor()) == (colorForState = this.drawableState.strokeColor.getColorForState(iArr, color))) {
            return z;
        }
        this.strokePaint.setColor(colorForState);
        return true;
    }

    private float getStrokeInsetLength() {
        if (hasStroke()) {
            return this.strokePaint.getStrokeWidth() / 2.0f;
        }
        return 0.0f;
    }

    private RectF getBoundsInsetByStroke() {
        RectF boundsAsRectF = getBoundsAsRectF();
        float strokeInsetLength = getStrokeInsetLength();
        this.insetRectF.set(boundsAsRectF.left + strokeInsetLength, boundsAsRectF.top + strokeInsetLength, boundsAsRectF.right - strokeInsetLength, boundsAsRectF.bottom - strokeInsetLength);
        return this.insetRectF;
    }

    static final class MaterialShapeDrawableState extends Drawable.ConstantState {
        public int alpha = 255;
        public ColorFilter colorFilter;
        public ColorStateList fillColor = null;
        public float interpolation = 1.0f;
        public Paint.Style paintStyle = Paint.Style.FILL_AND_STROKE;
        public float scale = 1.0f;
        public int shadowCompatElevation = 0;
        public int shadowCompatMode = 0;
        public int shadowCompatOffset = 0;
        public int shadowCompatRadius = 0;
        public int shadowCompatRotation = 0;
        public ShapeAppearanceModel shapeAppearanceModel;
        public ColorStateList strokeColor = null;
        public ColorStateList strokeTintList = null;
        public float strokeWidth;
        public ColorStateList tintList = null;
        public PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;
        public boolean useTintColorForShadow = false;

        public int getChangingConfigurations() {
            return 0;
        }

        public MaterialShapeDrawableState(ShapeAppearanceModel shapeAppearanceModel2) {
            this.shapeAppearanceModel = shapeAppearanceModel2;
        }

        public MaterialShapeDrawableState(MaterialShapeDrawableState materialShapeDrawableState) {
            this.shapeAppearanceModel = new ShapeAppearanceModel(materialShapeDrawableState.shapeAppearanceModel);
            this.strokeWidth = materialShapeDrawableState.strokeWidth;
            this.colorFilter = materialShapeDrawableState.colorFilter;
            this.fillColor = materialShapeDrawableState.fillColor;
            this.strokeColor = materialShapeDrawableState.strokeColor;
            this.tintMode = materialShapeDrawableState.tintMode;
            this.tintList = materialShapeDrawableState.tintList;
            this.alpha = materialShapeDrawableState.alpha;
            this.scale = materialShapeDrawableState.scale;
            this.shadowCompatOffset = materialShapeDrawableState.shadowCompatOffset;
            this.shadowCompatMode = materialShapeDrawableState.shadowCompatMode;
            this.useTintColorForShadow = materialShapeDrawableState.useTintColorForShadow;
            this.interpolation = materialShapeDrawableState.interpolation;
            this.shadowCompatElevation = materialShapeDrawableState.shadowCompatElevation;
            this.shadowCompatRadius = materialShapeDrawableState.shadowCompatRadius;
            this.shadowCompatRotation = materialShapeDrawableState.shadowCompatRotation;
            this.strokeTintList = materialShapeDrawableState.strokeTintList;
            this.paintStyle = materialShapeDrawableState.paintStyle;
        }

        public Drawable newDrawable() {
            return new MaterialShapeDrawable(this);
        }
    }
}
