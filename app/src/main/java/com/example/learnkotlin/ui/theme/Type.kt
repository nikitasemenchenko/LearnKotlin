package com.example.learnkotlin.ui.theme

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.learnkotlin.R

val default = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val forCode = FontFamily(
    Font(R.font.mono)
)


val Typography = Typography(
    bodyLarge = TextStyle(  //основной текст
        fontFamily = default,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(  //Заголовок
        fontFamily = default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(  //Субзаголовок
        fontFamily = default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    displayLarge = TextStyle(  //Код
        fontFamily = forCode,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    )
)