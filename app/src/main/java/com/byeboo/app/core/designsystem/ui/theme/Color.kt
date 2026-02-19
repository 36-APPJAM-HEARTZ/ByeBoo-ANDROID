package com.byeboo.app.core.designsystem.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// primary
val primary50 = Color(0xFFF6F0FF)
val primary100 = Color(0xFFF1E9FF)
val primary200 = Color(0xFFE2D2FF)
val primary300 = Color(0xFFA16DFF)
val primary400 = Color(0xFF9162E6)
val primary500 = Color(0xFF8157CC)
val primary600 = Color(0xFF7952BF)
val primary700 = Color(0xFF614199)
val primary800 = Color(0xFF483173)
val primary900 = Color(0xFF382659)

// secondary
val secondary50 = Color(0xFFFFFAEE)
val secondary100 = Color(0xFFFFF8E5)
val secondary300 = Color(0xFFFFCD54)

// olive_green
val oliveGreen300 = Color(0xFF96CE5A)
val oliveGreen700 = Color(0xFF5A7C37)

// soft_blue
val softBlue200 = Color(0xFFD4EEFA)
val softBlue300 = Color(0xFF75C8EE)
val softBlue600 = Color(0xFF5896B3)

// sub
val sand100 = Color(0xFFFFF8E5)

val orange300 = Color(0xFFFDC6B0)

val warning50 = Color(0xFFFFF5E6)
val warning300 = Color(0xFFFFA01A)

// state
val success50 = Color(0xFFE8F4FC)
val success300 = Color(0xFF2C9CE3)

val error50 = Color(0xFFFFEBEC)
val error300 = Color(0xFFFF3B3E)

// grayscale
val gray50 = Color(0xFFFEFEFE)
val gray100 = Color(0xFFF4F4F4)
val gray200 = Color(0xFFE2E2E2)
val gray300 = Color(0xFFC4C4C4)
val gray400 = Color(0xFFA6A6A6)
val gray500 = Color(0xFF878787)
val gray600 = Color(0xFF6E6E6E)
val gray700 = Color(0xFF404040)
val gray800 = Color(0xFF2C2C2C)
val gray900 = Color(0xFF161616)

// black_and_white
val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)

// opacity
val blackAlpha80 = black.copy(alpha = 0.8f)
val blackAlpha50 = black.copy(alpha = 0.5f)

val whiteAlpha50 = white.copy(alpha = 0.5f)
val whiteAlpha5 = white.copy(alpha = 0.05f)

val primary300Alpha10 = primary300.copy(alpha = 0.1f)
val primary300Alpha20 = primary300.copy(alpha = 0.2f)
val primary50Alpha50 = primary50.copy(alpha = 0.5f)

val secondary300Alpha30 = secondary300.copy(alpha = 0.3f)
val secondary300Alpha10 = secondary300.copy(alpha = 0.1f)

val gray900Alpha80 = gray900.copy(alpha = 0.8f)

// kakao
val kakaoYellow = Color(0xFFFEE500)

// background
val background = Color(0xFF161616)

