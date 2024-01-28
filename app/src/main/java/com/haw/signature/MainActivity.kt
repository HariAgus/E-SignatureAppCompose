package com.haw.signature

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import com.haw.signature.ui.theme.SampleSignatureEversheetTheme
import com.joelkanyi.composesignature.ComposeSignature

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleSignatureEversheetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {
    val titles = listOf("Text", "Draw")
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tabIndex) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            when (tabIndex) {
                0 -> {
                    SignatureText()
                }

                1 -> {
                    SignatureDraw()
                }

                /*2 -> {
                    SignatureUpload()
                }*/
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureText() {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val bitmap: Bitmap = remember { createBitmap(640, 100) }

    var text by remember { mutableStateOf(TextFieldValue()) }

    val canvas = Canvas(bitmap.asImageBitmap())
    val paint = Paint()

    LaunchedEffect(text.text) {

        // Clear image and drawing background white
        canvas.nativeCanvas.drawColor(Color(0xFFF1F0EF).toArgb())
        paint.color = Color.Black.toArgb()
        paint.typeface = ResourcesCompat.getFont(context, R.font.fuggles_regular)
        paint.textSize = 130F
        paint.textAlign = Paint.Align.CENTER

        // Drawing a new text
        canvas.nativeCanvas.drawText(text.text, 300f, 50f, paint)
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = "Sign Name"
            )

            Spacer(modifier = Modifier.padding(16.dp))

            BasicTextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = { value ->
                    text = value
                },
                textStyle = TextStyle(color = Color.Black, fontFamily = FontFamily.Monospace),
                decorationBox = {
                    TextFieldDefaults.OutlinedTextFieldDecorationBox(
                        value = text.text,
                        innerTextField = it,
                        singleLine = true,
                        enabled = true,
                        placeholder = { Text("Write your name") },
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.textFieldColors()
                    )
                }
            )

            Spacer(modifier = Modifier.padding(14.dp))

            ButtonClearSignature(
                modifier = Modifier.align(Alignment.Bottom),
                onClear = {

                }
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(top = 16.dp),
            color = Color(0xFFF1F0EF),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "Signature",
                    fontSize = 12.sp,
                    color = Color(0xFF777777)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    bitmap = remember { bitmap.asImageBitmap() },
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.terms_and_condition),
            fontSize = 10.sp,
            lineHeight = 14.sp,
            color = Color(0xFF777777),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(0.dp),
                color = Color(0xFFAAAAAA)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "CANCEL", color = Color.White, textAlign = TextAlign.Center
                )
            }
            Surface(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(0.dp),
                color = Color(0xFF274c77)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "CREATE & SIGN", color = Color.White, textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun SignatureDraw() {
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            color = Color(0xFFF1F0EF),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp),
                        text = "Draw Here",
                        fontSize = 12.sp,
                        color = Color(0xFF777777)
                    )

                    // Draw Signature
                    ComposeSignature(
                        modifier = Modifier
                            .fillMaxSize(),
                        signaturePadCardElevation = 0.dp,
                        signaturePadColor = Color(0xFFF1F0EF),
                        signaturePadHeight = 400.dp,
                        signatureColor = Color.Black,
                        signatureThickness = 10f,
                        onComplete = { signature ->
                            imageBitmap = signature.asImageBitmap()
                        },
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                ButtonClearSignature(
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp)
                        .align(Alignment.End),
                    onClear = {
                        imageBitmap = null
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.terms_and_condition),
            fontSize = 10.sp,
            lineHeight = 14.sp,
            color = Color(0xFF777777),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(0.dp),
                color = Color(0xFFAAAAAA)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "CANCEL", color = Color.White, textAlign = TextAlign.Center
                )
            }
            Surface(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(0.dp),
                color = Color(0xFF274c77)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "CREATE & SIGN", color = Color.White, textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SignatureUpload() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Comming Soon",
            fontSize = 24.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun ButtonClearSignature(
    modifier: Modifier = Modifier,
    onClear: () -> Unit
) {
    Text(
        modifier = modifier
            .clickable {
                onClear()
            },
        text = "Clear",
        style = TextStyle(textDecoration = TextDecoration.Underline),
        fontWeight = FontWeight.Bold,
        color = Color(0xFF800B0B)
    )
}