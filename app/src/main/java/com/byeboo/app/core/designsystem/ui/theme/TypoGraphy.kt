package com.byeboo.app.core.designsystem.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.byeboo.app.R

val pretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))
val pretendardMedium = FontFamily(Font(R.font.pretendard_medium, FontWeight.Medium))
val pretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))

private fun ByeBooTextStyle(
    fontFamily: FontFamily,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
): TextStyle =
    TextStyle(
        fontFamily = fontFamily,
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        lineHeightStyle =
            LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
    )

@Stable
class ByeBooTypography internal constructor(
    head1: TextStyle,
    head2: TextStyle,
    sub1: TextStyle,
    sub2: TextStyle,
    sub3: TextStyle,
    sub4: TextStyle,
    body1: TextStyle,
    body2: TextStyle,
    body3: TextStyle,
    body4: TextStyle,
    body5: TextStyle,
    body6: TextStyle,
    cap1: TextStyle,
    cap2: TextStyle,
) {
    var head1 by mutableStateOf(head1)
        private set
    var head2 by mutableStateOf(head2)
        private set
    var sub1 by mutableStateOf(sub1)
        private set
    var sub2 by mutableStateOf(sub2)
        private set
    var sub3 by mutableStateOf(sub3)
        private set
    var sub4 by mutableStateOf(sub4)
        private set
    var body1 by mutableStateOf(body1)
        private set
    var body2 by mutableStateOf(body2)
        private set
    var body3 by mutableStateOf(body3)
        private set
    var body4 by mutableStateOf(body4)
        private set
    var body5 by mutableStateOf(body5)
        private set
    var body6 by mutableStateOf(body6)
        private set
    var cap1 by mutableStateOf(cap1)
        private set
    var cap2 by mutableStateOf(cap2)
        private set

    fun copy(): ByeBooTypography =
        ByeBooTypography(
            head1 = head1,
            head2 = head2,
            sub1 = sub1,
            sub2 = sub2,
            sub3 = sub3,
            sub4 = sub4,
            body1 = body1,
            body2 = body2,
            body3 = body3,
            body4 = body4,
            body5 = body5,
            body6 = body6,
            cap1 = cap1,
            cap2 = cap2,
        )

    fun update(typography: ByeBooTypography) {
        head1 = typography.head1
        head2 = typography.head2
        sub1 = typography.sub1
        sub2 = typography.sub2
        sub3 = typography.sub3
        sub4 = typography.sub4
        body1 = typography.body1
        body2 = typography.body2
        body3 = typography.body3
        body4 = typography.body4
        body5 = typography.body5
        body6 = typography.body6
        cap1 = typography.cap1
        cap2 = typography.cap2
    }
}

@Composable
fun ByeBooTypography(): ByeBooTypography =
    ByeBooTypography(
        head1 =
            ByeBooTextStyle(
                fontFamily = pretendardMedium,
                fontSize = 24.sp,
                lineHeight = 31.sp,
                letterSpacing = 0.em,
            ),
        head2 =
            ByeBooTextStyle(
                fontFamily = pretendardMedium,
                fontSize = 22.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.em,
            ),
        sub1 =
            ByeBooTextStyle(
                fontFamily = pretendardSemiBold,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.em,
            ),
        sub2 =
            ByeBooTextStyle(
                fontFamily = pretendardSemiBold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.em,
            ),
        sub3 =
            ByeBooTextStyle(
                fontFamily = pretendardMedium,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.em,
            ),
        sub4 =
            ByeBooTextStyle(
                fontFamily = pretendardRegular,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.em,
            ),
        body1 =
            ByeBooTextStyle(
                fontFamily = pretendardSemiBold,
                fontSize = 16.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.em,
            ),
        body2 =
            ByeBooTextStyle(
                fontFamily = pretendardMedium,
                fontSize = 16.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.em,
            ),
        body3 =
            ByeBooTextStyle(
                fontFamily = pretendardRegular,
                fontSize = 16.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.em,
            ),
        body4 =
            ByeBooTextStyle(
                fontFamily = pretendardSemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.em,
            ),
        body5 =
            ByeBooTextStyle(
                fontFamily = pretendardMedium,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.em,
            ),
        body6 =
            ByeBooTextStyle(
                fontFamily = pretendardRegular,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.em,
            ),
        cap1 =
            ByeBooTextStyle(
                fontFamily = pretendardMedium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.em,
            ),
        cap2 =
            ByeBooTextStyle(
                fontFamily = pretendardRegular,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.em,
            ),
    )
