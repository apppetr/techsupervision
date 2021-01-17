package ru.lihachev.norm31937.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import ru.lihachev.norm31937.free.R;

public final class SelectableButton
  extends AppCompatButton
{
  private boolean l;
  
  public SelectableButton(Context paramContext)
  {
    super(paramContext);
  }
  
  public SelectableButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SelectableButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private final void setSelectedButton(boolean paramBoolean)
  {
    l = paramBoolean;
  }
  
  public final void a(boolean paramBoolean)
  {
    l = paramBoolean;
    if (!paramBoolean)
    {
      setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.abc_background_cache_hint_selector_material_dark));
      setTextColor(-16777216);
      return;
    }
    setSupportBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.azure));
    setTextColor(-1);
  }
  
  public final boolean n()
  {
    return l;
  }
}
