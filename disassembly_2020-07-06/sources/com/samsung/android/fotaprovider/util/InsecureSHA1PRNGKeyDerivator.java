package com.samsung.android.fotaprovider.util;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;

public class InsecureSHA1PRNGKeyDerivator {
    private static final int BYTES_OFFSET = 81;
    private static final int COUNTER_BASE = 0;
    private static final int DIGEST_LENGTH = 20;
    private static final int[] END_FLAGS = {Integer.MIN_VALUE, 8388608, 32768, 128};
    private static final int EXTRAFRAME_OFFSET = 5;
    private static final int FRAME_LENGTH = 16;
    private static final int FRAME_OFFSET = 21;
    private static final int H0 = 1732584193;
    private static final int H1 = -271733879;
    private static final int H2 = -1732584194;
    private static final int H3 = 271733878;
    private static final int H4 = -1009589776;
    private static final int HASHBYTES_TO_USE = 20;
    private static final int HASHCOPY_OFFSET = 0;
    private static final int HASH_OFFSET = 82;
    private static final int[] LEFT = {0, 24, 16, 8};
    private static final int[] MASK = {-1, ViewCompat.MEASURED_SIZE_MASK, SupportMenu.USER_MASK, 255};
    private static final int MAX_BYTES = 48;
    private static final int NEXT_BYTES = 2;
    private static final int[] RIGHT1 = {0, 40, 48, 56};
    private static final int[] RIGHT2 = {0, 8, 16, 24};
    private static final int SET_SEED = 1;
    private static final int UNDEFINED = 0;
    private transient int[] copies;
    private transient long counter;
    private transient int nextBIndex;
    private transient byte[] nextBytes;
    private transient int[] seed = new int[87];
    private transient long seedLength;
    private transient int state;

    public static byte[] deriveInsecureKey(byte[] bArr, int i) {
        InsecureSHA1PRNGKeyDerivator insecureSHA1PRNGKeyDerivator = new InsecureSHA1PRNGKeyDerivator();
        insecureSHA1PRNGKeyDerivator.setSeed(bArr);
        byte[] bArr2 = new byte[i];
        insecureSHA1PRNGKeyDerivator.nextBytes(bArr2);
        return bArr2;
    }

    private InsecureSHA1PRNGKeyDerivator() {
        int[] iArr = this.seed;
        iArr[82] = H0;
        iArr[83] = H1;
        iArr[84] = H2;
        iArr[85] = H3;
        iArr[86] = H4;
        this.seedLength = 0;
        this.copies = new int[37];
        this.nextBytes = new byte[20];
        this.nextBIndex = 20;
        this.counter = 0;
        this.state = 0;
    }

    private void updateSeed(byte[] bArr) {
        updateHash(this.seed, bArr, 0, bArr.length - 1);
        this.seedLength += (long) bArr.length;
    }

    private synchronized void setSeed(byte[] bArr) {
        if (bArr != null) {
            if (this.state == 2) {
                System.arraycopy(this.copies, 0, this.seed, 82, 5);
            }
            this.state = 1;
            if (bArr.length != 0) {
                updateSeed(bArr);
            }
        } else {
            throw new IllegalArgumentException("seed == null");
        }
    }

