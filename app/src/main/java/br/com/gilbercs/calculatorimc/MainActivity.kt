package br.com.gilbercs.calculatorimc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.gilbercs.calculatorimc.ui.theme.CalculatorIMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorIMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(0xFFF067B4),
                                    Color(0xFF81FFEF)
                                )
                            )
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Titulo da aplicação, utilizando as strings criadas anteriormente
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 10.dp),
                            text = stringResource(id = R.string.title_home),
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        HomeImc()
                    }
                }
            }
        }
    }

        @OptIn(ExperimentalAnimationApi::class)
        @Composable
        fun HomeImc() {
            //Declaração da varivéis para entrada de dados conforme regra
            var peso by remember { mutableStateOf("") }
            var altura by remember { mutableStateOf("") }
            var result_imc by remember { mutableStateOf("") }
            var show by remember { mutableStateOf(false) }
            val context = LocalContext.current
            val requestFocus = FocusRequester()
            //aplicar uma margens e alinhar os campos
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
                //campos para entrada de dados: Peso
                    OutlinedTextField(
                        modifier = Modifier.focusRequester(requestFocus),
                        value = peso,
                        onValueChange = { peso = it },
                        label = { Text(text = stringResource(id = R.string.text_peso), fontWeight = FontWeight.Bold,color = Color.White, fontSize = 21.sp)},
                        placeholder = { Text(text = stringResource(id = R.string.text_exemple_peso), color = Color.White)},
                        textStyle = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Medium, color = Color.White),
                        leadingIcon = { Icon(tint = Color.White, painter = painterResource(id = R.drawable.ic_peso), contentDescription = "Peso") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )
                Space1()
                //campos para entrada de dados: Altura
                OutlinedTextField(
                    value = altura,
                    onValueChange = { altura = it },
                    label = { Text(text = stringResource(id = R.string.text_altura), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 21.sp) },
                    placeholder = { Text(text = stringResource(id = R.string.text_exemple_altura), color = Color.White) },
                    textStyle = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Medium, color = Color.White),
                    leadingIcon = { Icon(tint = Color.White, painter = painterResource(id = R.drawable.ic_altura), contentDescription = "altura")},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        if (peso.isNotEmpty() && altura.isNotEmpty()) {
                            //Ação no teclado
                            result_imc = calculatorImc(peso,altura)
                            show = true
                        } else {
                            Toast.makeText(context,"Preencha os campos",Toast.LENGTH_LONG).show()
                            requestFocus.requestFocus()
                        }
                    },
                    singleLine = true
                )
                Space1()
                //Button Criação de evento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly){
                    OutlinedButton(
                        onClick = {
                            peso = ""
                            altura = ""
                            show = false
                            requestFocus.requestFocus()
                        }) {
                        Text(text = stringResource(id = R.string.buttom_clean))
                    }
                    OutlinedButton(
                        onClick = {
                            if (peso.isNotEmpty() && altura.isNotEmpty()) {
                                //Ação
                                result_imc = calculatorImc(peso,altura)
                                show = true
                            } else {
                                Toast.makeText(context,"Preencha os campos",Toast.LENGTH_LONG).show()
                                requestFocus.requestFocus()
                            }
                        }) {
                        Text(text = stringResource(id = R.string.buttom_calculator))
                    }
                }
                //saida de dados: resultado animação
                //Resultado
                AnimatedVisibility(
                    visible = show,
                    enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                    exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
                ) {
                    Text(
                        text = result_imc,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                }
            }
        }
    //Função para calcular IMC retornando uma string
    fun calculatorImc(weight: String, height: String):String {
        //Receber dados de entrada e converter em double
        val peso = weight.toDouble()
        val altura = height.toDouble()
        //Processamento: formula para o calculo do IMC
        val calculeImc = peso / (altura*altura)
        //Formatar Numero
        val resultFormt: Double = String.format("%.1f", calculeImc).toDouble()
        //Função para classificação IMC: Recebi por parametro o resultado do
        // calculo já convertido em double e retorna uma String com resultado da
        //classificação
        return classificationImc(resultImc = resultFormt)
    }
    //
    //função para classificar o resultado do IMC: Recibe no para o Double
    fun classificationImc(resultImc: Double):String{
        var resulClassificationtImc = ""
        //Classificação conforme tabela da OMS a partir do IMC
        if (resultImc <18.5f){
            //MENOR QUE 18,5 -> Baixo pesso - Baixo
            resulClassificationtImc = "Baixo Peso"
        }else{
            if (resultImc>=18.5 && resultImc <24.9){
                //ENTRE 18,5 E 24,9  -> Normal - Adequado
                resulClassificationtImc = "Peso Adequado"
            }else{
                if (resultImc == 25.0){
                    //Igual a 25  -> Normal - Risco
                    resulClassificationtImc = "Risco de SobrePeso"
                }else{
                    if (resultImc > 25 && resultImc<=29.9){
                        //ENTRE 25,0 E 29,9 -> Sobrepreso Aumentado
                        resulClassificationtImc = "Pré-Obeso"
                    }else{
                        if(resultImc>=30 && resultImc<=34.9){
                            //ENTRE 30,0 E 34,9 -> Obesidade - Moderado
                            resulClassificationtImc = "Obesidade GRAU I"
                        }else{
                            if (resultImc>=35 && resultImc<=39.9){
                                //ENTRE 35,0 E 39,9 -> Obesidade Mórbida - Grave
                                resulClassificationtImc = "Obesidade GRAU II"
                            }else{
                                //resultImc > 40
                                //Igual ou Maior 40  -> Obesidade Mórbida - Muito Grave
                                resulClassificationtImc = "Obesidade GRAU III"
                            }
                        }
                    }
                }
            }
        }
        return "Seu Imc É: ${resultImc}\n\n"+resulClassificationtImc+"\n"
    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        CalculatorIMCTheme {
            HomeImc()
        }
    }
        //função para criar espaço
        @Composable
        fun Space1() {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
