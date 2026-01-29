package com.example.app01_auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.app01_auth.ui.theme.App01authTheme



class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.setContext(this)

        enableEdgeToEdge()
        setContent {
            App01authTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainView(mainViewModel)

                }
            }
        }
    }



    @Composable

    fun MainView(
        viewModel: MainViewModel
    ) {


        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            val Title = if (viewModel.userIsAuthenticated) {

                stringResource(R.string.logged_in_title)
            } else {
                /// 👇🏽👇🏽👇🏽 Updated code
                if (viewModel.appJustLaunched) {

                    stringResource(R.string.initial_title)
                } else {
                    stringResource(R.string.logged_out_title)
                }
            }
            Title(

                text = Title

            )


            if (viewModel.userIsAuthenticated) {

                UserInfoRow(
                    label = stringResource(R.string.name_label),
                    value = "Name goes here",
                )
                UserInfoRow(
                    label = stringResource(R.string.email_label),
                    value = "Email goes here",
                )
                UserPicture(
                    url = stringResource(R.string.user_icon_url),
                    description = "Description goes here",
                )
            }

            // Button
            // ------
            val buttonText: String
            val onClickAction: () -> Unit
            /// 👇🏽👇🏽👇🏽 Updated code
            if (viewModel.userIsAuthenticated) {
                /// 👆🏽👆🏽👆🏽
                buttonText = stringResource(R.string.log_out_button)
                /// 👇🏽👇🏽👇🏽 Updated code
                onClickAction = { viewModel.logout() }
                /// 👆🏽👆🏽👆🏽
            } else {
                buttonText = stringResource(R.string.log_in_button)
                /// 👇🏽👇🏽👇🏽 Updated code
                onClickAction = { viewModel.login() }
                /// 👆🏽👆🏽👆🏽
            }
            LogButton(
                text = buttonText,
                onClick = onClickAction,
            )
        }
    }

    @Composable
    fun Title(
        text: String,
    )
    {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
            )
        )
    }

    @Composable
    fun LogButton(  // 1
        text: String,
        onClick: () -> Unit,
    ) {
        Column(  // 2
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(  // 3
                onClick = { onClick() },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
            ) {
                Text(  // 4
                    text = text,
                    fontSize = 20.sp,
                )
            }
        }
    }


    @Composable
    fun UserInfoRow(
        label: String,
        value: String,
    ) {
        Row {  // 1
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            )
            Spacer( // 2
                modifier = Modifier.width(10.dp),
            )
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp,
                )
            )
        }
    }

    @Composable
    fun UserPicture(  // 1
        url: String,
        description: String,
    ) {
        Column(  // 2
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(  // 3
                painter = rememberAsyncImagePainter(url),
                contentDescription = description,
                modifier = Modifier
                    .fillMaxSize(0.5f),
            )
        }
    }

}


