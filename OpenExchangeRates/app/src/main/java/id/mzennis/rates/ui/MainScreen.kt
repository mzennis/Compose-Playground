package id.mzennis.rates.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.mzennis.rates.ui.model.MainIntent
import id.mzennis.rates.ui.model.ScreenState
import id.mzennis.rates.ui.model.UiEvent
import id.mzennis.rates.ui.model.UiState
import id.mzennis.rates.ui.theme.Green
import id.mzennis.rates.ui.theme.OpenExchangeRatesTheme
import id.mzennis.rates.ui.theme.Red
import id.mzennis.rates.ui.theme.Typography
import id.mzennis.rates.ui.theme.Yellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.OpenLink -> {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(event.externalLink)
                        )
                    )
                }
            }
        }
    }

    MainContent(
        uiState = uiState.value,
        dispatch = {
            viewModel.onIntent(it)
        }
    )
}

@Composable
fun MainContent(uiState: UiState, dispatch: (MainIntent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExchangeRatesFormUi(
            currencyCodes = uiState.currencyCodes,
            dispatch = dispatch,
            modifier = Modifier.padding(16.dp)
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp, 8.dp)
        ) {
            Text(text = "result")
            Text(text = uiState.convertedAmount, style = Typography.displayMedium)
        }
        ProgressUi(screenState = uiState.screenState, modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant))
        AppInformationUi(
            lastUpdated = uiState.lastUpdated,
            appId = uiState.appId,
            dispatch = dispatch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun ExchangeRatesFormUi(
    currencyCodes: List<String>,
    dispatch: (MainIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    var fromCurrencyCode by rememberSaveable { mutableStateOf("") }
    var toCurrencyCode by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = amount) {
        delay(1500)
        dispatch(MainIntent.Convert(fromCurrencyCode, toCurrencyCode, amount))
    }

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ExpandedDropdownUi(
                label = "from",
                options = currencyCodes,
                onSelectedItem = {
                    fromCurrencyCode = it
                    dispatch(MainIntent.Convert(fromCurrencyCode, toCurrencyCode, amount))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            ExpandedDropdownUi(
                label = "to",
                options = currencyCodes,
                onSelectedItem = {
                    toCurrencyCode = it
                    dispatch(MainIntent.Convert(fromCurrencyCode, toCurrencyCode, amount))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
        InputTextFieldUi(
            label = "amount",
            onValueChanged = {
                amount = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextFieldUi(
    label: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var value by rememberSaveable { mutableStateOf("") }
    val maxDigit = 15

    TextField(
        value = value,
        onValueChange = {
            if (it.length <= maxDigit) {
                value = it
                onValueChanged.invoke(value)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = modifier,
        supportingText = {
            Text(
                text = "${value.length} / $maxDigit",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedDropdownUi(
    label: String,
    options: List<String>,
    onSelectedItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { newValue ->
            expanded = newValue
        },
        modifier = modifier
    ) {
        TextField(
            value = selectedOptionText,
            onValueChange = { selectedOptionText = it },
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        val filteringOptions = options.filter { it.contains(selectedOptionText, ignoreCase = true) }
        if (filteringOptions.isEmpty()) return@ExposedDropdownMenuBox
        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(true),
            properties = PopupProperties(focusable = false),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            filteringOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onSelectedItem.invoke(selectedOptionText)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun ProgressUi(screenState: ScreenState, modifier: Modifier = Modifier) {
    val (label, color) = when(screenState) {
        ScreenState.Loading -> "Loading..." to Yellow
        ScreenState.Available -> "Ready to serve" to Green
        ScreenState.UnAvailable -> "Unavailable" to Red
    }
    Text(text = label, color = color, modifier = modifier)
}

@Composable
fun AppInformationUi(
    lastUpdated: String,
    appId: String,
    dispatch: (MainIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val openExchangeRatesStr = buildAnnotatedString {
            append("Consistent, reliable exchange rate data from ")

            pushStringAnnotation(tag = "doc", annotation = "https://openexchangerates.org")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("https://openexchangerates.org")
            }
            pop()
        }
        ClickableText(
            text = openExchangeRatesStr,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            onClick = { offset ->
                openExchangeRatesStr.getStringAnnotations(tag = "doc", start = offset, end = offset)
                    .firstOrNull()?.let {
                    dispatch.invoke(MainIntent.OpenLink(it.item))
                }
            }
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))
        Text(text = "last updated: $lastUpdated")
        Button(onClick = { dispatch.invoke(MainIntent.UpdateRates) }) {
            Text(text = "update now")
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))
        Text(text = "This app using App ID:")
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp))
        Text(text = appId, style = Typography.titleLarge,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp, 8.dp))
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))
        val creatorStr = buildAnnotatedString {
            append("Author: ")

            pushStringAnnotation(tag = "author", annotation = "https://github.com/mzennis")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("github.com/mzennis")
            }
            pop()
        }
        ClickableText(
            text = creatorStr,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            onClick = { offset ->
                creatorStr.getStringAnnotations(tag = "author", start = offset, end = offset)
                    .firstOrNull()?.let {
                        dispatch.invoke(MainIntent.OpenLink(it.item))
                    }
            }
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))
        val completeSourceCodeStr = buildAnnotatedString {
            append("you can find the complete source code on: ")

            pushStringAnnotation(tag = "source-code", annotation = "https://github.com/mzennis/exchange-rates")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("github.com/mzennis/exchange-rates")
            }
            pop()
        }
        ClickableText(
            text = completeSourceCodeStr,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            onClick = { offset ->
                completeSourceCodeStr.getStringAnnotations(
                    tag = "source-code",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    dispatch.invoke(MainIntent.OpenLink(it.item))
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    OpenExchangeRatesTheme {
        MainScreen()
    }
}