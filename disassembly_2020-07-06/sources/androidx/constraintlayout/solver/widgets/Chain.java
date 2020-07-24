package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        ChainHead[] chainHeadArr;
        int i3;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i2 = i4;
            i3 = 0;
        } else {
            i3 = 2;
            int i5 = constraintWidgetContainer.mVerticalChainsSize;
            i2 = i5;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i6 = 0; i6 < i2; i6++) {
            ChainHead chainHead = chainHeadArr[i6];
            chainHead.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i3, chainHead)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
        if (r2.mHorizontalChainStyle == 2) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0048, code lost:
        if (r2.mVerticalChainStyle == 2) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004c, code lost:
        r5 = false;
     */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x038f  */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x03a8  */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x03ab  */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x03b1  */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x0483  */
    /* JADX WARNING: Removed duplicated region for block: B:256:0x04b8  */
    /* JADX WARNING: Removed duplicated region for block: B:265:0x04dd  */
    /* JADX WARNING: Removed duplicated region for block: B:266:0x04e2  */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x04e8  */
    /* JADX WARNING: Removed duplicated region for block: B:270:0x04ed  */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x04f1  */
    /* JADX WARNING: Removed duplicated region for block: B:278:0x0503  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x0390 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x015b  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0188  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0196  */
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        SolverVariable solverVariable;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        ConstraintAnchor constraintAnchor3;
        int i3;
        ConstraintWidget constraintWidget2;
        int i4;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3;
        ConstraintAnchor constraintAnchor4;
        ConstraintWidget constraintWidget3;
        ConstraintWidget constraintWidget4;
        SolverVariable solverVariable4;
        SolverVariable solverVariable5;
        ConstraintAnchor constraintAnchor5;
        float f;
        int size;
        int i5;
        ArrayList<ConstraintWidget> arrayList;
        int i6;
        float f2;
        boolean z3;
        int i7;
        ConstraintWidget constraintWidget5;
        boolean z4;
        int i8;
        ConstraintWidgetContainer constraintWidgetContainer2 = constraintWidgetContainer;
        LinearSystem linearSystem2 = linearSystem;
        ChainHead chainHead2 = chainHead;
        ConstraintWidget constraintWidget6 = chainHead2.mFirst;
        ConstraintWidget constraintWidget7 = chainHead2.mLast;
        ConstraintWidget constraintWidget8 = chainHead2.mFirstVisibleWidget;
        ConstraintWidget constraintWidget9 = chainHead2.mLastVisibleWidget;
        ConstraintWidget constraintWidget10 = chainHead2.mHead;
        float f3 = chainHead2.mTotalWeight;
        ConstraintWidget constraintWidget11 = chainHead2.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget12 = chainHead2.mLastMatchConstraintWidget;
        boolean z5 = constraintWidgetContainer2.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (i == 0) {
            z2 = constraintWidget10.mHorizontalChainStyle == 0;
            z = constraintWidget10.mHorizontalChainStyle == 1;
        } else {
            z2 = constraintWidget10.mVerticalChainStyle == 0;
            z = constraintWidget10.mVerticalChainStyle == 1;
        }
        boolean z6 = true;
        boolean z7 = z2;
        ConstraintWidget constraintWidget13 = constraintWidget6;
        boolean z8 = z;
        boolean z9 = z6;
        boolean z10 = false;
        while (true) {
            ConstraintWidget constraintWidget14 = null;
            if (z10) {
                break;
            }
            ConstraintAnchor constraintAnchor6 = constraintWidget13.mListAnchors[i2];
            int i9 = (z5 || z9) ? 1 : 4;
            int margin = constraintAnchor6.getMargin();
            if (!(constraintAnchor6.mTarget == null || constraintWidget13 == constraintWidget6)) {
                margin += constraintAnchor6.mTarget.getMargin();
            }
            int i10 = margin;
            if (z9 && constraintWidget13 != constraintWidget6 && constraintWidget13 != constraintWidget8) {
                f2 = f3;
                z3 = z10;
                i7 = 6;
            } else if (!z7 || !z5) {
                f2 = f3;
                i7 = i9;
                z3 = z10;
            } else {
                f2 = f3;
                z3 = z10;
                i7 = 4;
            }
            if (constraintAnchor6.mTarget != null) {
                if (constraintWidget13 == constraintWidget8) {
                    z4 = z7;
                    constraintWidget5 = constraintWidget10;
                    linearSystem2.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i10, 5);
                } else {
                    constraintWidget5 = constraintWidget10;
                    z4 = z7;
                    linearSystem2.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i10, 6);
                }
                linearSystem2.addEquality(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i10, i7);
            } else {
                constraintWidget5 = constraintWidget10;
                z4 = z7;
            }
            if (z5) {
                if (constraintWidget13.getVisibility() == 8 || constraintWidget13.mListDimensionBehaviors[i] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    i8 = 0;
                } else {
                    i8 = 0;
                    linearSystem2.addGreaterThan(constraintWidget13.mListAnchors[i2 + 1].mSolverVariable, constraintWidget13.mListAnchors[i2].mSolverVariable, 0, 5);
                }
                linearSystem2.addGreaterThan(constraintWidget13.mListAnchors[i2].mSolverVariable, constraintWidgetContainer2.mListAnchors[i2].mSolverVariable, i8, 6);
            }
            ConstraintAnchor constraintAnchor7 = constraintWidget13.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor7 != null) {
                ConstraintWidget constraintWidget15 = constraintAnchor7.mOwner;
                if (constraintWidget15.mListAnchors[i2].mTarget != null && constraintWidget15.mListAnchors[i2].mTarget.mOwner == constraintWidget13) {
                    constraintWidget14 = constraintWidget15;
                }
            }
            if (constraintWidget14 != null) {
                constraintWidget13 = constraintWidget14;
                z10 = z3;
            } else {
                z10 = true;
            }
            f3 = f2;
            z7 = z4;
            constraintWidget10 = constraintWidget5;
        }
        ConstraintWidget constraintWidget16 = constraintWidget10;
        float f4 = f3;
        boolean z11 = z7;
        if (constraintWidget9 != null) {
            int i11 = i2 + 1;
            if (constraintWidget7.mListAnchors[i11].mTarget != null) {
                ConstraintAnchor constraintAnchor8 = constraintWidget9.mListAnchors[i11];
                linearSystem2.addLowerThan(constraintAnchor8.mSolverVariable, constraintWidget7.mListAnchors[i11].mTarget.mSolverVariable, -constraintAnchor8.getMargin(), 5);
                if (z5) {
                    int i12 = i2 + 1;
                    linearSystem2.addGreaterThan(constraintWidgetContainer2.mListAnchors[i12].mSolverVariable, constraintWidget7.mListAnchors[i12].mSolverVariable, constraintWidget7.mListAnchors[i12].getMargin(), 6);
                }
                ArrayList<ConstraintWidget> arrayList2 = chainHead2.mWeightedMatchConstraintsWidgets;
                if (arrayList2 != null && (size = arrayList2.size()) > 1) {
                    float f5 = (!chainHead2.mHasUndefinedWeights || chainHead2.mHasComplexMatchWeights) ? f4 : (float) chainHead2.mWidgetsMatchCount;
                    float f6 = 0.0f;
                    ConstraintWidget constraintWidget17 = null;
                    i5 = 0;
                    float f7 = 0.0f;
                    while (i5 < size) {
                        ConstraintWidget constraintWidget18 = arrayList2.get(i5);
                        float f8 = constraintWidget18.mWeight[i];
                        if (f8 < f6) {
                            if (chainHead2.mHasComplexMatchWeights) {
                                linearSystem2.addEquality(constraintWidget18.mListAnchors[i2 + 1].mSolverVariable, constraintWidget18.mListAnchors[i2].mSolverVariable, 0, 4);
                                arrayList = arrayList2;
                                i6 = size;
                                i5++;
                                size = i6;
                                arrayList2 = arrayList;
                                f6 = 0.0f;
                            } else {
                                f8 = 1.0f;
                            }
                        }
                        if (f8 == 0.0f) {
                            linearSystem2.addEquality(constraintWidget18.mListAnchors[i2 + 1].mSolverVariable, constraintWidget18.mListAnchors[i2].mSolverVariable, 0, 6);
                            arrayList = arrayList2;
                            i6 = size;
                            i5++;
                            size = i6;
                            arrayList2 = arrayList;
                            f6 = 0.0f;
                        } else {
                            if (constraintWidget17 != null) {
                                SolverVariable solverVariable6 = constraintWidget17.mListAnchors[i2].mSolverVariable;
                                int i13 = i2 + 1;
                                SolverVariable solverVariable7 = constraintWidget17.mListAnchors[i13].mSolverVariable;
                                SolverVariable solverVariable8 = constraintWidget18.mListAnchors[i2].mSolverVariable;
                                arrayList = arrayList2;
                                SolverVariable solverVariable9 = constraintWidget18.mListAnchors[i13].mSolverVariable;
                                i6 = size;
                                ArrayRow createRow = linearSystem.createRow();
                                createRow.createRowEqualMatchDimensions(f7, f5, f8, solverVariable6, solverVariable7, solverVariable8, solverVariable9);
                                linearSystem2.addConstraint(createRow);
                            } else {
                                arrayList = arrayList2;
                                i6 = size;
                            }
                            f7 = f8;
                            constraintWidget17 = constraintWidget18;
                            i5++;
                            size = i6;
                            arrayList2 = arrayList;
                            f6 = 0.0f;
                        }
                    }
                }
                if (constraintWidget8 == null && (constraintWidget8 == constraintWidget9 || z9)) {
                    ConstraintAnchor constraintAnchor9 = constraintWidget6.mListAnchors[i2];
                    int i14 = i2 + 1;
                    ConstraintAnchor constraintAnchor10 = constraintWidget7.mListAnchors[i14];
                    SolverVariable solverVariable10 = constraintWidget6.mListAnchors[i2].mTarget != null ? constraintWidget6.mListAnchors[i2].mTarget.mSolverVariable : null;
                    SolverVariable solverVariable11 = constraintWidget7.mListAnchors[i14].mTarget != null ? constraintWidget7.mListAnchors[i14].mTarget.mSolverVariable : null;
                    if (constraintWidget8 == constraintWidget9) {
                        constraintAnchor9 = constraintWidget8.mListAnchors[i2];
                        constraintAnchor10 = constraintWidget8.mListAnchors[i14];
                    }
                    if (!(solverVariable10 == null || solverVariable11 == null)) {
                        if (i == 0) {
                            f = constraintWidget16.mHorizontalBiasPercent;
                        } else {
                            f = constraintWidget16.mVerticalBiasPercent;
                        }
                        linearSystem.addCentering(constraintAnchor9.mSolverVariable, solverVariable10, constraintAnchor9.getMargin(), f, solverVariable11, constraintAnchor10.mSolverVariable, constraintAnchor10.getMargin(), 5);
                    }
                } else if (z11 || constraintWidget8 == null) {
                    int i15 = 8;
                    if (z8 && constraintWidget8 != null) {
                        boolean z12 = chainHead2.mWidgetsMatchCount > 0 && chainHead2.mWidgetsCount == chainHead2.mWidgetsMatchCount;
                        constraintWidget = constraintWidget8;
                        ConstraintWidget constraintWidget19 = constraintWidget;
                        while (constraintWidget != null) {
                            ConstraintWidget constraintWidget20 = constraintWidget.mNextChainWidget[i];
                            while (constraintWidget20 != null && constraintWidget20.getVisibility() == i15) {
                                constraintWidget20 = constraintWidget20.mNextChainWidget[i];
                            }
                            if (constraintWidget == constraintWidget8 || constraintWidget == constraintWidget9 || constraintWidget20 == null) {
                                constraintWidget2 = constraintWidget19;
                                i4 = 8;
                            } else {
                                ConstraintWidget constraintWidget21 = constraintWidget20 == constraintWidget9 ? null : constraintWidget20;
                                ConstraintAnchor constraintAnchor11 = constraintWidget.mListAnchors[i2];
                                SolverVariable solverVariable12 = constraintAnchor11.mSolverVariable;
                                if (constraintAnchor11.mTarget != null) {
                                    SolverVariable solverVariable13 = constraintAnchor11.mTarget.mSolverVariable;
                                }
                                int i16 = i2 + 1;
                                SolverVariable solverVariable14 = constraintWidget19.mListAnchors[i16].mSolverVariable;
                                int margin2 = constraintAnchor11.getMargin();
                                int margin3 = constraintWidget.mListAnchors[i16].getMargin();
                                if (constraintWidget21 != null) {
                                    constraintAnchor4 = constraintWidget21.mListAnchors[i2];
                                    solverVariable3 = constraintAnchor4.mSolverVariable;
                                    solverVariable2 = constraintAnchor4.mTarget != null ? constraintAnchor4.mTarget.mSolverVariable : null;
                                } else {
                                    constraintAnchor4 = constraintWidget.mListAnchors[i16].mTarget;
                                    solverVariable3 = constraintAnchor4 != null ? constraintAnchor4.mSolverVariable : null;
                                    solverVariable2 = constraintWidget.mListAnchors[i16].mSolverVariable;
                                }
                                if (constraintAnchor4 != null) {
                                    margin3 += constraintAnchor4.getMargin();
                                }
                                int i17 = margin3;
                                if (constraintWidget19 != null) {
                                    margin2 += constraintWidget19.mListAnchors[i16].getMargin();
                                }
                                int i18 = margin2;
                                int i19 = z12 ? 6 : 4;
                                if (solverVariable12 == null || solverVariable14 == null || solverVariable3 == null || solverVariable2 == null) {
                                    constraintWidget3 = constraintWidget21;
                                    constraintWidget2 = constraintWidget19;
                                    i4 = 8;
                                } else {
                                    constraintWidget3 = constraintWidget21;
                                    int i20 = i17;
                                    constraintWidget2 = constraintWidget19;
                                    i4 = 8;
                                    linearSystem.addCentering(solverVariable12, solverVariable14, i18, 0.5f, solverVariable3, solverVariable2, i20, i19);
                                }
                                constraintWidget20 = constraintWidget3;
                            }
                            if (constraintWidget.getVisibility() == i4) {
                                constraintWidget = constraintWidget2;
                            }
                            constraintWidget19 = constraintWidget;
                            i15 = 8;
                            constraintWidget = constraintWidget20;
                        }
                        ConstraintAnchor constraintAnchor12 = constraintWidget8.mListAnchors[i2];
                        constraintAnchor = constraintWidget6.mListAnchors[i2].mTarget;
                        int i21 = i2 + 1;
                        constraintAnchor2 = constraintWidget9.mListAnchors[i21];
                        constraintAnchor3 = constraintWidget7.mListAnchors[i21].mTarget;
                        if (constraintAnchor != null) {
                            i3 = 5;
                        } else if (constraintWidget8 != constraintWidget9) {
                            i3 = 5;
                            linearSystem2.addEquality(constraintAnchor12.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor12.getMargin(), 5);
                        } else {
                            i3 = 5;
                            if (constraintAnchor3 != null) {
                                linearSystem.addCentering(constraintAnchor12.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor12.getMargin(), 0.5f, constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, constraintAnchor2.getMargin(), 5);
                            }
                        }
                        if (!(constraintAnchor3 == null || constraintWidget8 == constraintWidget9)) {
                            linearSystem2.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
                        }
                    }
                } else {
                    boolean z13 = chainHead2.mWidgetsMatchCount > 0 && chainHead2.mWidgetsCount == chainHead2.mWidgetsMatchCount;
                    ConstraintWidget constraintWidget22 = constraintWidget8;
                    ConstraintWidget constraintWidget23 = constraintWidget22;
                    while (constraintWidget22 != null) {
                        ConstraintWidget constraintWidget24 = constraintWidget22.mNextChainWidget[i];
                        while (true) {
                            if (constraintWidget24 != null) {
                                if (constraintWidget24.getVisibility() != 8) {
                                    break;
                                }
                                constraintWidget24 = constraintWidget24.mNextChainWidget[i];
                            } else {
                                break;
                            }
                        }
                        if (constraintWidget24 != null || constraintWidget22 == constraintWidget9) {
                            ConstraintAnchor constraintAnchor13 = constraintWidget22.mListAnchors[i2];
                            SolverVariable solverVariable15 = constraintAnchor13.mSolverVariable;
                            SolverVariable solverVariable16 = constraintAnchor13.mTarget != null ? constraintAnchor13.mTarget.mSolverVariable : null;
                            if (constraintWidget23 != constraintWidget22) {
                                solverVariable16 = constraintWidget23.mListAnchors[i2 + 1].mSolverVariable;
                            } else if (constraintWidget22 == constraintWidget8 && constraintWidget23 == constraintWidget22) {
                                solverVariable16 = constraintWidget6.mListAnchors[i2].mTarget != null ? constraintWidget6.mListAnchors[i2].mTarget.mSolverVariable : null;
                            }
                            int margin4 = constraintAnchor13.getMargin();
                            int i22 = i2 + 1;
                            int margin5 = constraintWidget22.mListAnchors[i22].getMargin();
                            if (constraintWidget24 != null) {
                                constraintAnchor5 = constraintWidget24.mListAnchors[i2];
                                solverVariable5 = constraintAnchor5.mSolverVariable;
                                solverVariable4 = constraintWidget22.mListAnchors[i22].mSolverVariable;
                            } else {
                                constraintAnchor5 = constraintWidget7.mListAnchors[i22].mTarget;
                                solverVariable5 = constraintAnchor5 != null ? constraintAnchor5.mSolverVariable : null;
                                solverVariable4 = constraintWidget22.mListAnchors[i22].mSolverVariable;
                            }
                            if (constraintAnchor5 != null) {
                                margin5 += constraintAnchor5.getMargin();
                            }
                            if (constraintWidget23 != null) {
                                margin4 += constraintWidget23.mListAnchors[i22].getMargin();
                            }
                            if (!(solverVariable15 == null || solverVariable16 == null || solverVariable5 == null || solverVariable4 == null)) {
                                if (constraintWidget22 == constraintWidget8) {
                                    margin4 = constraintWidget8.mListAnchors[i2].getMargin();
                                }
                                int i23 = margin4;
                                int margin6 = constraintWidget22 == constraintWidget9 ? constraintWidget9.mListAnchors[i22].getMargin() : margin5;
                                int i24 = i23;
                                SolverVariable solverVariable17 = solverVariable5;
                                SolverVariable solverVariable18 = solverVariable4;
                                int i25 = margin6;
                                constraintWidget4 = constraintWidget24;
                                linearSystem.addCentering(solverVariable15, solverVariable16, i24, 0.5f, solverVariable17, solverVariable18, i25, z13 ? 6 : 4);
                                if (constraintWidget22.getVisibility() == 8) {
                                    constraintWidget23 = constraintWidget22;
                                }
                                constraintWidget22 = constraintWidget4;
                            }
                        }
                        constraintWidget4 = constraintWidget24;
                        if (constraintWidget22.getVisibility() == 8) {
                        }
                        constraintWidget22 = constraintWidget4;
                    }
                }
                if ((!z11 || z8) && constraintWidget8 != null) {
                    ConstraintAnchor constraintAnchor14 = constraintWidget8.mListAnchors[i2];
                    int i26 = i2 + 1;
                    ConstraintAnchor constraintAnchor15 = constraintWidget9.mListAnchors[i26];
                    solverVariable = constraintAnchor14.mTarget == null ? constraintAnchor14.mTarget.mSolverVariable : null;
                    SolverVariable solverVariable19 = constraintAnchor15.mTarget == null ? constraintAnchor15.mTarget.mSolverVariable : null;
                    if (constraintWidget7 != constraintWidget9) {
                        ConstraintAnchor constraintAnchor16 = constraintWidget7.mListAnchors[i26];
                        solverVariable19 = constraintAnchor16.mTarget != null ? constraintAnchor16.mTarget.mSolverVariable : null;
                    }
                    SolverVariable solverVariable20 = solverVariable19;
                    if (constraintWidget8 == constraintWidget9) {
                        constraintAnchor14 = constraintWidget8.mListAnchors[i2];
                        constraintAnchor15 = constraintWidget8.mListAnchors[i26];
                    }
                    if (solverVariable != null && solverVariable20 != null) {
                        int margin7 = constraintAnchor14.getMargin();
                        if (constraintWidget9 != null) {
                            constraintWidget7 = constraintWidget9;
                        }
                        linearSystem.addCentering(constraintAnchor14.mSolverVariable, solverVariable, margin7, 0.5f, solverVariable20, constraintAnchor15.mSolverVariable, constraintWidget7.mListAnchors[i26].getMargin(), 5);
                        return;
                    }
                }
                return;
            }
        }
        if (z5) {
        }
        ArrayList<ConstraintWidget> arrayList22 = chainHead2.mWeightedMatchConstraintsWidgets;
        if (!chainHead2.mHasUndefinedWeights || chainHead2.mHasComplexMatchWeights) {
        }
        float f62 = 0.0f;
        ConstraintWidget constraintWidget172 = null;
        i5 = 0;
        float f72 = 0.0f;
        while (i5 < size) {
        }
        if (constraintWidget8 == null) {
        }
        if (z11) {
        }
        int i152 = 8;
        if (chainHead2.mWidgetsMatchCount > 0 || chainHead2.mWidgetsCount == chainHead2.mWidgetsMatchCount) {
        }
        constraintWidget = constraintWidget8;
        ConstraintWidget constraintWidget192 = constraintWidget;
        while (constraintWidget != null) {
        }
        ConstraintAnchor constraintAnchor122 = constraintWidget8.mListAnchors[i2];
        constraintAnchor = constraintWidget6.mListAnchors[i2].mTarget;
        int i212 = i2 + 1;
        constraintAnchor2 = constraintWidget9.mListAnchors[i212];
        constraintAnchor3 = constraintWidget7.mListAnchors[i212].mTarget;
        if (constraintAnchor != null) {
        }
        linearSystem2.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
        if (!z11) {
        }
        ConstraintAnchor constraintAnchor142 = constraintWidget8.mListAnchors[i2];
        int i262 = i2 + 1;
        ConstraintAnchor constraintAnchor152 = constraintWidget9.mListAnchors[i262];
        if (constraintAnchor142.mTarget == null) {
        }
        if (constraintAnchor152.mTarget == null) {
        }
        if (constraintWidget7 != constraintWidget9) {
        }
        SolverVariable solverVariable202 = solverVariable19;
        if (constraintWidget8 == constraintWidget9) {
        }
        if (solverVariable != null) {
        }
    }
}
