package com.google.android.material.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import com.google.android.material.R;

public class ShapeAppearanceModel {
    private EdgeTreatment bottomEdge;
    private CornerTreatment bottomLeftCorner;
    private CornerTreatment bottomRightCorner;
    private EdgeTreatment leftEdge;
    private EdgeTreatment rightEdge;
    private EdgeTreatment topEdge;
    private CornerTreatment topLeftCorner;
    private CornerTreatment topRightCorner;

    public ShapeAppearanceModel() {
        setTopLeftCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setTopRightCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setBottomRightCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setBottomLeftCorner(MaterialShapeUtils.createDefaultCornerTreatment());
        setTopEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setRightEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setBottomEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setLeftEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
    }

    public ShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.topLeftCorner = shapeAppearanceModel.getTopLeftCorner().clone();
        this.topRightCorner = shapeAppearanceModel.getTopRightCorner().clone();
        this.bottomRightCorner = shapeAppearanceModel.getBottomRightCorner().clone();
        this.bottomLeftCorner = shapeAppearanceModel.getBottomLeftCorner().clone();
        this.topEdge = shapeAppearanceModel.getTopEdge().clone();
        this.rightEdge = shapeAppearanceModel.getRightEdge().clone();
        this.leftEdge = shapeAppearanceModel.getLeftEdge().clone();
        this.bottomEdge = shapeAppearanceModel.getBottomEdge().clone();
    }

    public ShapeAppearanceModel(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i, i2, 0);
    }

    public ShapeAppearanceModel(Context context, AttributeSet attributeSet, int i, int i2, int i3) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MaterialShape, i, i2);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.MaterialShape_shapeAppearance, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.MaterialShape_shapeAppearanceOverlay, 0);
        obtainStyledAttributes.recycle();
        if (resourceId2 != 0) {
            context = new ContextThemeWrapper(context, resourceId);
            resourceId = resourceId2;
        }
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, R.styleable.ShapeAppearance);
        int i4 = obtainStyledAttributes2.getInt(R.styleable.ShapeAppearance_cornerFamily, 0);
        int i5 = obtainStyledAttributes2.getInt(R.styleable.ShapeAppearance_cornerFamilyTopLeft, i4);
        int i6 = obtainStyledAttributes2.getInt(R.styleable.ShapeAppearance_cornerFamilyTopRight, i4);
        int i7 = obtainStyledAttributes2.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomRight, i4);
        int i8 = obtainStyledAttributes2.getInt(R.styleable.ShapeAppearance_cornerFamilyBottomLeft, i4);
        int dimensionPixelSize = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSize, i3);
        int dimensionPixelSize2 = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeTopLeft, dimensionPixelSize);
        int dimensionPixelSize3 = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeTopRight, dimensionPixelSize);
        int dimensionPixelSize4 = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeBottomRight, dimensionPixelSize);
        int dimensionPixelSize5 = obtainStyledAttributes2.getDimensionPixelSize(R.styleable.ShapeAppearance_cornerSizeBottomLeft, dimensionPixelSize);
        setTopLeftCorner(i5, dimensionPixelSize2);
        setTopRightCorner(i6, dimensionPixelSize3);
        setBottomRightCorner(i7, dimensionPixelSize4);
        setBottomLeftCorner(i8, dimensionPixelSize5);
        setTopEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setRightEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setBottomEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        setLeftEdge(MaterialShapeUtils.createDefaultEdgeTreatment());
        obtainStyledAttributes2.recycle();
    }

    public void setAllCorners(CornerTreatment cornerTreatment) {
        this.topLeftCorner = cornerTreatment.clone();
        this.topRightCorner = cornerTreatment.clone();
        this.bottomRightCorner = cornerTreatment.clone();
        this.bottomLeftCorner = cornerTreatment.clone();
    }

    public void setAllCorners(int i, int i2) {
        setAllCorners(MaterialShapeUtils.createCornerTreatment(i, i2));
    }

    public void setCornerRadius(float f) {
        this.topLeftCorner.setCornerSize(f);
        this.topRightCorner.setCornerSize(f);
        this.bottomRightCorner.setCornerSize(f);
        this.bottomLeftCorner.setCornerSize(f);
    }

    public void setCornerRadii(float f, float f2, float f3, float f4) {
        this.topLeftCorner.setCornerSize(f);
        this.topRightCorner.setCornerSize(f2);
        this.bottomRightCorner.setCornerSize(f3);
        this.bottomLeftCorner.setCornerSize(f4);
    }

    public void setAllEdges(EdgeTreatment edgeTreatment) {
        this.leftEdge = edgeTreatment.clone();
        this.topEdge = edgeTreatment.clone();
        this.rightEdge = edgeTreatment.clone();
        this.bottomEdge = edgeTreatment.clone();
    }

    public void setCornerTreatments(CornerTreatment cornerTreatment, CornerTreatment cornerTreatment2, CornerTreatment cornerTreatment3, CornerTreatment cornerTreatment4) {
        this.topLeftCorner = cornerTreatment;
        this.topRightCorner = cornerTreatment2;
        this.bottomRightCorner = cornerTreatment3;
        this.bottomLeftCorner = cornerTreatment4;
    }

    public void setEdgeTreatments(EdgeTreatment edgeTreatment, EdgeTreatment edgeTreatment2, EdgeTreatment edgeTreatment3, EdgeTreatment edgeTreatment4) {
        this.leftEdge = edgeTreatment;
        this.topEdge = edgeTreatment2;
        this.rightEdge = edgeTreatment3;
        this.bottomEdge = edgeTreatment4;
    }

    public void setTopLeftCorner(int i, int i2) {
        setTopLeftCorner(MaterialShapeUtils.createCornerTreatment(i, i2));
    }

    public void setTopLeftCorner(CornerTreatment cornerTreatment) {
        this.topLeftCorner = cornerTreatment;
    }

    public CornerTreatment getTopLeftCorner() {
        return this.topLeftCorner;
    }

    public void setTopRightCorner(int i, int i2) {
        setTopRightCorner(MaterialShapeUtils.createCornerTreatment(i, i2));
    }

    public void setTopRightCorner(CornerTreatment cornerTreatment) {
        this.topRightCorner = cornerTreatment;
    }

    public CornerTreatment getTopRightCorner() {
        return this.topRightCorner;
    }

    public void setBottomRightCorner(int i, int i2) {
        setBottomRightCorner(MaterialShapeUtils.createCornerTreatment(i, i2));
    }

    public void setBottomRightCorner(CornerTreatment cornerTreatment) {
        this.bottomRightCorner = cornerTreatment;
    }

    public CornerTreatment getBottomRightCorner() {
        return this.bottomRightCorner;
    }

    public void setBottomLeftCorner(int i, int i2) {
        setBottomLeftCorner(MaterialShapeUtils.createCornerTreatment(i, i2));
    }

    public void setBottomLeftCorner(CornerTreatment cornerTreatment) {
        this.bottomLeftCorner = cornerTreatment;
    }

    public CornerTreatment getBottomLeftCorner() {
        return this.bottomLeftCorner;
    }

    public void setTopEdge(EdgeTreatment edgeTreatment) {
        this.topEdge = edgeTreatment;
    }

    public EdgeTreatment getTopEdge() {
        return this.topEdge;
    }

    public void setRightEdge(EdgeTreatment edgeTreatment) {
        this.rightEdge = edgeTreatment;
    }

    public EdgeTreatment getRightEdge() {
        return this.rightEdge;
    }

    public void setBottomEdge(EdgeTreatment edgeTreatment) {
        this.bottomEdge = edgeTreatment;
    }

    public EdgeTreatment getBottomEdge() {
        return this.bottomEdge;
    }

    public void setLeftEdge(EdgeTreatment edgeTreatment) {
        this.leftEdge = edgeTreatment;
    }

    public EdgeTreatment getLeftEdge() {
        return this.leftEdge;
    }

    public boolean isRoundRect() {
        boolean z = this.leftEdge.getClass().equals(EdgeTreatment.class) && this.rightEdge.getClass().equals(EdgeTreatment.class) && this.topEdge.getClass().equals(EdgeTreatment.class) && this.bottomEdge.getClass().equals(EdgeTreatment.class);
        float cornerSize = this.topLeftCorner.getCornerSize();
        boolean z2 = this.topRightCorner.getCornerSize() == cornerSize && this.bottomLeftCorner.getCornerSize() == cornerSize && this.bottomRightCorner.getCornerSize() == cornerSize;
        boolean z3 = (this.topRightCorner instanceof RoundedCornerTreatment) && (this.topLeftCorner instanceof RoundedCornerTreatment) && (this.bottomRightCorner instanceof RoundedCornerTreatment) && (this.bottomLeftCorner instanceof RoundedCornerTreatment);
        if (!z || !z2 || !z3) {
            return false;
        }
        return true;
    }
}