@Stable
class ByeBooColors(
    primary50: Color,
    primary100: Color,
    primary200: Color,
    primary300: Color,
    primary400: Color,
    primary500: Color,
    primary600: Color,
    primary700: Color,
    primary800: Color,
    primary900: Color,
    secondary50: Color,
    secondary100: Color,
    secondary300: Color,
    oliveGreen300: Color,
    oliveGreen700: Color,
    softBlue200: Color,
    softBlue300: Color,
    softBlue600: Color,
    sand100: Color,
    orange300: Color,
    warning50: Color,
    warning300: Color,
    success50: Color,
    success300: Color,
    error50: Color,
    error300: Color,
    gray50: Color,
    gray100: Color,
    gray200: Color,
    gray300: Color,
    gray400: Color,
    gray500: Color,
    gray600: Color,
    gray700: Color,
    gray800: Color,
    gray900: Color,
    black: Color,
    white: Color,
    blackAlpha80: Color,
    blackAlpha50: Color,
    whiteAlpha50: Color,
    whiteAlpha5: Color,
    primary300Alpha10: Color,
    primary300Alpha20: Color,
    primary50Alpha50: Color,
    secondary300Alpha30: Color,
    secondary300Alpha10: Color,
    gray900Alpha80: Color,
    kakaoYellow: Color,
    background: Color,
    isLight: Boolean,
) {
    var primary50 by mutableStateOf(primary50)
        private set
    var primary100 by mutableStateOf(primary100)
        private set
    var primary200 by mutableStateOf(primary200)
        private set
    var primary300 by mutableStateOf(primary300)
        private set
    var primary400 by mutableStateOf(primary400)
        private set
    var primary500 by mutableStateOf(primary500)
        private set
    var primary600 by mutableStateOf(primary600)
        private set
    var primary700 by mutableStateOf(primary700)
        private set
    var primary800 by mutableStateOf(primary800)
        private set
    var primary900 by mutableStateOf(primary900)
        private set
    var secondary50 by mutableStateOf(secondary50)
        private set
    var secondary100 by mutableStateOf(secondary100)
        private set
    var secondary300 by mutableStateOf(secondary300)
        private set
    var oliveGreen300 by mutableStateOf(oliveGreen300)
        private set
    var oliveGreen700 by mutableStateOf(oliveGreen700)
        private set
    var softBlue200 by mutableStateOf(softBlue200)
        private set
    var softBlue300 by mutableStateOf(softBlue300)
        private set
    var softBlue600 by mutableStateOf(softBlue600)
        private set
    var sand100 by mutableStateOf(sand100)
        private set
    var orange300 by mutableStateOf(orange300)
        private set
    var warning50 by mutableStateOf(warning50)
        private set
    var warning300 by mutableStateOf(warning300)
        private set
    var success50 by mutableStateOf(success50)
        private set
    var success300 by mutableStateOf(success300)
        private set
    var error50 by mutableStateOf(error50)
        private set
    var error300 by mutableStateOf(error300)
        private set
    var gray50 by mutableStateOf(gray50)
        private set
    var gray100 by mutableStateOf(gray100)
        private set
    var gray200 by mutableStateOf(gray200)
        private set
    var gray300 by mutableStateOf(gray300)
        private set
    var gray400 by mutableStateOf(gray400)
        private set
    var gray500 by mutableStateOf(gray500)
        private set
    var gray600 by mutableStateOf(gray600)
        private set
    var gray700 by mutableStateOf(gray700)
        private set
    var gray800 by mutableStateOf(gray800)
        private set
    var gray900 by mutableStateOf(gray900)
        private set
    var black by mutableStateOf(black)
        private set
    var white by mutableStateOf(white)
        private set
    var blackAlpha80 by mutableStateOf(blackAlpha80)
        private set
    var blackAlpha50 by mutableStateOf(blackAlpha50)
        private set
    var whiteAlpha50 by mutableStateOf(whiteAlpha50)
        private set
    var whiteAlpha5 by mutableStateOf(whiteAlpha5)
        private set
    var primary300Alpha10 by mutableStateOf(primary300Alpha10)
        private set
    var primary300Alpha20 by mutableStateOf(primary300Alpha20)
        private set
    var primary50Alpha50 by mutableStateOf(primary50Alpha50)
        private set
    var secondary300Alpha30 by mutableStateOf(secondary300Alpha30)
        private set
    var secondary300Alpha10 by mutableStateOf(secondary300Alpha10)
        private set
    var gray900Alpha80 by mutableStateOf(gray900Alpha80)
        private set
    var kakaoYellow by mutableStateOf(kakaoYellow)
        private set
    var background by mutableStateOf(background)
        private set
    var isLight by mutableStateOf(isLight)

    fun copy(): ByeBooColors =
        ByeBooColors(
            primary50,
            primary100,
            primary200,
            primary300,
            primary400,
            primary500,
            primary600,
            primary700,
            primary800,
            primary900,
            secondary50,
            secondary100,
            secondary300,
            oliveGreen300,
            oliveGreen700,
            softBlue200,
            softBlue300,
            softBlue600,
            sand100,
            orange300,
            warning50,
            warning300,
            success50,
            success300,
            error50,
            error300,
            gray50,
            gray100,
            gray200,
            gray300,
            gray400,
            gray500,
            gray600,
            gray700,
            gray800,
            gray900,
            black,
            white,
            blackAlpha80,
            blackAlpha50,
            whiteAlpha50,
            whiteAlpha5,
            primary300Alpha10,
            primary300Alpha20,
            primary50Alpha50,
            secondary300Alpha30,
            secondary300Alpha10,
            gray900Alpha80,
            kakaoYellow,
            background,
            isLight,
        )

    fun update(colors: ByeBooColors) {
        primary50 = colors.primary50
        primary100 = colors.primary100
        primary200 = colors.primary200
        primary300 = colors.primary300
        primary400 = colors.primary400
        primary500 = colors.primary500
        primary600 = colors.primary600
        primary700 = colors.primary700
        primary800 = colors.primary800
        primary900 = colors.primary900
        secondary50 = colors.secondary50
        secondary100 = colors.secondary100
        secondary300 = colors.secondary300
        oliveGreen300 = colors.oliveGreen300
        oliveGreen700 = colors.oliveGreen700
        softBlue200 = colors.softBlue200
        softBlue300 = colors.softBlue300
        softBlue600 = colors.softBlue600
        sand100 = colors.sand100
        orange300 = colors.orange300
        warning50 = colors.warning50
        warning300 = colors.warning300
        success50 = colors.success50
        success300 = colors.success300
        error50 = colors.error50
        error300 = colors.error300
        gray50 = colors.gray50
        gray100 = colors.gray100
        gray200 = colors.gray200
        gray300 = colors.gray300
        gray400 = colors.gray400
        gray500 = colors.gray500
        gray600 = colors.gray600
        gray700 = colors.gray700
        gray800 = colors.gray800
        gray900 = colors.gray900
        black = colors.black
        white = colors.white
        blackAlpha80 = colors.blackAlpha80
        blackAlpha50 = colors.blackAlpha50
        whiteAlpha50 = colors.whiteAlpha50
        whiteAlpha5 = colors.whiteAlpha5
        primary300Alpha10 = colors.primary300Alpha10
        primary300Alpha20 = colors.primary300Alpha20
        primary50Alpha50 = colors.primary50Alpha50
        secondary300Alpha30 = colors.secondary300Alpha30
        secondary300Alpha10 = colors.secondary300Alpha10
        gray900Alpha80 = colors.gray900Alpha80
        kakaoYellow = colors.kakaoYellow
        background = colors.background
        isLight = colors.isLight
    }
}