    private synchronized void nextBytes(byte[] bArr) {
        int i;
        byte[] bArr2 = bArr;
        synchronized (this) {
            if (bArr2 != null) {
                try {
                    int i2 = this.seed[81] == 0 ? 0 : (this.seed[81] + 7) >> 2;
                    if (this.state != 0) {
                        char c = ' ';
                        int i3 = 48;
                        if (this.state == 1) {
                            System.arraycopy(this.seed, 82, this.copies, 0, 5);
                            for (int i4 = i2 + 3; i4 < 18; i4++) {
                                this.seed[i4] = 0;
                            }
                            long j = (this.seedLength << 3) + 64;
                            if (this.seed[81] < 48) {
                                this.seed[14] = (int) (j >>> 32);
                                this.seed[15] = (int) (j & -1);
                            } else {
                                this.copies[19] = (int) (j >>> 32);
                                this.copies[20] = (int) (j & -1);
                            }
                            this.nextBIndex = 20;
                        }
                        this.state = 2;
                        if (bArr2.length != 0) {
                            int length = 20 - this.nextBIndex < bArr2.length - 0 ? 20 - this.nextBIndex : bArr2.length - 0;
                            if (length > 0) {
                                System.arraycopy(this.nextBytes, this.nextBIndex, bArr2, 0, length);
                                this.nextBIndex += length;
                                i = length + 0;
                            } else {
                                i = 0;
                            }
                            if (i < bArr2.length) {
                                int i5 = this.seed[81] & 3;
                                while (true) {
                                    if (i5 == 0) {
                                        this.seed[i2] = (int) (this.counter >>> c);
                                        this.seed[i2 + 1] = (int) (this.counter & -1);
                                        this.seed[i2 + 2] = END_FLAGS[0];
                                    } else {
                                        int[] iArr = this.seed;
                                        iArr[i2] = ((int) (((long) MASK[i5]) & (this.counter >>> RIGHT1[i5]))) | iArr[i2];
                                        this.seed[i2 + 1] = (int) ((this.counter >>> RIGHT2[i5]) & -1);
                                        this.seed[i2 + 2] = (int) ((this.counter << LEFT[i5]) | ((long) END_FLAGS[i5]));
                                    }
                                    if (this.seed[81] > i3) {
                                        this.copies[5] = this.seed[16];
                                        this.copies[6] = this.seed[17];
                                    }
                                    computeHash(this.seed);
                                    if (this.seed[81] > i3) {
                                        System.arraycopy(this.seed, 0, this.copies, 21, 16);
                                        System.arraycopy(this.copies, 5, this.seed, 0, 16);
                                        computeHash(this.seed);
                                        System.arraycopy(this.copies, 21, this.seed, 0, 16);
                                    }
                                    this.counter++;
                                    int i6 = 0;
                                    for (int i7 = 0; i7 < 5; i7++) {
                                        int i8 = this.seed[i7 + 82];
                                        this.nextBytes[i6] = (byte) (i8 >>> 24);
                                        this.nextBytes[i6 + 1] = (byte) (i8 >>> 16);
                                        this.nextBytes[i6 + 2] = (byte) (i8 >>> 8);
                                        this.nextBytes[i6 + 3] = (byte) i8;
                                        i6 += 4;
                                    }
                                    this.nextBIndex = 0;
                                    int length2 = 20 < bArr2.length - i ? 20 : bArr2.length - i;
                                    if (length2 > 0) {
                                        System.arraycopy(this.nextBytes, 0, bArr2, i, length2);
                                        i += length2;
                                        this.nextBIndex += length2;
                                    }
                                    if (i < bArr2.length) {
                                        c = ' ';
                                        i3 = 48;
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                    } else {
                        throw new IllegalStateException("No seed supplied!");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                throw new IllegalArgumentException("bytes == null");
            }
        }
    }

    private static void computeHash(int[] iArr) {
        int i;
        int i2;
        int i3;
        int i4 = iArr[82];
        int i5 = iArr[83];
        int i6 = iArr[84];
        int i7 = iArr[85];
        int i8 = iArr[86];
        for (int i9 = 16; i9 < 80; i9++) {
            int i10 = ((iArr[i9 - 3] ^ iArr[i9 - 8]) ^ iArr[i9 - 14]) ^ iArr[i9 - 16];
            iArr[i9] = (i10 >>> 31) | (i10 << 1);
        }
        int i11 = 0;
        int i12 = i5;
        int i13 = i4;
        int i14 = i12;
        int i15 = i7;
        int i16 = i6;
        int i17 = i8;
        int i18 = i15;
        while (true) {
            i = 20;
            if (i11 >= 20) {
                break;
            }
            int i19 = i17 + iArr[i11] + 1518500249 + ((i13 << 5) | (i13 >>> 27)) + ((i14 & i16) | ((~i14) & i18));
            i11++;
            int i20 = i16;
            i16 = (i14 >>> 2) | (i14 << 30);
            i14 = i13;
            i13 = i19;
            i17 = i18;
            i18 = i20;
        }
        int i21 = i13;
        int i22 = i14;
        int i23 = i21;
        int i24 = i16;
        int i25 = i17;
        int i26 = i18;
        int i27 = i24;
        while (true) {
            i2 = 40;
            if (i >= 40) {
                break;
            }
            int i28 = i25 + iArr[i] + 1859775393 + ((i23 << 5) | (i23 >>> 27)) + ((i22 ^ i27) ^ i26);
            i++;
            int i29 = (i22 >>> 2) | (i22 << 30);
            i22 = i23;
            i23 = i28;
            i25 = i26;
            i26 = i27;
            i27 = i29;
        }
        int i30 = i22;
        int i31 = i23;
        int i32 = i30;
        int i33 = i25;
        int i34 = i26;
        int i35 = i27;
        int i36 = i33;
        while (true) {
            i3 = 60;
            if (i2 >= 60) {
                break;
            }
            int i37 = ((i36 + iArr[i2]) - 1894007588) + ((i31 << 5) | (i31 >>> 27)) + ((i32 & i35) | (i32 & i34) | (i35 & i34));
            i2++;
            int i38 = i35;
            i35 = (i32 >>> 2) | (i32 << 30);
            i32 = i31;
            i31 = i37;
            i36 = i34;
            i34 = i38;
        }
        int i39 = i31;
        int i40 = i32;
        int i41 = i39;
        int i42 = i34;
        int i43 = i35;
        int i44 = i36;
        int i45 = i42;
        while (i3 < 80) {
            int i46 = ((i44 + iArr[i3]) - 899497514) + ((i41 << 5) | (i41 >>> 27)) + ((i40 ^ i43) ^ i45);
            i3++;
            int i47 = (i40 >>> 2) | (i40 << 30);
            i40 = i41;
            i41 = i46;
            i44 = i45;
            i45 = i43;
            i43 = i47;
        }
        iArr[82] = iArr[82] + i41;
        iArr[83] = iArr[83] + i40;
        iArr[84] = iArr[84] + i43;
        iArr[85] = iArr[85] + i45;
        iArr[86] = iArr[86] + i44;
    }

    private static void updateHash(int[] iArr, byte[] bArr, int i, int i2) {
        int i3 = iArr[81];
        int i4 = i3 >> 2;
        int i5 = i3 & 3;
        iArr[81] = (((i3 + i2) - i) + 1) & 63;
        if (i5 != 0) {
            while (i <= i2 && i5 < 4) {
                iArr[i4] = iArr[i4] | ((bArr[i] & 255) << ((3 - i5) << 3));
                i5++;
                i++;
            }
            if (i5 == 4 && (i4 = i4 + 1) == 16) {
                computeHash(iArr);
                i4 = 0;
            }
            if (i > i2) {
                return;
            }
        }
        int i6 = ((i2 - i) + 1) >> 2;
        int i7 = i4;
        int i8 = i;
        for (int i9 = 0; i9 < i6; i9++) {
            iArr[i7] = ((bArr[i8] & 255) << 24) | ((bArr[i8 + 1] & 255) << 16) | ((bArr[i8 + 2] & 255) << 8) | (bArr[i8 + 3] & 255);
            i8 += 4;
            i7++;
            if (i7 >= 16) {
                computeHash(iArr);
                i7 = 0;
            }
        }
        int i10 = (i2 - i8) + 1;
        if (i10 != 0) {
            int i11 = (bArr[i8] & 255) << 24;
            if (i10 != 1) {
                i11 |= (bArr[i8 + 1] & 255) << 16;
                if (i10 != 2) {
                    i11 |= (bArr[i8 + 2] & 255) << 8;
                }
            }
            iArr[i7] = i11;
        }
    }
}
