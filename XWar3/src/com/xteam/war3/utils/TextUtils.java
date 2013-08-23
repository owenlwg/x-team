package com.xteam.war3.utils;

import com.xteam.war3.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

public class TextUtils {
	
	private Context mContext;
	private ForegroundColorSpan textForegroundColorSpan;
	private ForegroundColorSpan titleForegroundColorSpan;
	private RelativeSizeSpan relativeSizeSpan;
//	private StyleSpan styleSpan;
	
	public TextUtils(Context context) {
		mContext = context;
		textForegroundColorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_blue));
		relativeSizeSpan = new RelativeSizeSpan(1.2f);
//		styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
		titleForegroundColorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_red));
	}
	
	public void setTextStyle(SpannableStringBuilder span, int start, int end, String content) {
		span.clear();
		span.append(content);
		span.setSpan(textForegroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(relativeSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		span.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	public void setTitleStyle(SpannableStringBuilder span, String content) {
		span.clear();
		span.append(content);
		int start = content.indexOf("vs");
		span.setSpan(titleForegroundColorSpan, start, start + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	public String getGameUrl(int index) {
		String[] gameUrls = mContext.getResources().getStringArray(R.array.game_url);
		return gameUrls[index];
	}
	
	public static String ToDBC(String input) {
		   char[] c = input.toCharArray();
		   for (int i = 0; i< c.length; i++) {
		       if (c[i] == 12288) {
		         c[i] = (char) 32;
		         continue;
		       }if (c[i]> 65280&& c[i]< 65375)
		          c[i] = (char) (c[i] - 65248);
		       }
		   return new String(c);
	}
	
    public static String halfWidthToFullWidth(String s) {
        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
    
    
}
