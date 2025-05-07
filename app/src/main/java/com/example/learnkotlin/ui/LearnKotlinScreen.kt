package com.example.learnkotlin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learnkotlin.R
import com.example.learnkotlin.ui.theme.LearnKotlinTheme

sealed class Screen(val route: String){
    object MainScreen: Screen("main")
    object ArticleScreen: Screen("main/{chapterId}/article/{articleId}"){
        fun createRoute(chapterId: Int, articleId: Int) = "main/$chapterId/article/$articleId"
    }

}
@Composable
fun ActionsButton(onClick: () -> Unit){
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box{
        IconButton(
            onClick = { expanded = true}
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false},
        ) {
            DropdownMenuItem(
                onClick = {
                    onClick()
                    expanded = false
                },
                leadingIcon ={
                    Icon(
                        painter = painterResource(R.drawable.themechange),
                        contentDescription = stringResource(R.string.theme_change)
                    )} ,
                text = {
                    Text(
                        text = stringResource(R.string.theme_change),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
            DropdownMenuItem(
                onClick = {},
                leadingIcon ={
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search)
                    )} ,
                text = {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    text: String,
    canNavigateBack: Boolean,
    back: ()-> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    changeTheme: () -> Unit){

    CenterAlignedTopAppBar(
        title = {
            Row{
                if(!canNavigateBack) {
                    Image(
                        painter = painterResource(R.drawable.kotlin),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(R.dimen.logo_size))
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_micro)))
                }
                Text(
                    text = text,
                    textAlign = TextAlign.Center
                )
            }
        },
        navigationIcon = {
            if(canNavigateBack){
            IconButton(
                onClick = back
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null)
                }
            }
        },
        actions = {
            ActionsButton(
                onClick = changeTheme
            )
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.shadow(dimensionResource(R.dimen.padding_medium))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnKotlinScreen(){
    val vm: AppViewModel = viewModel()

    val context = LocalContext.current
    val chapters by remember { mutableStateOf(vm.loadChapters(context)) }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var title = ""
    var isLastArticle = false
    var canGoBackToPreviousArticle = false
    var isLastInGeneral = false
    when(backStackEntry?.destination?.route){
        Screen.MainScreen.route ->{
           title = stringResource(R.string.app_name)
        }
        Screen.ArticleScreen.route ->{
            val chapterId = backStackEntry!!.arguments!!.getInt("chapterId")
            val articleId = backStackEntry!!.arguments!!.getInt("articleId")
            val chapter = chapters.first{it.chapterId == chapterId}
            title = vm.getArticle(chapters, chapterId, articleId).title
            isLastArticle = if(chapter.articles.size == articleId) true
            else false
            canGoBackToPreviousArticle = if(articleId !=1) true else false
            isLastInGeneral = if (chapterId == chapters.size && articleId == chapter.articles.size) true else false
        }
        else -> {
            isLastArticle = false
            title = ""
        }
    }

    val uiState by vm.uiState.collectAsState()

    LearnKotlinTheme(darkTheme = uiState.isDarkTheme) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    canNavigateBack = backStackEntry?.destination?.route != Screen.MainScreen.route,
                    back = { navController.navigate(Screen.MainScreen.route) },
                    text = title,
                    scrollBehavior = scrollBehavior,
                    changeTheme = {
                        vm.switchTheme()
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.MainScreen.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(route = Screen.MainScreen.route) {
                    MainScreen(
                        chapters,
                        onArticleClick = { chapterId, articleId ->
                            navController.navigate(
                                Screen.ArticleScreen.createRoute(
                                    chapterId,
                                    articleId
                                )
                            )
                        }
                    )
                }
                composable(
                    route = Screen.ArticleScreen.route,
                    arguments = listOf(
                        navArgument("chapterId") { type = NavType.IntType },
                        navArgument("articleId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val chapterId = backStackEntry.arguments!!.getInt("chapterId")
                    val articleId = backStackEntry.arguments!!.getInt("articleId")
                    val article = vm.getArticle(chapters, chapterId, articleId)
                    ArticleScreen(article,
                        isDark = uiState.isDarkTheme,
                        context = context,
                        isLastArticle = isLastArticle,
                        switchArticle = {
                            navController.navigate(
                                Screen.ArticleScreen.createRoute(
                                    chapterId,
                                    articleId+1
                                )
                            )
                        },
                        switchChapter = {
                            if(chapterId+1 <= chapters.size) {
                                navController.navigate(
                                    Screen.ArticleScreen.createRoute(
                                        chapterId + 1,
                                        1
                                    )
                                )
                            }
                        },
                        canGoBack = canGoBackToPreviousArticle,
                        goBack = {navController.navigate(
                            Screen.ArticleScreen.createRoute(
                                chapterId,
                                articleId-1)
                                    )
                                 },
                         lastInGeneral = isLastInGeneral)
                }
            }

        }
    }
}