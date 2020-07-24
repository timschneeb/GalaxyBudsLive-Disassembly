package android.app;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import androidx.core.content.ContextCompat;
import com.samsung.android.fotaprovider.log.Log;
import com.samsung.android.sdk.mobileservice.social.group.provider.GroupMemberContract;
import com.sec.android.fotaprovider.R;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class SpinnerTimePickerDialog extends TimePickerDialog {
    public SpinnerTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener onTimeSetListener, int i, int i2, boolean z) {
        super(context, onTimeSetListener, i, i2, z);
        applySpinnerForcefully(context, i, i2, z);
    }

    public SpinnerTimePickerDialog(Context context, int i, TimePickerDialog.OnTimeSetListener onTimeSetListener, int i2, int i3, boolean z) {
        super(context, i, onTimeSetListener, i2, i3, z);
        applySpinnerForcefully(context, i2, i3, z);
    }

    private void applySpinnerForcefully(Context context, int i, int i2, boolean z) {
        Context context2 = context;
        if (Build.VERSION.SDK_INT == 24) {
            try {
                TimePicker timePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
                Field findField = findField(TimePicker.class, Class.forName("android.widget.TimePicker$TimePickerDelegate"), "mDelegate");
                Object obj = findField.get(timePicker);
                Class<?> cls = Class.forName("android.widget.TimePickerSpinnerDelegate");
                if (obj.getClass() != cls) {
                    findField.set(timePicker, (Object) null);
                    timePicker.removeAllViews();
                    Constructor<?> constructor = cls.getConstructor(new Class[]{TimePicker.class, Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE});
                    constructor.setAccessible(true);
                    findField.set(timePicker, constructor.newInstance(new Object[]{timePicker, context2, null, 16843933, Integer.valueOf(R.style.FotaProviderTheme_Widget_TimePicker)}));
                    timePicker.setIs24HourView(Boolean.valueOf(z));
                    timePicker.setHour(i);
                    timePicker.setMinute(i2);
                    timePicker.setOnTimeChangedListener(this);
                    setNumberPickerTextAttributes(context2, (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier("amPm", GroupMemberContract.GroupMember.ID, "android")));
                    setNumberPickerTextAttributes(context2, (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier("hour", GroupMemberContract.GroupMember.ID, "android")));
                    setNumberPickerTextAttributes(context2, (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier("minute", GroupMemberContract.GroupMember.ID, "android")));
                }
            } catch (Exception e) {
                Log.W(e.toString());
            }
        }
    }

    private static void setNumberPickerTextAttributes(Context context, NumberPicker numberPicker) {
        int childCount = numberPicker.getChildCount();
        int color = ContextCompat.getColor(context, R.color.dialog_buttonbarbutton_textcolor);
        float dimension = context.getResources().getDimension(R.dimen.dialog_timepicker_textsize);
        for (int i = 0; i < childCount; i++) {
            EditText editText = (EditText) numberPicker.getChildAt(i);
            editText.setTextColor(color);
            editText.setTextSize(0, dimension);
            try {
                Field declaredField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                declaredField.setAccessible(true);
                Paint paint = (Paint) declaredField.get(numberPicker);
                paint.setColor(color);
                paint.setTextSize(dimension);
            } catch (Exception e) {
                Log.W(e.toString());
            }
        }
        numberPicker.invalidate();
    }

    private static Field findField(Class cls, Class cls2, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e) {
            Log.W(e.toString());
            for (Field field : cls.getDeclaredFields()) {
                if (field.getType() == cls2) {
                    field.setAccessible(true);
                    return field;
                }
            }
            return null;
        }
    }
}
