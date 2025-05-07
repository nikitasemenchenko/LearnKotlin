package com.example.learnkotlin.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import com.example.learnkotlin.model.Article
import com.example.learnkotlin.model.Block
import com.example.learnkotlin.R
import com.example.learnkotlin.ui.theme.IDEcolor
import com.example.learnkotlin.ui.theme.nextButtonColor

@Composable
fun ArticleScreen(article: Article,
                  isDark: Boolean,
                  context: Context,
                  isLastArticle: Boolean,
                  switchChapter: () -> Unit,
                  switchArticle: () -> Unit,
                  canGoBack: Boolean,
                  goBack: () -> Unit,
                  lastInGeneral: Boolean){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(article.content){block ->
            when(block){
                is Block.Text -> {
                    TextBlock(block.text)
                }
                is Block.Code-> {
                    CodeBlock(block.code, isDark)
                }
                is Block.Image->{
                    ImageBlock(block.img)
                }
                is Block.Subtitle -> {
                    SubtitleBlock(block.text)
                }
                is Block.Url -> {
                    UrlBlock(block.url, context)
                }
            }
        }
        item {
                Row {
                    if(canGoBack)CustomBackButton(goBack, isLastArticle = isLastArticle)
                    if(!lastInGeneral) {
                        if(isLastArticle) CustomNextButton(switchChapter, isLastArticle)
                        else CustomNextButton(switchArticle, isLastArticle)
                    }
                }
            }
        }
    }

@Composable
fun TextBlock(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            lineBreak = LineBreak.Paragraph,
            hyphens = Hyphens.Auto
        )
    )
}

@Composable
fun CodeBlock(text: String,
              isDark: Boolean){
    Surface(
        tonalElevation = dimensionResource(R.dimen.padding_micro),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_small))
            .border(
                width = dimensionResource(R.dimen.border_size),
                color = Color.Gray
            )
    ) {
        SelectionContainer(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .background(color = if (isDark) IDEcolor else Color.White)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun ImageBlock(image: String){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val imageId = remember(image) {
            context.resources.getIdentifier(
                image,
                "drawable",
                context.packageName
            )
        }
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.image_size))
        )
    }
}

@Composable
fun SubtitleBlock(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun UrlBlock(url: String, context: Context){
    Text(
        text = url,
        color = Color.Blue,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}

@Composable
fun CustomBackButton(goBack: () -> Unit,
                     isLastArticle: Boolean){
    Button(
        onClick = goBack,
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_small)),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isLastArticle) Color.Transparent else nextButtonColor,
            contentColor = if(isLastArticle) nextButtonColor else MaterialTheme.colorScheme.background
        ),
        border = if(isLastArticle) BorderStroke(width = dimensionResource(R.dimen.border_size),
            color = nextButtonColor) else BorderStroke(width = dimensionResource(R.dimen.zero_size),color = Color.Transparent)
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = null,
        )
    }
}

@Composable
fun CustomNextButton(
    goNext: () -> Unit,
    isLastArticle: Boolean
){
    Button(
        onClick = goNext,
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_small))
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isLastArticle) Color.Transparent else nextButtonColor,
            contentColor = if(isLastArticle) nextButtonColor else MaterialTheme.colorScheme.background
        ),
        border = if(isLastArticle) BorderStroke(width = dimensionResource(R.dimen.border_size),
            color = nextButtonColor) else BorderStroke(width = dimensionResource(R.dimen.zero_size),color = Color.Transparent)
    ) {
        Text(
            text = if (isLastArticle) stringResource(R.string.next_chapter) else stringResource(R.string.next_article),
            style = MaterialTheme.typography.bodyLarge)
    }
}