fun ByeBooDarkColors(
    Primary50: Color = primary50,
    Primary100: Color = primary100,
    Primary200: Color = primary200,
    Primary300: Color = primary300,
    Primary400: Color = primary400,
    Primary500: Color = primary500,
    Primary600: Color = primary600,
    Primary700: Color = primary700,
    Primary800: Color = primary800,
    Primary900: Color = primary900,
    Secondary50: Color = secondary50,
    Secondary100: Color = secondary100,
    Secondary300: Color = secondary300,
    OliveGreen300: Color = oliveGreen300,
    OliveGreen700: Color = oliveGreen700,
    SoftBlue200: Color = softBlue200,
    SoftBlue300: Color = softBlue300,
    SoftBlue600: Color = softBlue600,
    Sand100: Color = sand100,
    Orange300: Color = orange300,
    Warning50: Color = warning50,
    Warning300: Color = warning300,
    Success50: Color = success50,
    Success300: Color = success300,
    Error50: Color = error50,
    Error300: Color = error300,
    Gray50: Color = gray50,
    Gray100: Color = gray100,
    Gray200: Color = gray200,
    Gray300: Color = gray300,
    Gray400: Color = gray400,
    Gray500: Color = gray500,
    Gray600: Color = gray600,
    Gray700: Color = gray700,
    Gray800: Color = gray800,
    Gray900: Color = gray900,
    Black: Color = black,
    White: Color = white,
    BlackAlpha80: Color = blackAlpha80,
    BlackAlpha50: Color = blackAlpha50,
    WhiteAlpha50: Color = whiteAlpha50,
    WhiteAlpha5: Color = whiteAlpha5,
    Primary300Alpha10: Color = primary300Alpha10,
    Primary300Alpha20: Color = primary300Alpha20,
    Primary50Alpha50: Color = primary50Alpha50,
    Secondary300Alpha30: Color = secondary300Alpha30,
    Secondary300Alpha10: Color = secondary300Alpha10,
    Gray900Alpha80: Color = gray900Alpha80,
    KakaoYellow: Color = kakaoYellow,
    Background: Color = background,
) = ByeBooColors(
    Primary50,
    Primary100,
    Primary200,
    Primary300,
    Primary400,
    Primary500,
    Primary600,
    Primary700,
    Primary800,
    Primary900,
    Secondary50,
    Secondary100,
    Secondary300,
    OliveGreen300,
    OliveGreen700,
    SoftBlue200,
    SoftBlue300,
    SoftBlue600,
    Sand100,
    Orange300,
    Warning50,
    Warning300,
    Success50,
    Success300,
    Error50,
    Error300,
    Gray50,
    Gray100,
    Gray200,
    Gray300,
    Gray400,
    Gray500,
    Gray600,
    Gray700,
    Gray800,
    Gray900,
    Black,
    White,
    BlackAlpha80,
    BlackAlpha50,
    WhiteAlpha50,
    WhiteAlpha5,
    Primary300Alpha10,
    Primary300Alpha20,
    Primary50Alpha50,
    Secondary300Alpha30,
    Secondary300Alpha10,
    Gray900Alpha80,
    KakaoYellow,
    Background,
    isLight = true,
)
